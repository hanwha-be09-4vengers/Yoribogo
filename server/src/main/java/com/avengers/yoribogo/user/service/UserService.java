package com.avengers.yoribogo.user.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.avengers.yoribogo.user.domain.vo.signup.RequestResistAdminUserVO;
import com.avengers.yoribogo.user.domain.vo.signup.RequestResistEnterpriseUserVO;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.dto.profile.RequestUpdateUserDTO;
import com.avengers.yoribogo.user.dto.validate.BooleanResponseDTO;
import com.avengers.yoribogo.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
// 설명. UserDetailsService 인터페이스 구현
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AmazonS3Client s3Client;
    private final StringRedisTemplate stringRedisTemplate;  // StringRedisTemplate 사용
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(
             UserRepository userRepository
            , AmazonS3Client s3Client
            , StringRedisTemplate stringRedisTemplate  // Redis template 추가
            , ModelMapper modelMapper
            , BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.userRepository = userRepository;
        this.s3Client = s3Client;
        this.stringRedisTemplate = stringRedisTemplate;  // 주입
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 설명. 이름, 가입 구분, 이메일로 사용자 찾기
    public UserDTO findUserByUserNicknameAndSignupPathAndEmail(String nickname, SignupPath signupPath, String email) {
        UserEntity userEntity = userRepository.findByNicknameAndSignupPathAndEmail(nickname, signupPath, email)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return modelMapper.map(userEntity, UserDTO.class);
    }

    // 설명. 아이디와 이메일로 사용자 찾기
    public UserDTO findUserByUserAuthIdAndEmail(String userAuthId, String email) {
        UserEntity userEntity = userRepository.findByUserAuthIdAndEmail(userAuthId, email)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        return modelMapper.map(userEntity, UserDTO.class);
    }


    // 설명. 사용자 식별자( userIdentifier)로 조회
    public UserEntity findByUserIdentifier(String userIdentifier) {
        return userRepository.findByUserIdentifier(userIdentifier)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }


    // 설명. userAuthId로 사용자 조회
    public UserEntity findByUserAuthId(String userAuthId) {
        return userRepository.findByUserAuthId(userAuthId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
    }

    /* 설명. 로그인 시 security가 자동으로 호출하는 메소드 */
    @Override
    public UserDetails loadUserByUsername(String userAuthId) throws UsernameNotFoundException {
        // 1. userAuthId를 기준으로 사용자 조회
        UserEntity loginUser = userRepository.findByUserAuthId(userAuthId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 2. 비밀번호 처리 (소셜 로그인 시 비밀번호가 없을 경우 기본값 설정)
        String encryptedPwd = loginUser.getEncryptedPwd();
        if (encryptedPwd == null) {
            encryptedPwd = "{noop}";  // 비밀번호가 없을 경우 기본값 설정
        }

        // 3. 권한 정보를 userRole 필드에서 가져와서 변환
        List<GrantedAuthority> grantedAuthorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + loginUser.getUserRole().name()) // "ROLE_ADMIN" 또는 "ROLE_ENTERPRISE"
        );

        // 4. UserDetails 객체 반환
        return new User(loginUser.getUserAuthId(), encryptedPwd,
                true, true, true, true,
                grantedAuthorities);
    }


    /* 설명. 일반 회원가입 메서드 */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDTO registUser(RequestResistEnterpriseUserVO newUser) {

        // 1. 동일한 UserIdentifier가 존재하는지 확인 (중복 검증)
        userRepository.findByUserIdentifier("NORMAL_" + newUser.getUserAuthId())
                .ifPresent(user -> {
                    throw new CommonException(ErrorCode.EXIST_USER_ID);
                });

        // 2. 이메일 인증 여부 확인 (이메일이 있을 때만)
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            String emailVerificationStatus = stringRedisTemplate.opsForValue().get(newUser.getEmail());
            if (!"True".equals(emailVerificationStatus)) {
                throw new CommonException(ErrorCode.EMAIL_VERIFICATION_REQUIRED);
            }
        }

        // 3. 닉네임 중복 검증
        if (newUser.getNickname() == null || newUser.getNickname().isEmpty()) {
            throw new CommonException(ErrorCode.INVALID_INPUT_NICKNAME);
        }

        userRepository.findByNickname(newUser.getNickname())
                .ifPresent(user -> {
                    throw new CommonException(ErrorCode.DUPLICATE_NICKNAME_EXISTS);
                });

        // 4. 기본 프로필 이미지 설정 (추후 S3로 교체 가능)
        String defaultProfileImageUrl = "https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png";

        // 5. UserDTO 생성
        UserDTO newUserDTO = UserDTO.builder()
                .userAuthId(newUser.getUserAuthId())
                .userName(newUser.getUserName())
                .email(newUser.getEmail())
                .signupPath(SignupPath.NORMAL)
                .createdAt(LocalDateTime.now().withNano(0))
                .acceptStatus(AcceptStatus.Y)
                .userStatus(ActiveStatus.ACTIVE)
                .nickname(newUser.getNickname())
                .profileImage(defaultProfileImageUrl)
                .tierId(1L)
                .userLikes(0L)
                .userIdentifier("NORMAL_" + newUser.getUserAuthId())  // user_identifier 생성
                .userRole(UserRole.ENTERPRISE)  // 일반 사용자로 설정
                .build();

        // 6. DTO -> Entity 변환
        UserEntity userEntity = modelMapper.map(newUserDTO, UserEntity.class);

        // 7. 비밀번호 암호화
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(newUser.getPassword()));

        // 8. Entity 저장 후 반환된 Entity 가져오기
        UserEntity savedEntity = userRepository.save(userEntity);

        // 9. 회원가입 성공 후 Redis에서 이메일 인증 키 삭제
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            stringRedisTemplate.delete(newUser.getEmail());
        }

        // 10. 저장된 Entity를 DTO로 변환하여 반환
        return modelMapper.map(savedEntity, UserDTO.class);
    }

    /* 설명. 관리자 회원가입 메서드 */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserDTO registAdminUser(RequestResistAdminUserVO newUser) {

        // 1. 동일한 UserIdentifier가 존재하는지 확인 (중복 검증)
        userRepository.findByUserIdentifier("ADMIN_" + newUser.getUserAuthId())
                .ifPresent(user -> {
                    throw new CommonException(ErrorCode.EXIST_USER_ID);
                });

        // 2. 이메일 인증 여부 확인 (이메일이 있을 때만)
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            String emailVerificationStatus = stringRedisTemplate.opsForValue().get(newUser.getEmail());
            if (!"True".equals(emailVerificationStatus)) {
                throw new CommonException(ErrorCode.EMAIL_VERIFICATION_REQUIRED);
            }
        }

        // 3. 관리자 기본 프로필 이미지 설정 (추후 S3로 교체 가능)
        String defaultProfileImageUrl = "https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/admin_default_profile.png";

        // 4. UserDTO 생성
        UserDTO newUserDTO = UserDTO.builder()
                .userAuthId(newUser.getUserAuthId())
                .userName(newUser.getUserName())
                .email(newUser.getEmail())
                .signupPath(SignupPath.ADMIN)
                .createdAt(LocalDateTime.now().withNano(0))
                .acceptStatus(AcceptStatus.Y)
                .userStatus(ActiveStatus.ACTIVE)
                .profileImage(defaultProfileImageUrl)
                .userIdentifier("ADMIN_" + newUser.getUserAuthId())  // user_identifier 생성
                .userRole(UserRole.ADMIN)  // 일반 사용자로 설정
                .build();

        // 5. DTO -> Entity 변환
        UserEntity userEntity = modelMapper.map(newUserDTO, UserEntity.class);

        // 6. 비밀번호 암호화
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(newUser.getPassword()));

        // 7. Entity 저장 후 반환된 Entity 가져오기
        UserEntity savedEntity = userRepository.save(userEntity);

        // 8. 회원가입 성공 후 Redis에서 이메일 인증 키 삭제
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            stringRedisTemplate.delete(newUser.getEmail());
        }

        // 10. 저장된 Entity를 DTO로 변환하여 반환
        return modelMapper.map(savedEntity, UserDTO.class);
    }

    // 설명. 닉네임 중복 여부 확인
    public BooleanResponseDTO getUserByNicknameForDuplicate(String nickname) {
        // 닉네임이 존재하는지 여부를 BooleanResponseDTO로 바로 매핑
        return new BooleanResponseDTO(userRepository.findByNickname(nickname).isPresent());
    }

    // 설명. 사용자 인증 아이디 중복 여부 확인
    public BooleanResponseDTO getUserByUserAuthId(String userAuthId) {
        // 사용자 인증 아이디가 존재하는지 여부를 BooleanResponseDTO로 바로 매핑
        return new BooleanResponseDTO(userRepository.findByUserAuthId(userAuthId).isPresent());
    }

    /**
     * 사용자 계정을 비활성화하는 메서드.
     */
    public UserEntity deactivateUser(Long userId) {
        // Optional 처리 및 예외 발생
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 사용자 상태를 비활성화(INACTIVE)로 변경
        userEntity.deactivateUser();

        // 변경된 상태를 저장하고 반환
        return userRepository.save(userEntity);
    }

    /**
     * 사용자 계정을 활성화하는 메서드.
     */
    public UserEntity activateUser(String userAuthId) {
        // Optional 처리 및 예외 발생
        UserEntity userEntity = userRepository.findByUserIdentifier("NORMAL_" + userAuthId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 사용자 상태를 활성화(ACTIVE)로 변경
        userEntity.activateUser();

        // 변경된 상태를 저장하고 반환
        return userRepository.save(userEntity);
    }

    // 설명. 로그인 전 사용자 비밀번호 재설정
    public UserEntity updatePassword(String userAuthId, String newPassword) {
        // 1. 사용자 ID를 통해 사용자를 조회
        UserEntity userEntity = userRepository.findByUserIdentifier("NORMAL_" + userAuthId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 2. 비번 재설정시 이메일 인증여부 확인
        if (userEntity.getEmail() != null && !userEntity.getEmail().isEmpty()) {
            String emailVerificationStatus = stringRedisTemplate.opsForValue().get(userEntity.getEmail());
            if (!"True".equals(emailVerificationStatus)) {
                log.error("이메일 인증이 완료되지 않았습니다: {}", userEntity.getEmail());
                throw new CommonException(ErrorCode.EMAIL_VERIFICATION_REQUIRED); // 이메일 인증이 필요하다는 커스텀 예외 던지기
            }
        }

        // 3. 새로운 비밀번호를 암호화하여 설정
        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        userEntity.setEncryptedPwd(encryptedPassword);

        // 4. 암호화된 비밀번호를 저장하고 사용자 정보를 업데이트
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        // 5. 비번 재설정 성공 후 Redis에서 이메일 키 삭제
        if (userEntity.getEmail() != null && !userEntity.getEmail().isEmpty()) {
            stringRedisTemplate.delete(userEntity.getEmail());
        }

        // 6. 업데이트된 사용자 엔티티 반환
        return updatedUserEntity;
    }

    // 설명. 로그인 후 사용자 비밀번호 재설정
    public UserEntity updateLoggedInPassword(Long userId, String newPassword) {
        // 1. 사용자 ID를 통해 사용자를 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

        // 2. 새로운 비밀번호를 암호화하여 설정
        String encryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        userEntity.setEncryptedPwd(encryptedPassword);

        // 3. 암호화된 비밀번호를 저장하고 사용자 정보를 업데이트
        UserEntity updatedUserEntity = userRepository.save(userEntity);

        // 4. 업데이트된 사용자 엔티티 반환
        return updatedUserEntity;
    }

    /**설명.
     *  사용자의 프로필(닉네임, 이미지)을 업데이트하는 메서드.
     *설명.
     * @param userId 프로필을 업데이트할 사용자의 ID
     * @param userUpdateDTO 업데이트할 정보가 담긴 DTO 객체
     * @return 업데이트된 UserEntity 객체
     * @throws CommonException 사용자가 존재하지 않을 경우 발생
     */
    public UserEntity updateProfile(Long userId, RequestUpdateUserDTO userUpdateDTO) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
        UserEntity userEntity = user.get();

        String imageUrl = null;

        // 닉네임 중복 검증(null이 아니고 기존과 같지 않은 경우에)
        if (userUpdateDTO.getNickname() != null ) {

            if (userUpdateDTO.getNickname().equals(userEntity.getNickname()))
            {
                throw new CommonException(ErrorCode.DUPLICATE_NICKNAME);
            }

            Optional<UserEntity> existingUserWithSameNickname = userRepository.findByNickname(userUpdateDTO.getNickname());
            if (existingUserWithSameNickname.isPresent()) {
                throw new CommonException(ErrorCode.DUPLICATE_NICKNAME_EXISTS); // 커스텀 예외 던지기 (DUPLICATE_NICKNAME은 정의된 에러 코드로 가정)
            }
        }


        // 프로필 이미지가 제공된 경우에만 처리
        if (userUpdateDTO.getProfileImage() != null && !userUpdateDTO.getProfileImage().isEmpty()) {
            // 기존 이미지가 있으면 삭제
            if (userEntity.getProfileImage() != null && !userEntity.getProfileImage().isEmpty()) {
                deleteProfileImage(userEntity.getProfileImage());
            }
            // 새로운 이미지를 업로드하고 URL을 얻음
            imageUrl = uploadProfileImage(userUpdateDTO.getProfileImage(), userId);
        }

        // 닉네임이나 이미지가 null일 수 있으므로 기존 값을 유지할지 여부를 확인
        String updatedNickname = userUpdateDTO.getNickname() != null ? userUpdateDTO.getNickname() : userEntity.getNickname();
        String updatedImageUrl = imageUrl != null ? imageUrl : userEntity.getProfileImage();

        // 프로필 업데이트
        userEntity.updateProfile(updatedNickname, updatedImageUrl);
        return userRepository.save(userEntity);
    }

    /**설명.
     *  S3에서 기존 프로필 이미지를 삭제하는 메서드.
     *설명.
     * @param fileUrl 삭제할 파일의 S3 URL
     */
    public void deleteProfileImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());

        log.info("Attempting to delete file from S3: " + fileName);

        try {
            // S3에서 파일 삭제 요청
            s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
            log.info("Successfully deleted image from S3: " + fileName);
        } catch (AmazonClientException e) {
            log.error("Failed to delete image from S3: " + fileName, e);
        }
    }



    /** 설명.
     *  MultipartFile을 S3에 업로드하고, 업로드된 파일의 URL을 반환하는 메서드.
     * 설명.
     * @param profileImage 업로드할 프로필 이미지 파일
     * @param userId 사용자의 ID로 파일명을 지정
     * @return 업로드된 파일의 S3 URL
     * @throws CommonException 파일 업로드에 실패할 경우 발생
     */
    private String uploadProfileImage(MultipartFile profileImage, Long userId) {
        String originalFileName = profileImage.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();  // 확장자를 소문자로 변환
        String fileName = "user_" + userId + fileExtension;  // 사용자 ID를 기반으로 파일명 생성

        try {
            // ObjectMetadata 객체 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(profileImage.getSize());

            // MIME 타입 설정 (MultipartFile에서 ContentType 가져오기)
            String contentType = profileImage.getContentType();
            if (contentType != null) {
                metadata.setContentType(contentType);  // Content-Type 설정
            } else {
                metadata.setContentType("application/octet-stream");  // 기본 값 설정
            }

            // Content-Disposition을 inline으로 설정
            metadata.setContentDisposition("inline");

            // S3에 파일 업로드
            s3Client.putObject(new PutObjectRequest(bucket, fileName, profileImage.getInputStream(), metadata));

            // 업로드된 파일의 S3 URL 반환
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (AmazonClientException | IOException e) {
            log.error("S3에 이미지 업로드 실패", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

}

package com.avengers.yoribogo.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.AcceptStatus;
import com.avengers.yoribogo.user.domain.enums.ActiveStatus;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.domain.enums.UserRole;
import com.avengers.yoribogo.user.domain.vo.signup.RequestResistEnterpriseUserVO;
import com.avengers.yoribogo.user.dto.UserDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

}

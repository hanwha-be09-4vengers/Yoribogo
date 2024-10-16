package com.avengers.yoribogo.user.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.dto.EnterpriseUserDTO;
import com.avengers.yoribogo.user.dto.UserDTO;
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

import java.util.ArrayList;
import java.util.List;

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
    public UserService(UserRepository userRepository
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
        UserEntity userEntity = userRepository.findByNicknameAndSignupPathAndEmail(nickname, signupPath, email);
        if (userEntity == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
        return modelMapper.map(userEntity, UserDTO.class);
    }

    // 설명. 아이디와 이메일로 사용자 찾기
    public UserDTO findUserByUserAuthIdAndEmail(String userAuthId, String email) {
        UserEntity userEntity = userRepository.findByUserAuthIdAndEmail(userAuthId, email);
        if (userEntity == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
        return modelMapper.map(userEntity, UserDTO.class);
    }

    // 설명. 사용자 식별자( userIdentifier)로 조회
    public UserEntity findByUserIdentifier(String userIdentifier) {
        UserEntity userEntity =userRepository.findByUserIdentifier(userIdentifier);
        if (userEntity == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }
        return userEntity;
    }

    /* 설명. 로그인 시 security가 자동으로 호출하는 메소드 */
    @Override
    public UserDetails loadUserByUsername(String userIdentifier) throws UsernameNotFoundException {

        /* 설명. 넘어온 user_auth_id와 signupPath를 사용자가 입력한 id로써 회원을 조회하는 쿼리 메소드 작성 */
        UserEntity loginUser = userRepository.findByUserIdentifier(userIdentifier);
        if (loginUser == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_USER);
        }

        // 소셜 로그인으로 가입된 사용자의 경우 비밀번호가 없을 수 있으므로 기본 비밀번호 설정
        String encryptedPwd = loginUser.getEncryptedPwd();
        if (encryptedPwd == null) {
            // 비밀번호가 없을 경우 임의의 문자열이나 빈 문자열을 설정
            encryptedPwd = "{noop}"; // 또는 필요한 기본값 설정
        }

        /* 설명. 사용자의 권한들을 가져왔다는 가정 */
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ENTERPRISE"));

        return new User(loginUser.getUserAuthId(), encryptedPwd,
                true, true, true, true,
                grantedAuthorities);
    }

}

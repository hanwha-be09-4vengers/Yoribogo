package com.avengers.yoribogo.user.service;


import com.amazonaws.services.s3.AmazonS3Client;

import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.domain.enums.SignupPath;
import com.avengers.yoribogo.user.dto.UserDTO;
import com.avengers.yoribogo.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;



@Service("userCommandServiceImpl")
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AmazonS3Client s3Client;

    private final StringRedisTemplate stringRedisTemplate;  // StringRedisTemplate 사용


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /* 설명. security 모듈 추가 후 암호화를 위해 BCryptPasswordEncoder bean 주입 */
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

//    @Autowired
//    public UserServiceImpl(UserRepository userRepository
//            , AmazonS3Client s3Client
//            , ModelMapper modelMapper
//            // , BCryptPasswordEncoder bCryptPasswordEncoder
//            ) {
//        this.userRepository = userRepository;
//        this.s3Client = s3Client;
//        this.modelMapper = modelMapper;
////        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//    }
    @Autowired
    public UserService(UserRepository userRepository
            , AmazonS3Client s3Client
            , ModelMapper modelMapper
            , StringRedisTemplate stringRedisTemplate) {
        this.userRepository = userRepository;
        this.s3Client = s3Client;
        this.modelMapper = modelMapper;
        this.stringRedisTemplate=stringRedisTemplate;
    }


    // 이름, 가입 구분, 이메일로 사용자 찾기
    public UserDTO findUserByUserNicknameAndSignupPathAndEmail(String nickname, SignupPath signupPath, String email) {
        UserEntity userEntity = userRepository.findByNicknameAndSignupPathAndEmail(nickname, signupPath, email);
        if (userEntity == null) {
            return null;
        }
        return modelMapper.map(userEntity, UserDTO.class);
    }

    // 아이디와 이메일로 사용자 찾기
    public UserDTO findUserByUserAuthIdAndEmail(String userAuthId, String email) {
        UserEntity userEntity = userRepository.findByUserAuthIdAndEmail(userAuthId, email);
        if (userEntity == null) {
            return null;
        }
        return modelMapper.map(userEntity, UserDTO.class);
    }

}

//package com.avengers.yoribogo.security;
//
//import com.avengers.yoribogo.common.exception.CommonException;
//import com.avengers.yoribogo.user.domain.UserEntity;
//import com.avengers.yoribogo.user.domain.enums.UserRole;
//import com.avengers.yoribogo.user.domain.vo.login.AuthTokens;
//import com.avengers.yoribogo.user.service.UserService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.security.Key;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//    private final Logger log = LoggerFactory.getLogger(JwtUtilTest.class);
//
//    @Mock
//    private UserService userService;
//    private UserEntity user;
//
//    // application.yml에서 값을 읽어옴
//    @Value("${token.secret}")
//    private String secretKeyBase64;
//
//    @Value("${token.access-expiration-time}")
//    private long accessExpirationTime;
//
//    @Value("${token.refresh-expiration-time}")
//    private long refreshExpirationTime;
//
//    // 테스트 초기화: Mock 객체 설정 및 JwtUtil 초기화
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Base64로 인코딩된 secretKey를 디코딩
//        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKeyBase64);
//        Key secretKey = Keys.hmacShaKeyFor(secretKeyBytes); // 디코딩된 시크릿 키를 사용
//
//        // JwtUtil 인스턴스 생성
//        jwtUtil = new JwtUtil(secretKeyBase64, accessExpirationTime, refreshExpirationTime, userService);
//
//        // 기본 사용자 정보 설정
//        user = new UserEntity();
//        user.setUserIdentifier("user123");
//        user.setEmail("test@example.com");
//        user.setUserRole(UserRole.ADMIN);
//
//        log.info("테스트 준비 완료 - JwtUtil 및 UserEntity 초기화 완료");
//    }
//
//    // JWT 토큰 생성 테스트
//    @Test
//    @DisplayName("JWT 토큰 생성 테스트")
//    void testGenerateToken() {
//        // Given: 사용자와 역할 정보 설정
//        List<String> roles = Collections.singletonList("ADMIN");
//
//        // When: 토큰 생성
//        String token = jwtUtil.generateToken(user, roles);
//        log.info("생성된 토큰: {}", token);
//
//        // Then: 토큰이 정상적으로 생성되었는지 확인
//        assertNotNull(token);
//        log.info("JWT 토큰 생성 성공");
//
//        // JWT Claims 추출
//        Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtil.getSecretKey()).build().parseClaimsJws(token).getBody();
//        log.info("JWT Claims 추출 성공: {}", claims);
//
//        // Claims에서 값을 확인
//        assertEquals("user123", claims.getSubject());
//        assertEquals("test@example.com", claims.get("email"));
//
//        // "auth"는 리스트로 저장되었으므로 리스트로 변환 후 확인
//        List<String> authClaims = (List<String>) claims.get("auth");
//        assertEquals(1, authClaims.size());
//        assertEquals("ADMIN", authClaims.get(0));  // 리스트에서 첫 번째 요소 확인
//
//        assertEquals("user123", claims.get("userIdentifier"));
//        log.info("JWT Claims 검증 성공");
//    }
//
//    // 리프레시 토큰을 사용해 새로운 액세스 토큰을 발급받는 테스트
//    @Test
//    @DisplayName("리프레시 토큰으로 액세스 토큰 재발급 테스트 (시간 로그 포함)")
//    void testRefreshAccessToken() {
//        // Given: 유효한 리프레시 토큰 생성
//        String refreshToken = jwtUtil.generateRefreshToken(user, Collections.singletonList("ADMIN"));
//        log.info("생성된 리프레시 토큰: {}", refreshToken);
//
//        // 만료 시간 로그 찍기
//        long refreshTokenExpiration = jwtUtil.getRefreshTokenExpiration();
//        log.info("리프레시 토큰 만료 시간 (epoch): {}", refreshTokenExpiration);
//        log.info("리프레시 토큰 만료 시간 (Date): {}", new Date(refreshTokenExpiration));
//
//        // Mock UserService에서 사용자 정보 반환 설정
//        when(userService.findByUserIdentifier(anyString())).thenReturn(user);
//
//        // When: 리프레시 토큰으로 액세스 토큰 재발급
//        long beforeReissueTime = System.currentTimeMillis();  // 재발급 전의 시간 기록
//        AuthTokens tokens = jwtUtil.refreshAccessToken(refreshToken);
//        long afterReissueTime = System.currentTimeMillis();   // 재발급 후의 시간 기록
//
//        // 재발급된 액세스 토큰 만료 시간 로그 찍기
//        long accessTokenExpiration = tokens.getAccessTokenExpiry();
//        log.info("재발급된 액세스 토큰: {}", tokens.getAccessToken());
//        log.info("재발급된 액세스 토큰 만료 시간 (epoch): {}", accessTokenExpiration);
//        log.info("재발급된 액세스 토큰 만료 시간 (Date): {}", new Date(accessTokenExpiration));
//
//        // Then: 새로운 액세스 토큰이 정상적으로 생성되었는지 확인
//        assertNotNull(tokens.getAccessToken(), "액세스 토큰이 정상적으로 발급되지 않았습니다.");
//        assertEquals(refreshToken, tokens.getRefreshToken(), "리프레시 토큰은 그대로 유지되어야 합니다.");
//
//        // 재발급된 액세스 토큰의 만료 시간이 적절한지 확인 (현재 시간보다 이후여야 함)
//        assertTrue(accessTokenExpiration > afterReissueTime, "액세스 토큰 만료 시간이 현재 시간보다 이후여야 합니다.");
//        log.info("액세스 토큰 만료 시간은 적절하게 설정되었습니다.");
//
//        // 발급 시각과 만료 시각 비교 (정상적으로 액세스 토큰이 발급되었는지 확인)
//        assertTrue(accessTokenExpiration > beforeReissueTime, "액세스 토큰 만료 시간이 발급 전 시간보다 이후여야 합니다.");
//        assertTrue(accessTokenExpiration > afterReissueTime, "액세스 토큰 만료 시간이 발급 후 시간보다 이후여야 합니다.");
//        log.info("액세스 토큰 만료 시간 검증 성공");
//
//        // 리프레시 토큰 만료 시간도 확인 (리프레시 토큰은 기존 것 유지)
//        assertTrue(refreshTokenExpiration > beforeReissueTime, "리프레시 토큰 만료 시간도 현재 시간보다 이후여야 합니다.");
//        log.info("리프레시 토큰 만료 시간도 적절하게 설정되었습니다.");
//    }
//
//
//    // 잘못된 토큰을 검증할 때 예외가 발생하는지 테스트
//    @Test
//    @DisplayName("잘못된 토큰 검증 실패 테스트")
//    void testInvalidToken() {
//        // Given: 잘못된 토큰
//        String invalidToken = "Invalid.JWT.Token";
//        log.info("잘못된 토큰: {}", invalidToken);
//
//        // When & Then: 잘못된 토큰 검증 시 -> CommonException 발생
//        assertThrows(CommonException.class, () -> jwtUtil.validateToken(invalidToken));
//        log.info("잘못된 토큰 검증 시 CommonException 발생 테스트 성공");
//    }
//}

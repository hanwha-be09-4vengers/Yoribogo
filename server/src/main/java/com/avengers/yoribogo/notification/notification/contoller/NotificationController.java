package com.avengers.yoribogo.notification.notification.contoller;

import com.avengers.yoribogo.notification.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // SSE 연결 엔드포인트
    @GetMapping("/sseconnect")
    public SseEmitter sseConnect(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or missing Authorization header");
        }

        String jwtToken = token.substring(7);  // "Bearer " 이후의 실제 토큰 추출

        // JWT 토큰 검증 로직 추가 (예: 유효성 검사, 토큰 파싱)
        // 예: JWT 유효성 검증 실패 시 ResponseStatusException을 던질 수 있음

        SseEmitter emitter = new SseEmitter(0L);  // 무한 타임아웃 설정
        return emitter;
    }

}

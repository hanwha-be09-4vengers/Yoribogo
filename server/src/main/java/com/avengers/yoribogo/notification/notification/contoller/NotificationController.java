package com.avengers.yoribogo.notification.notification.contoller;

import com.avengers.yoribogo.notification.notification.dto.NotificationEntity;
import com.avengers.yoribogo.notification.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // SSE 연결 엔드포인트
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/sseconnect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToNotifications() {
        log.info("(contoller)SSE 연결 지나갑니당 ~~!");
        return notificationService.subscribe();  // SSE 연결을 서비스로 위임
    }

    // 알림 저장 테스트 API
    @PostMapping("/inserttest")
    public ResponseEntity<NotificationEntity> createNotification() {
        // 필요한 데이터는 service에서 직접 설정
        NotificationEntity savedNotification = notificationService.createNotification();
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    // 알림 보내는 api

    // 사용자가 알림을 읽으면 ( 클릭하면 ) READ 상태로 바뀌면서 READ_at 이 now().witnano(0)로 바뀌는 api
}

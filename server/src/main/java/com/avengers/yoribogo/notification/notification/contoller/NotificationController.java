package com.avengers.yoribogo.notification.notification.contoller;

import com.avengers.yoribogo.notification.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}

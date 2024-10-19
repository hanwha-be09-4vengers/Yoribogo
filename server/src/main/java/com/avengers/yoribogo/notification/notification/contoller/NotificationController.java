package com.avengers.yoribogo.notification.notification.contoller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.notification.dto.NotificationEntity;
import com.avengers.yoribogo.notification.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

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
    @GetMapping(value = "/sseConnect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToNotifications() {
        log.info("(controller)SSE 연결 지나갑니당 ~~!");
        return notificationService.subscribe();  // SSE 연결을 서비스로 위임
    }

    // 알림 저장 테스트 API
    @PostMapping("/insertTest")
    public ResponseEntity<NotificationEntity> createNotification() {
        // 필요한 데이터는 service에서 직접 설정
        NotificationEntity savedNotification = notificationService.createNotification();
        return new ResponseEntity<>(savedNotification, HttpStatus.CREATED);
    }

    // 사용자에게 알림을 전송하고, 전송된 알림 데이터를 Response로 반환
    @GetMapping("/send/{userId}")
    // 시큐리티 적용 시 PathVariable 방식 쓸 필요 없음 수정 필요.
    public ResponseEntity<ResponseDTO<List<NotificationEntity>>> sendUserNotifications(@PathVariable("userId") Long userId) {
            List<NotificationEntity> userNotifications = notificationService.sendNotificationsToUser(userId);

        if (userNotifications == null) {
            throw new CommonException(ErrorCode. NOT_FOUND_NOTIFICATION);
        } else{
            return ResponseEntity.ok(ResponseDTO.ok(userNotifications));}
    }


    // 사용자가 알림을 읽으면 ( 클릭하면 ) READ 상태로 바뀌면서 READ_at 이 now().witnano(0)로 바뀌는 api
}

package com.avengers.yoribogo.notification.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NotificationService {

    // 연결된 클라이언트의 SSEEmitter 목록
    private final List<SseEmitter> emitters = new ArrayList<>();

    // 클라이언트가 연결할 수 있는 SSE 엔드포인트
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);  // 무한 타임아웃 설정
        emitters.add(emitter);

        log.info("emitter1",emitter);
        // 연결 완료, 타임아웃, 에러 발생 시 emitter 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));
        log.info("emitter2",emitter);

        return emitter;
    }

    // 로그인된 사용자들에게 알림을 전송
    public void sendNotificationToLoggedInUsers(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                deadEmitters.add(emitter);  // 전송 실패한 emitter를 제거 목록에 추가
            }
        }
        // 실패한 emitter 제거
        emitters.removeAll(deadEmitters);
    }
}

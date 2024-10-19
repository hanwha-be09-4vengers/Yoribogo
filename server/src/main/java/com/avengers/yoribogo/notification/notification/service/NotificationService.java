package com.avengers.yoribogo.notification.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class NotificationService {

    private final List<SseEmitter> emitters = new ArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((ex) -> emitters.remove(emitter));

        // 연결 즉시 메시지 전송
        try {
            emitter.send(SseEmitter.event().name("connect").data("Connected!"));
            log.info("(Service)SSE 연결 및 메세지 전송 완료!!");
        } catch (IOException e) {
            /*
            Web 새로고침 시 SSE 연결된 객체는 죽고 다시 재연결 되기 때문에
            연결된 객체에게 한 번 메세지를 보내려고 하여
            "현재 연결은 사용자의 호스트 시스템의 소프트웨어의 의해 중단되었습니다"
            문구가 콘솔에 뜹니다.
            */
            emitter.completeWithError(e);

        }

        return emitter;
    }

    // 메시지를 클라이언트로 전송
    public void sendNotification(String data) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(data));
            } catch (IOException e) {
                deadEmitters.add(emitter);  // 전송 실패한 emitter를 제거 목록에 추가
            }
        }

        // 실패한 emitter 제거
        emitters.removeAll(deadEmitters);
    }
}

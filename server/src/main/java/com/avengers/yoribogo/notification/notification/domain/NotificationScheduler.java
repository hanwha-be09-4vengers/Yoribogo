package com.avengers.yoribogo.notification.notification.domain;

import com.avengers.yoribogo.notification.notification.service.NotificationService;
import com.avengers.yoribogo.notification.weeklypopularrecipe.service.WeeklyPopularRecipeService;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipe;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    private final WeeklyPopularRecipeService weeklyPopularRecipeService;
    private final NotificationService notificationService;

    public NotificationScheduler(WeeklyPopularRecipeService weeklyPopularRecipeService, NotificationService notificationService) {
        this.weeklyPopularRecipeService = weeklyPopularRecipeService;
        this.notificationService = notificationService;
    }

    // 30초마다 실행되는 테스트 스케줄러 (매일 11시, 17시에 실행하려면 cron 설정 사용)
    @Scheduled(fixedRate = 30000)
    public void sendMostLikedRecipeNotification() {
        // 좋아요가 가장 많은 레시피 가져오기
        WeeklyPopularRecipe mostLikedRecipe = weeklyPopularRecipeService.getRandomTopLikedRecipe();

        // 알림 전송
        // 알림 보낼 때 알림에 레시피 이름이 들어가야 하므로 받은 레시피 ID 값을 이용 레시피 단건조회 API를 호출하여

        String message = "가장 많은 좋아요를 받은 레시피는: " + mostLikedRecipe.getMyRecipeId();
        notificationService.sendNotificationToLoggedInUsers(message);  // SSE로 클라이언트에 전송
    }
}

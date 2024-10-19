package com.avengers.yoribogo.notification.notification.domain;

import com.avengers.yoribogo.notification.notification.dto.NotificationStatus;
import com.avengers.yoribogo.notification.notification.service.NotificationService;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipe;
import com.avengers.yoribogo.notification.weeklypopularrecipe.service.WeeklyPopularRecipeService;
import com.avengers.yoribogo.notification.notification.dto.NotificationEntity;
import com.avengers.yoribogo.notification.notification.repository.NotificationRepository;
import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.recipeboard.recipeboard.dto.RecipeBoardEntity;
import com.avengers.yoribogo.recipeboard.recipeboard.repository.RecipeBoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class NotificationScheduler {

    private final WeeklyPopularRecipeService weeklyPopularRecipeService;
    private final RecipeBoardRepository recipeBoardRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    public NotificationScheduler(WeeklyPopularRecipeService weeklyPopularRecipeService, RecipeBoardRepository recipeBoardRepository, NotificationRepository notificationRepository, NotificationService notificationService) {
        this.weeklyPopularRecipeService = weeklyPopularRecipeService;
        this.recipeBoardRepository = recipeBoardRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
    }

    // 11시에 실행되는 스케줄러
    @Scheduled(cron = "0 0 11 * * ?")
    public void saveLunchRecipeNotification() {
        notificationService.saveRecipeNotification("lunch");
    }

    // 17시에 실행되는 스케줄러
    @Scheduled(cron = "0 0 17 * * ?")
    public void saveDinnerRecipeNotification() {
        notificationService.saveRecipeNotification("dinner"); 
    }

}

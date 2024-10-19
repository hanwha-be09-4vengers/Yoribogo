package com.avengers.yoribogo.notification.weeklypopularrecipe.service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.InsertLikeToMongoEvent;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipe;
import com.avengers.yoribogo.notification.weeklypopularrecipe.repository.WeeklyPopularRecipeMongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
public class WeeklyPopularRecipeService {

    private final WeeklyPopularRecipeMongoRepository weeklyPopularRecipeMongoRepository;

    public WeeklyPopularRecipeService(WeeklyPopularRecipeMongoRepository weeklyPopularRecipeMongoRepository) {
        this.weeklyPopularRecipeMongoRepository = weeklyPopularRecipeMongoRepository;
    }

    public List<WeeklyPopularRecipe> getAllWeeklyPopularRecipes() {
        List<WeeklyPopularRecipe> recipes = weeklyPopularRecipeMongoRepository.findAll();
        if (recipes.isEmpty()) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE);
        }
        return recipes;
    }

    // mostliked
    public WeeklyPopularRecipe getMostLikedRecipe() {
        WeeklyPopularRecipe recipe = weeklyPopularRecipeMongoRepository.findTopByOrderByLikeIdDesc();    // MongoDB Search
        if (recipe == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_RECIPE);
        }
        return recipe;
    }

    //  mongodb에 insert 하기 위한 메서드 ( 단, 좋아요 트랜젝션 커밋 이후 실행 )
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInsertLikeDataToMongo(InsertLikeToMongoEvent event) {

        WeeklyPopularRecipe newLike = new WeeklyPopularRecipe();

        newLike.setUserId(event.getUserId());
        newLike.setMyRecipeId(event.getPostId());
        newLike.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).withNano(0));  // KST로 현재 시간 저장

        weeklyPopularRecipeMongoRepository.save(newLike);
    }

    public List<WeeklyPopularRecipe> getTop3LikedRecipes() {
        // 일주일 전의 날짜 계산
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);

        // 일주일 이내의 상위 3개 레시피 조회
        return weeklyPopularRecipeMongoRepository.findTop3LikedRecipesWithinLastWeek(oneWeekAgo);
    }

}
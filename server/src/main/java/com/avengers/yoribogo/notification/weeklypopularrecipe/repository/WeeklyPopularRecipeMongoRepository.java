package com.avengers.yoribogo.notification.weeklypopularrecipe.repository;

import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeeklyPopularRecipeMongoRepository extends MongoRepository<WeeklyPopularRecipe, String> {
    // 컬렉션 내의 모든 데이터를 조회하는 메서드
    List<WeeklyPopularRecipe> findAll();

    // likeId 필드를 기준으로 가장 높은 값을 가진 데이터를 하나 반환
    WeeklyPopularRecipe findTopByOrderByLikeIdDesc();
}

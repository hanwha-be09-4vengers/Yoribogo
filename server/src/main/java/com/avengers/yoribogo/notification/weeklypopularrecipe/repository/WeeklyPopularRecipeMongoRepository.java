package com.avengers.yoribogo.notification.weeklypopularrecipe.repository;

import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.WeeklyPopularRecipe;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeeklyPopularRecipeMongoRepository extends MongoRepository<WeeklyPopularRecipe, String> {
    // 컬렉션 내의 모든 데이터를 조회하는 메서드
    List<WeeklyPopularRecipe> findAll();

    // likeId 필드를 기준으로 가장 높은 값을 가진 데이터를 하나 반환
    WeeklyPopularRecipe findTopByOrderByLikeIdDesc();

    // 일주일 이내의 좋아요 데이터를 가져와 게시글별로 그룹핑하고, 좋아요 수를 카운트한 후 상위 3개 게시글 선택
    @Aggregation(pipeline = {
            "{ $match: { 'created_at': { $gte: ?0 } } }",  // 일주일 이내의 데이터를 필터링
            "{ $group: { _id: '$my_recipe_id', count: { $sum: 1 } } }",  // 게시글별로 그룹핑하고 좋아요 수 카운트
            "{ $sort: { count: -1 } }",  // 좋아요 수 기준으로 내림차순 정렬
            "{ $limit: 3 }"  // 상위 3개 선택
    })
    List<WeeklyPopularRecipe> findTop3LikedRecipesWithinLastWeek(LocalDateTime oneWeekAgo);
}

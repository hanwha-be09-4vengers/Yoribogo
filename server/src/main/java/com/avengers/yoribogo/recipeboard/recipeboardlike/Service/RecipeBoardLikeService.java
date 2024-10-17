package com.avengers.yoribogo.recipeboard.recipeboardlike.Service;

import com.avengers.yoribogo.common.exception.CommonException;
import com.avengers.yoribogo.common.exception.ErrorCode;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.DeleteLikeInMongoEvent;
import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.InsertLikeToMongoEvent;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoard;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.RecipeBoardLikeEntity;
import com.avengers.yoribogo.recipeboard.recipeboardlike.Repository.RecipeBoardLikeRepository;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.LikeRequestDTO;

import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.user.domain.UserEntity;
import com.avengers.yoribogo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RecipeBoardLikeService {

    private final UserRepository userRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeBoardLikeRepository recipeBoardLikeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public RecipeBoardLikeService(UserRepository userRepository, RecipeBoardRepository recipeBoardRepository, RecipeBoardLikeRepository recipeBoardLikeRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.userRepository = userRepository;
        this.recipeBoardRepository = recipeBoardRepository;
        this.recipeBoardLikeRepository = recipeBoardLikeRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void likePost(LikeRequestDTO likeRequestDTO) {
        Long userId = likeRequestDTO.getUserId();
        Long postId = likeRequestDTO.getPostId();

        // 1. 이미 해당 유저가 해당 게시글에 좋아요를 눌렀는지 확인
        RecipeBoardLikeEntity existingLike = recipeBoardLikeRepository.findByUserUserIdAndRecipeBoardRecipeBoardId(userId, postId).orElse(null);

        // 2. 유저와 게시글 정보 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
        RecipeBoard recipeBoard = recipeBoardRepository.findById(postId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_RECIPE));

        long postAuthorId = recipeBoard.getUserId();  // 게시글 작성자
        UserEntity postAuthor = new UserEntity();

        if (existingLike != null) {
            // 3. 좋아요가 이미 눌러져 있는 경우 -> 좋아요 취소 로직
            recipeBoardLikeRepository.delete(existingLike);
            recipeBoard.setRecipeBoardLikes(recipeBoard.getRecipeBoardLikes() - 1);
            postAuthor.setUserLikes(postAuthor.getUserLikes() - 1);


        } else {
            // 4. 좋아요가 눌러져 있지 않은 경우 -> 좋아요 추가 로직
            recipeBoard.setRecipeBoardLikes(recipeBoard.getRecipeBoardLikes() + 1);  // 게시글의 좋아요 수 +1

            postAuthor.setUserId(postAuthorId);
            postAuthor.setUserLikes(postAuthor.getUserLikes() + 1);  // 게시글 작성자의 좋아요 수 +1

            RecipeBoardLikeEntity newLike = new RecipeBoardLikeEntity();
            newLike.setUser(user);
            newLike.setRecipeBoard(existingLike.getRecipeBoard());
            newLike.setLikeCreatedAt(LocalDateTime.now());
            recipeBoardLikeRepository.save(newLike);  // 좋아요 엔티티 저장
        }

        // 5. 변경된 게시글, 유저 정보 저장
        recipeBoardRepository.save(recipeBoard);
        userRepository.save(postAuthor);

        // 6. 좋아요 추가 시에만 MongoDB 비동기 이벤트 처리
        if (existingLike == null) {
            applicationEventPublisher.publishEvent(new InsertLikeToMongoEvent(userId.toString(), postId.toString()));
        } else {
            applicationEventPublisher.publishEvent((new DeleteLikeInMongoEvent(userId.toString(), postId.toString())));
        }
    }
}

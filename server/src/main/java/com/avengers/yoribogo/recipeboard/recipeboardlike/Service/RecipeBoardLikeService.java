package com.avengers.yoribogo.recipeboard.recipeboardlike.Service;

import com.avengers.yoribogo.notification.weeklypopularrecipe.dto.InsertLikeToMongoEvent;
import com.avengers.yoribogo.recipeboard.recipeboard.dto.RecipeBoardEntity;
import com.avengers.yoribogo.recipeboard.recipeboard.repository.RecipeBoardRepository;
import com.avengers.yoribogo.recipeboard.recipeboardlike.Repository.RecipeBoardLikeRepository;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.LikeRequestDTO;
import com.avengers.yoribogo.recipeboard.recipeboardlike.dto.RecipeBoardLikeEntity;
import com.avengers.yoribogo.user.dto.UserEntity;
import com.avengers.yoribogo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RecipeBoardLikeService {

    @Autowired
    private final UserRepository userRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final RecipeBoardLikeRepository recipeBoardLikeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;      // Mongodb에 비동기 이벤트 처리를 위해 추가


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

        System.out.println("Received userId: " + userId);
        // 1. 이미 해당 유저가 해당 게시글에 좋아요를 눌렀는지 확인
        recipeBoardLikeRepository.findByUserUserIdAndRecipeBoardRecipeBoardId(userId, postId)
                .ifPresent(like -> {
                    // ErrorCode 만들어서 수정하기
                    throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
                });
        System.out.println("Received userId: " + userId);

        // 2. 유저와 게시글 정보 조회 (fetch join 활용)
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        RecipeBoardEntity recipeBoard = recipeBoardRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        // 3. 좋아요 엔티티 생성 및 저장
        RecipeBoardLikeEntity newLike = new RecipeBoardLikeEntity();
        newLike.setUser(user);
        newLike.setRecipeBoard(recipeBoard);
        newLike.setLikeCreatedAt(LocalDateTime.now());
        recipeBoardLikeRepository.save(newLike);

        // 4. 게시글의 좋아요 수 +1
        recipeBoard.setRecipeBoardLikes(recipeBoard.getRecipeBoardLikes() + 1);

        // 5. 게시글 작성자의 좋아요 수 +1
        UserEntity postAuthor = recipeBoard.getUser();  // 게시글 작성자
        postAuthor.setUserLikes(postAuthor.getUserLikes() + 1);

        // 6. 변경된 게시글, 유저 정보 저장
        recipeBoardRepository.save(recipeBoard);
        userRepository.save(postAuthor);

        // 7. Mongodb 비동기 처리를 위해 해당 트랜젝션 종료 이후 이벤트 호출
        applicationEventPublisher.publishEvent(new InsertLikeToMongoEvent(userId.toString(), postId.toString()));
    }
}


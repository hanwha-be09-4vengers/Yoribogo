package com.avengers.yoribogo.recipeboard.service;

import com.avengers.yoribogo.recipeboard.domain.RecipeBoardComment;
import com.avengers.yoribogo.recipeboard.domain.RecipeBoardCommentStatus;
import com.avengers.yoribogo.recipeboard.dto.RecipeBoardCommentDTO;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardCommentRepository;
import com.avengers.yoribogo.recipeboard.repository.RecipeBoardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeBoardCommentServiceImpl implements RecipeBoardCommentService {
    private final RecipeBoardCommentRepository commentRepository;
    private final RecipeBoardRepository recipeBoardRepository;
    private final ModelMapper modelMapper;

    public RecipeBoardCommentServiceImpl(RecipeBoardCommentRepository commentRepository,
                                         RecipeBoardRepository recipeBoardRepository,
                                         ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.recipeBoardRepository = recipeBoardRepository;
        this.modelMapper = modelMapper;
    }

    /* 댓글 등록 */
    @Override
    @Transactional
    public RecipeBoardCommentDTO createComment(RecipeBoardCommentDTO commentDTO) {
        RecipeBoardComment comment = modelMapper.map(commentDTO, RecipeBoardComment.class);

        // 기본 status -> ACTIVE 설정
        comment.setRecipeBoardCommentStatus(RecipeBoardCommentStatus.ACTIVE);
        // 시간 설정
        comment.setRecipeBoardCommentCreatedAt(LocalDateTime.now());


        if (comment.getRecipeBoardCommentContent() == null || comment.getRecipeBoardCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException("댓글을 입력해주세요.");
        }

        try {
            RecipeBoardComment createdComment = commentRepository.save(comment);
            return modelMapper.map(createdComment, RecipeBoardCommentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("댓글 등록에 실패하였습니다. " + e);
        }
    }



    /* 댓글 수정 */
    @Override
    @Transactional
    public RecipeBoardCommentDTO modifyComment(Long id, RecipeBoardCommentDTO commentDTO) {
        // 수정할 내용이 비어 있는지 체크
        if (commentDTO.getRecipeBoardCommentContent() == null || commentDTO.getRecipeBoardCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException("수정할 댓글 내용이 비어있습니다.");
        }

        RecipeBoardComment existingComment = commentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        // DTO의 내용을 엔티티에 매핑
        modelMapper.map(commentDTO, existingComment);

        try {
            RecipeBoardComment updatedComment = commentRepository.save(existingComment);
            // 수정된 엔티티를 DTO로 변환하여 반환
            return modelMapper.map(updatedComment, RecipeBoardCommentDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("댓글 수정에 실패했습니다." + e);
        }
    }

    /* 댓글 삭제 */
    @Override
    @Transactional
    public void deleteComment(Long id) {


        try {
            commentRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("댓글을 찾을 수 없습니다. " + id);
        } catch (Exception e) {
            throw new RuntimeException("댓글 삭제에 실패하였습니다." + id, e);
        }
    }

    /* 댓글 게시글별 조회 */
    @Override
    public List<RecipeBoardCommentDTO> getCommentsByRecipeBoardId(Long recipeId) {
        if (!recipeBoardRepository.existsById(recipeId)) {
            throw new EntityNotFoundException("해당 게시글이 존재하지 않습니다" + recipeId);
        }

        List<RecipeBoardComment> comments = commentRepository.findAllByRecipeBoardId(recipeId);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("게시글에 댓글이 존재하지 않습니다." + recipeId);
        }

        // 엔티티 리스트를 DTO 리스트로 변환
        List<RecipeBoardCommentDTO> commentDTO = comments.stream()
                .map(comment -> modelMapper.map(comment, RecipeBoardCommentDTO.class))
                .collect(Collectors.toList());

        return commentDTO;
    }


    /* 댓글 회원별 조회 */
    @Override
    public List<RecipeBoardCommentDTO> getCommentsByUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId 가 null 이므로 예외 발생");
        }

        List<RecipeBoardComment> comments = commentRepository.findAllByUserId(userId);
        if (comments.isEmpty()) {
            throw new EntityNotFoundException("해당 유저의 댓글이 존재하지 않습니다." + userId);
        }

        List<RecipeBoardCommentDTO> commentDTO = comments.stream()
                .map(comment -> modelMapper.map(comment, RecipeBoardCommentDTO.class))
                .collect(Collectors.toList());

        return commentDTO;
    }
}

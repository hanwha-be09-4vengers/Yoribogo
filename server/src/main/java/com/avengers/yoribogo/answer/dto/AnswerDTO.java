package com.avengers.yoribogo.answer.dto;

import com.avengers.yoribogo.common.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnswerDTO {
    private int answerId;
    private String answerContent;
    private Role writerType;
    private LocalDateTime answerCreatedAt;
    private int userId;
    private int inquiryId;

    public AnswerDTO(String answerContent, Role writerType, int userId, int inquiryId) {
        this.answerContent = answerContent;
        this.writerType = writerType;
        this.userId = userId;
        this.inquiryId = inquiryId;
    }
}

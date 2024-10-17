package com.avengers.yoribogo.answer.domain;

import com.avengers.yoribogo.common.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerId;

    @Column(name = "answer_content")
    private String answerContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "writer_type")
    private Role writerType;

    @Column(name = "answer_created_at")
    private LocalDateTime answerCreatedAt;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "inquiry_id")
    private int inquiryId;

    public Answer(String answerContent, Role writerType, int userId, int inquiryId) {
        this.answerContent = answerContent;
        this.writerType = writerType;
        this.userId = userId;
        this.inquiryId = inquiryId;
    }
}

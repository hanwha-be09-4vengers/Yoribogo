package com.avengers.yoribogo.inquiry.domain;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.common.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "inquiry")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inquiryId;

    @Column(name = "inquiry_title")
    private String inquiryTitle;

    @Column(name = "inquiry_content")
    private String inquiryContent;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status")
    private Status inquiryStatus;

    @Column(name = "inquiry_created_at")
    private LocalDateTime inquiryCreatedAt;

    @Column(name = "answers")
    private Integer answers;

    @Column(name = "user_id")
    private int userId;

    @OneToMany(mappedBy = "inquiryId")
    private List<Answer> answer;

    public Inquiry(String inquiryTitle, String inquiryContent, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.userId = userId;
    }


}

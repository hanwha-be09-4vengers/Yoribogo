package com.avengers.yoribogo.inquiry.dto;

import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InquiryOnlyDTO {
    private Integer inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private Status inquiryStatus;
    private Visibility inquiryVisibility;
    private LocalDateTime inquiryCreatedAt;
    private Integer answers;
    private Status answerStatus;
    private int userId;

    public InquiryOnlyDTO(String inquiryTitle, String inquiryContent, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.userId = userId;
    }

    public InquiryOnlyDTO(String inquiryTitle, String inquiryContent, Visibility inquiryVisibility, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryVisibility = inquiryVisibility;
        this.userId = userId;
    }
}

package com.avengers.yoribogo.inquiry.dto;

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
    private String inquiryStatus;
    private LocalDateTime inquiryCreatedAt;
    private Integer answers;
    private int userId;

    public InquiryOnlyDTO(String inquiryTitle, String inquiryContent, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.userId = userId;
    }

    public InquiryOnlyDTO(String inquiryTitle, String inquiryContent, Integer answers, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.answers = answers;
        this.userId = userId;
    }
}

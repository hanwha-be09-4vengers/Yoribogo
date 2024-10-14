package com.avengers.yoribogo.inquiry.dto;

import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InquiryDTO {
    private Integer inquiryId;
    private String inquiryTitle;
    private String inquiryContent;
    private Status inquiryStatus;
    private LocalDateTime inquiryCreatedAt;
    private Integer answers;
    private int userId;
    private List<Inquiry> answer;

    public InquiryDTO(String inquiryTitle, String inquiryContent, int userId) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.userId = userId;
    }
}

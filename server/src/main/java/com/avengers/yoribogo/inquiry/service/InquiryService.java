package com.avengers.yoribogo.inquiry.service;

import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.inquiry.dto.InquiryDTO;
import com.avengers.yoribogo.inquiry.dto.InquiryOnlyDTO;

import java.util.List;

public interface InquiryService {

    // 전체 문의 or 회원의 문의 조회
    List<InquiryOnlyDTO> findInquiryOnly(Integer userId);

    // 전체 문의(+답변) or 회원의 문의(+답변) 조회
    List<Inquiry> findInquiry(Integer userId);

    // 문의 번호로 조회
    Inquiry findInquiryById(Integer id);

    // 문의 생성
    Inquiry insertInquiry(InquiryDTO newInquiry);

    // 문의 수정
    Inquiry updateInquiry(InquiryDTO modifyInquiry);

    // 문의 삭제(상태 변경: 'ACTIVE' -> 'INACTIVE')
    boolean removeInquiry(int id);
}

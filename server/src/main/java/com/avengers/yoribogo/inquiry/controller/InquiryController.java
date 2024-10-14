package com.avengers.yoribogo.inquiry.controller;

import com.avengers.yoribogo.common.ResponseDTO;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.inquiry.dto.InquiryDTO;
import com.avengers.yoribogo.inquiry.dto.InquiryOnlyDTO;
import com.avengers.yoribogo.inquiry.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @Autowired
    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    // '/get' == 전체 문의; '/get?id=5' == 회원(user_id=5)의 문의
    @GetMapping("/get-only")
    public ResponseDTO getInquiryOnly(@RequestParam(required = false, name = "id") Integer userId) {
        List<InquiryOnlyDTO> result = inquiryService.findInquiryOnly(userId);
        return ResponseDTO.ok(result);
    }

    // '/get' == 전체 문의(+답변); '/get?id=5' == 회원(user_id=5)의 문의(+답변)
    @GetMapping("/get")
    public ResponseDTO getInquiry(@RequestParam(required = false, value = "id") Integer userId) {
        List<Inquiry> result = inquiryService.findInquiry(userId);
        return ResponseDTO.ok(result);
    }

    // 문의 생성
    @PostMapping("/add")
    public ResponseDTO addInquiry(@RequestBody InquiryDTO newInquiry) {
        Inquiry result = inquiryService.insertInquiry(newInquiry);
        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("문의 생성 실패");
    }

    // 문의 수정
    @PostMapping("/modify")
    public ResponseDTO modifyInquiry(@RequestBody InquiryDTO modifyInquiry) {
        Inquiry result = inquiryService.updateInquiry(modifyInquiry);
        return result != null ? ResponseDTO.ok(result) : ResponseDTO.ok("문의 수정 실패");
    }

    // '/delete?code=5' == 문의(inquiry_id=5) 삭제(상태 변경: 'ACTIVE' -> 'INACTIVE')
    @DeleteMapping("/delete")
    public ResponseDTO deleteInquiry(@RequestParam int id) {
        boolean result = inquiryService.removeInquiry(id);
        return result ? ResponseDTO.ok("문의 삭제 성공") : ResponseDTO.ok("문의 삭제 실패");
    }
}

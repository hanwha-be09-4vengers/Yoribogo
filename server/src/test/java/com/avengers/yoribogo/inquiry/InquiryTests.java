package com.avengers.yoribogo.inquiry;

import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.common.Visibility;
import com.avengers.yoribogo.inquiry.domain.Inquiry;
import com.avengers.yoribogo.inquiry.dto.InquiryDTO;
import com.avengers.yoribogo.inquiry.dto.InquiryOnlyDTO;
import com.avengers.yoribogo.inquiry.service.InquiryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class InquiryTests {

    @Autowired
    private InquiryService inquiryService;

    @DisplayName("전체 (문의중인 상태)문의 목록 조회 확인 테스트")
    @Test
    public void testGetInquiryOnly() {
        List<InquiryOnlyDTO> result = inquiryService.findInquiryOnly(null, "pending");
        Assertions.assertEquals(result.get(0).getAnswerStatus(), Status.PENDING);
    }

    @DisplayName("회원의 (문의중인 상태)문의 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    public void testGetInquiryOnly2(int userId) {
        List<InquiryOnlyDTO> result = inquiryService.findInquiryOnly(userId, "pending");
        Assertions.assertEquals(result.get(0).getAnswerStatus(), Status.PENDING);
    }

    @DisplayName("전체 문의(+답변) 목록 조회 확인 테스트")
    @Test
    public void testGetInquiry() {
        List<Inquiry> result = inquiryService.findInquiry(null, null);
        Assertions.assertNotNull(result);
    }

    @DisplayName("회원의 문의(+답변) 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {3, 4, 5})
    public void testGetInquiry2(int userId) {
        List<Inquiry> result = inquiryService.findInquiry(userId, null);
        Assertions.assertNotNull(result);
    }

    @DisplayName("문의 생성 확인 테스트")
    @Test
    public void testAddInquiry() {
        Inquiry result = inquiryService.insertInquiry(
                new InquiryDTO("문의 생성 테스트", "문의 생성 테스트 내용", 3));
        Assertions.assertTrue(result.getInquiryId() != 0);
    }

    @DisplayName("문의 수정 확인 테스트")
    @Test
    public void testModifyInquiry() {
        Inquiry result = inquiryService.updateInquiry(
                new InquiryDTO(4,
                        "문의 수정 테스트",
                        "문의 수정 테스트 내용",
                        Status.ACTIVE,
                        Visibility.PUBLIC,
                        LocalDateTime.now().withNano(0),
                        0,
                        Status.PENDING,
                        3));
        Assertions.assertNotNull(result);
    }

    @DisplayName("문의 삭제 확인 테스트")
    @Test
    public void testDeleteInquiry() {
        Inquiry result = inquiryService.removeInquiry(4);
        Assertions.assertTrue(result.getInquiryStatus() == Status.INACTIVE);
    }
}

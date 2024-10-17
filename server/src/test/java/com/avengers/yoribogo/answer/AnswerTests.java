package com.avengers.yoribogo.answer;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;
import com.avengers.yoribogo.answer.service.AnswerService;
import com.avengers.yoribogo.common.Role;
import com.avengers.yoribogo.common.Status;
import com.avengers.yoribogo.inquiry.service.InquiryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AnswerTests {

    private AnswerService answerService;
    private InquiryService inquiryService;

    @Autowired
    public AnswerTests(AnswerService answerService, InquiryService inquiryService) {
        this.answerService = answerService;
        this.inquiryService = inquiryService;
    }

    @DisplayName("문의의 답변 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testGetAnswer(int inquiryId) {
        List<Answer> result = answerService.findAnswer(inquiryId);
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변 생성 확인 테스트")
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"ADMIN", "ENTERPRISE"})
    public void testAddAnswer(Role writer) {
        int userId = 0;

        if (writer == Role.ADMIN) userId = 1;
        else userId = 5;

        Answer result = answerService.insertAnswer(
                new AnswerDTO("답변 생성 테스트", writer, userId, 1));

        Status status = inquiryService.findInquiryById(1).getAnswerStatus();

        if (writer == Role.ADMIN) Assertions.assertTrue(status == Status.ANSWERED);
        else Assertions.assertTrue(status == Status.PENDING);
    }

    @DisplayName("답변 삭제 확인 테스트")
    @Test
    public void testDeleteAnswer() {
        boolean result = answerService.removeAnswer(3);
        Assertions.assertTrue(result);
    }
}

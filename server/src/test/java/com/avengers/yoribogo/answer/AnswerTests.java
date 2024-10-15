package com.avengers.yoribogo.answer;

import com.avengers.yoribogo.answer.domain.Answer;
import com.avengers.yoribogo.answer.dto.AnswerDTO;
import com.avengers.yoribogo.answer.service.AnswerService;
import com.avengers.yoribogo.common.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
public class AnswerTests {

    @Autowired
    private AnswerService answerService;

    @DisplayName("문의의 답변 목록 조회 확인 테스트")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testGetAnswer(int inquiryId) {
        List<Answer> result = answerService.findAnswer(inquiryId);
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변 생성 확인 테스트")
    @Test
    public void testAddAnswer() {
        Answer result = answerService.insertAnswer(
                new AnswerDTO("답변 생성 테스트", Role.ADMIN, 1, 1));
        log.info("result: {}", result);
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변(재문의) 생성 확인 테스트")
    @Test
    public void testAddAnswer2() {
        Answer result = answerService.insertAnswer(
                new AnswerDTO("답변(재문의) 생성 테스트", Role.ENTERPRISE, 3, 1));
        log.info("result: {}", result);
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변 수정 확인 테스트")
    @Test
    public void testModifyAnswer() {
        Answer result = answerService.updateAnswer(
                new AnswerDTO(13, "답변 수정 테스트", Role.ENTERPRISE, LocalDateTime.now(), 1, 1));
        Assertions.assertNotNull(result);
    }

    @DisplayName("답변 삭제 확인 테스트")
    @Test
    public void testDeleteAnswer() {
        boolean result = answerService.removeAnswer(13);
        Assertions.assertTrue(result);
    }
}

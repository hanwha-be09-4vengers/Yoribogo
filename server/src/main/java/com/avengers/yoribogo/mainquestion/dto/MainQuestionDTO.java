package com.avengers.yoribogo.mainquestion.dto;

import com.avengers.yoribogo.choice.domain.Choice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MainQuestionDTO {
    private Integer mainQuestionId;
    private String mainQuestionContent;
    private int userId;
    private List<Choice> choice;

    public MainQuestionDTO(String mainQuestionContent, int userId) {
        this.mainQuestionContent = mainQuestionContent;
        this.userId = userId;
    }

    public MainQuestionDTO(String mainQuestionContent, int userId, List<Choice> choice) {
        this.mainQuestionContent = mainQuestionContent;
        this.userId = userId;
        this.choice = choice;
    }
}

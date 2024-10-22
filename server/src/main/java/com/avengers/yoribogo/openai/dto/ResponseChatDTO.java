package com.avengers.yoribogo.openai.dto;

import com.avengers.yoribogo.openai.aggregate.Choice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatDTO implements Serializable {
    private List<Choice> choices;
}

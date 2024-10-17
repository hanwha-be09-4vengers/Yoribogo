package com.avengers.yoribogo.recipeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoardManualDTO {

    @JsonProperty("manual_step")
    private int recipeBoardManualStep;

    @JsonProperty("manual_image")
    private String recipeBoardManualImage;

    @JsonProperty("manual_content")
    private String recipeBoardManualContent;
}

package com.avengers.yoribogo.recipeboard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "RECIPE_BOARD_MANUAL")
@Data
public class RecipeBoardManual {

    @Id
    @Column(name = "RECIPE_BOARD_MANUAL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeBoardManualId;

    @Column(name = "RECIPE_BOARD_MANUAL_STEP")
    private int recipeBoardManualStep;

    @Column(name = "RECIPE_BOARD_MANUAL_IMAGE")
    private String recipeBoardManualImage;

    @Column(name = "RECIPE_BOARD_MANUAL_CONTENT")
    private String recipeBoardManualContent;

    // ManyToOne 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECIPE_BOARD_ID", insertable = false, updatable = false)
    private RecipeBoard recipeBoard;

    @Column(name = "RECIPE_BOARD_ID")
    private long recipeBoardId;

}

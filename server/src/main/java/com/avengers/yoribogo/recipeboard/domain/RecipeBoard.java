package com.avengers.yoribogo.RecipeBoard.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecipeBoard {

    @Id
    @Column(name = "RECIPE_BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long recipeBoardId;

    @Column(name = "RECIPE_BOARD_MENU_NAME")
    private String recipeBoardMenuName;

    @Column(name = "RECIPE_BOARD_INGREDIENT")
    private String recipeBoardIngredient;

    @Column(name = "RECIPE_BOARD_IMAGE")
    private String recipeBoardImage;

    @Column(name = "RECIPE_BOARD_LIKES")
    private int recipeBoardLikes;

    @Column(name = "RECIPE_BOARD_COMMENTS")
    private String recipeBoardComments;

    @Column(name = "RECIPE_BOARD_CREATED_AT")
    private LocalDateTime recipeBoardCreatedAt;

    @Column(name ="RECIPE_BOARD_STATUS")
    @Enumerated(EnumType.STRING)
    private RecipeBoardStatus recipeBoardStatus;

    @Column(name = "USER_ID")
    private long userId;
}

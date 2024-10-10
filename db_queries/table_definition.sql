-- 외래 키 제약을 비활성화
SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist
DROP TABLE IF EXISTS recipe_board_manual;
DROP TABLE IF EXISTS recipe_manual;
DROP TABLE IF EXISTS public_data_recipe;
DROP TABLE IF EXISTS ai_recipe;
DROP TABLE IF EXISTS recipe_board_favorite;
DROP TABLE IF EXISTS recipe_board_like;
DROP TABLE IF EXISTS recommended_menu;
DROP TABLE IF EXISTS recipe_board_recomment;
DROP TABLE IF EXISTS recipe_board_comment;
DROP TABLE IF EXISTS recipe_board;
DROP TABLE IF EXISTS recipe;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS inquiry;
DROP TABLE IF EXISTS choice;
DROP TABLE IF EXISTS main_question;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS tier;

-- 외래 키 제약을 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

-- Table creation

CREATE TABLE tier (
    tier_id INT PRIMARY KEY,
    tier_name VARCHAR(255) NOT NULL,
    tier_criteria BIGINT
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='티어' DEFAULT CHARSET=UTF8;

CREATE TABLE user (
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    user_auth_id VARCHAR(255) NOT NULL, -- 일반 로그인 ID 또는 소셜 로그인 고유번호
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    email VARCHAR(255),
    user_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(user_status IN ('ACTIVE','INACTIVE')),
    created_at TIMESTAMP,
    withdrawn_at TIMESTAMP,
    profile_image TEXT,
    signup_path VARCHAR(255) CHECK (signup_path IN ('KAKAO','NAVER','ADMIN','NORMAL')),  -- 가입 경로는 KAKAO,NAVER,ADMIN,NORMAL
    user_role VARCHAR(255) CHECK (user_role IN ('ADMIN','ENTERPRISE')),  -- 가입 경로는 KAKAO,NAVER,ADMIN,NORMAL
    user_identifier VARCHAR(511) NOT NULL,  -- 신규 추가, 가입 경로 + ID(or 고유번호)
    user_likes BIGINT,
    tier_id INT,
    accept_status VARCHAR(255) NOT NULL DEFAULT 'Y' CHECK(accept_status IN ('Y','N')),
    PRIMARY KEY (user_id),
    FOREIGN KEY (tier_id) REFERENCES tier(tier_id),
    UNIQUE KEY unique_user_identifier (user_identifier)  -- 유니크 인덱스 추가
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='회원' DEFAULT CHARSET=UTF8;

CREATE TABLE main_question (
    main_question_id INT PRIMARY KEY,
    main_question_content VARCHAR(255) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='메인질문' DEFAULT CHARSET=UTF8;

CREATE TABLE choice (
    choice_id INT PRIMARY KEY,
    choice_image TEXT,
    choice_content VARCHAR(255) NOT NULL,
    main_question_id INT,
    FOREIGN KEY (main_question_id) REFERENCES main_question(main_question_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='선지' DEFAULT CHARSET=UTF8;

CREATE TABLE inquiry (
    inquiry_id BIGINT PRIMARY KEY,
    user_id BIGINT,
    inquiry_type VARCHAR(255),
    inquiry_content TEXT,
    inquiry_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(inquiry_status IN ('ACTIVE','INACTIVE')),
    inquiry_created_at TIMESTAMP,
    answers BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='문의' DEFAULT CHARSET=UTF8;

CREATE TABLE notification (
    notification_id BIGINT PRIMARY KEY,
    notification_read_status VARCHAR(255) NOT NULL DEFAULT 'WAIT' CHECK(notification_read_status IN ('READ', 'WAIT')),
    notification_created_at TIMESTAMP,
    notification_content TEXT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='알림' DEFAULT CHARSET=UTF8;

CREATE TABLE answer (
    answer_id BIGINT PRIMARY KEY,
    user_id BIGINT,
    answer_type VARCHAR(255),
    answer_content TEXT,
    answer_classification VARCHAR(255) NOT NULL DEFAULT 'MEMBER' CHECK(answer_classification IN ('ENTERISE','ADMIN')),
    inquiry_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (inquiry_id) REFERENCES inquiry(inquiry_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='답변' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe (
    recipe_id BIGINT PRIMARY KEY,
    menu_name VARCHAR(255),
    menu_ingredient TEXT,
    menu_image TEXT,
    menu_type VARCHAR(255),
    recipe_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_status IN ('ACTIVE','INACTIVE')),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='요리레시피' DEFAULT CHARSET=UTF8;


CREATE TABLE recipe_board (
    recipe_board_id BIGINT PRIMARY KEY,
    recipe_board_menu_name VARCHAR(255),
    recipe_board_ingredient TEXT,
    recipe_board_image TEXT,
    recipe_board_likes BIGINT,
    recipe_board_comments BIGINT,
    recipe_board_created_at TIMESTAMP,
    recipe_board_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_status IN ('ACTIVE','INACTIVE')),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_comment (
    recipe_board_comment_id BIGINT PRIMARY KEY,
    recipe_board_id BIGINT,
    recipe_board_comment_content TEXT,
    recipe_board_comment_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_comment_status IN ('ACTIVE','INACTIVE')),
    user_id BIGINT,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피댓글' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_recomment (
    recipe_board_recomment_id BIGINT PRIMARY KEY,
    recipe_board_comment_id BIGINT,
    recipe_board_recomment_content TEXT,
    recipe_board_recomment_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_recomment_status IN ('ACTIVE','INACTIVE')),
    user_id BIGINT,
    FOREIGN KEY (recipe_board_comment_id) REFERENCES recipe_board_comment(recipe_board_comment_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피대댓글' DEFAULT CHARSET=UTF8;

CREATE TABLE recommended_menu (
    recommended_menu_id BIGINT PRIMARY KEY,
    satisfaction VARCHAR(255) NOT NULL CHECK(satisfaction IN ('GOOD','BAD')),
    user_id BIGINT,
    recipe_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='추천요리' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_like (
    recipe_board_like_id BIGINT PRIMARY KEY,
    like_created_at TIMESTAMP,
    user_id BIGINT,
    recipe_board_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피좋아요' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_favorite (
    recipe_board_favorite_id BIGINT PRIMARY KEY,
    recipe_board_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피즐겨찾기' DEFAULT CHARSET=UTF8;

CREATE TABLE ai_recipe (
    ai_recipe_id BIGINT PRIMARY KEY,
    ai_menu_name VARCHAR(255),
    ai_menu_ingredient TEXT,
    recipe_id BIGINT,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='AI요리레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE public_data_recipe (
    public_data_recipe_id BIGINT PRIMARY KEY,
    public_data_menu_name VARCHAR(255),
    public_data_menu_ingredient TEXT,
    public_data_menu_image TEXT,
    recipe_id BIGINT,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='공공데이터요리레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_manual (
    recipe_manual_id BIGINT PRIMARY KEY,
    manual_menu_image TEXT,
    manual_content TEXT,
    recipe_id BIGINT,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='요리메뉴얼' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_manual (
    recipe_board_manual_id BIGINT PRIMARY KEY,
    recipe_board_manual_image TEXT,
    recipe_board_manual_content TEXT,
    recipe_board_id BIGINT,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피메뉴얼' DEFAULT CHARSET=UTF8;

-- 테이블 생성 후 확인
SHOW TABLES;

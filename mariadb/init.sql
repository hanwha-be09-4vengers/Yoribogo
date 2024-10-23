CREATE DATABASE IF NOT EXISTS yoribogodb;

CREATE USER IF NOT EXISTS 'yoribogo'@'%' IDENTIFIED BY 'yoribogo'; 
ALTER USER 'yoribogo'@'%' IDENTIFIED BY 'yoribogo';
FLUSH PRIVILEGES;

GRANT ALL PRIVILEGES ON yoribogodb.* TO 'yoribogo'@'%';
FLUSH PRIVILEGES;

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


-- Table creation

CREATE TABLE tier (
    tier_id INT PRIMARY KEY AUTO_INCREMENT,
    tier_name VARCHAR(255) NOT NULL,
    tier_criteria BIGINT NOT NULL,
    tier_image TEXT NOT NULL
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='티어' DEFAULT CHARSET=UTF8;

CREATE TABLE user (
    user_id BIGINT(20) NOT NULL AUTO_INCREMENT,
    user_auth_id VARCHAR(255) NOT NULL, -- 일반 로그인 ID 또는 소셜 로그인 고유번호
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    nickname VARCHAR(255),   
    email VARCHAR(255) NOT NULL,
    user_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(user_status IN ('ACTIVE','INACTIVE')),
    created_at TIMESTAMP NOT NULL,
    withdrawn_at TIMESTAMP,
    profile_image TEXT,
    signup_path VARCHAR(255) NOT NULL CHECK (signup_path IN ('KAKAO','NAVER','ADMIN','NORMAL')),  -- 가입 경로는 KAKAO,NAVER,ADMIN,NORMAL
    user_role VARCHAR(255) NOT NULL CHECK (user_role IN ('ADMIN','ENTERPRISE')),  -- 가입 경로는 KAKAO,NAVER,ADMIN,NORMAL
    user_identifier VARCHAR(511) NOT NULL,  -- 신규 추가, 가입 경로 + ID(or 고유번호)
    user_likes BIGINT DEFAULT 0,
    tier_id INT NULL,
    accept_status VARCHAR(255) NOT NULL DEFAULT 'N' CHECK(accept_status IN ('Y','N')),
    PRIMARY KEY (user_id),
    FOREIGN KEY (tier_id) REFERENCES tier(tier_id),
    UNIQUE KEY unique_user_identifier (user_identifier)  -- 유니크 인덱스 추가
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='회원' DEFAULT CHARSET=UTF8;

CREATE TABLE main_question (
    main_question_id INT PRIMARY KEY AUTO_INCREMENT,
    main_question_content VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='메인질문' DEFAULT CHARSET=UTF8;

CREATE TABLE choice (
    choice_id INT PRIMARY KEY AUTO_INCREMENT,
    choice_image TEXT NOT NULL,
    choice_content VARCHAR(255) NOT NULL,
    main_question_id INT NOT NULL,
    FOREIGN KEY (main_question_id) REFERENCES main_question(main_question_id) ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='선지' DEFAULT CHARSET=UTF8;

CREATE TABLE inquiry (
    inquiry_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inquiry_title VARCHAR(255) NOT NULL,
    inquiry_content TEXT NOT NULL,
    inquiry_status VARCHAR(255) DEFAULT 'ACTIVE' CHECK(inquiry_status IN ('ACTIVE','INACTIVE')),
    inquiry_visibility VARCHAR(255) DEFAULT 'PUBLIC' CHECK(inquiry_visibility IN ('PUBLIC','PRIVATE')), -- 공개 여부
    inquiry_created_at TIMESTAMP NOT NULL,
    answers BIGINT DEFAULT 0,
    answer_status VARCHAR(255) DEFAULT 'PENDING' CHECK(answer_status IN ('PENDING', 'ANSWERED')), -- 답변 상태
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='문의' DEFAULT CHARSET=UTF8;

CREATE TABLE answer (
    answer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    answer_content TEXT NOT NULL,
    writer_type VARCHAR(255) NOT NULL DEFAULT 'ENTERPRISE' CHECK(writer_type IN ('ENTERPRISE','ADMIN')),
    answer_created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    inquiry_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (inquiry_id) REFERENCES inquiry(inquiry_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='답변' DEFAULT CHARSET=UTF8;


CREATE TABLE notification (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    notification_read_status VARCHAR(255) NOT NULL DEFAULT 'WAIT' CHECK(notification_read_status IN ('READ', 'UNREAD', 'DELETED')),
    notification_created_at TIMESTAMP NOT NULL,
    notification_read_at TIMESTAMP,
    notification_content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='알림' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe (
    recipe_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    menu_name VARCHAR(255) NOT NULL,
    menu_ingredient TEXT NOT NULL,
    menu_image TEXT,
    menu_type VARCHAR(255) NOT NULL DEFAULT 'PUBLIC' CHECK(menu_type IN ('PUBLIC', 'AI')),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='요리레시피' DEFAULT CHARSET=UTF8;


CREATE TABLE recipe_board (
    recipe_board_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_board_menu_name VARCHAR(255) NOT NULL,
    recipe_board_ingredient TEXT NOT NULL,
    recipe_board_image TEXT,
    recipe_board_likes BIGINT NOT NULL DEFAULT 0,
    recipe_board_comments BIGINT NOT NULL DEFAULT 0,
    recipe_board_created_at TIMESTAMP NOT NULL,
    recipe_board_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_status IN ('ACTIVE','INACTIVE')),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_comment (
    recipe_board_comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_board_comment_content TEXT NOT NULL,
    recipe_board_comment_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_comment_status IN ('ACTIVE','INACTIVE')),
    recipe_board_comment_created_at TIMESTAMP NOT NULL,
    recipe_board_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피댓글' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_recomment (
    recipe_board_recomment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_board_recomment_content TEXT NOT NULL,
    recipe_board_recomment_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recipe_board_recomment_status IN ('ACTIVE','INACTIVE')),
    recipe_board_recomment_created_at TIMESTAMP NOT NULL,
    recipe_board_comment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_board_comment_id) REFERENCES recipe_board_comment(recipe_board_comment_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피대댓글' DEFAULT CHARSET=UTF8;

CREATE TABLE recommended_menu (
    recommended_menu_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    satisfaction VARCHAR(255) NOT NULL CHECK(satisfaction IN ('GOOD','BAD')),
    recommended_menu_status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE' CHECK(recommended_menu_status IN ('ACTIVE','INACTIVE')),
	 recommended_menu_created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='추천요리' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_like (
    recipe_board_like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    like_created_at TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL,
    recipe_board_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피좋아요' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_favorite (
    recipe_board_favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_board_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피즐겨찾기' DEFAULT CHARSET=UTF8;

CREATE TABLE ai_recipe (
    ai_recipe_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    ai_menu_name VARCHAR(255) NOT NULL,
    ai_menu_ingredient TEXT NOT NULL,
    ai_menu_image TEXT,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE 
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='AI요리레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE public_data_recipe (
    public_data_recipe_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    public_data_menu_name VARCHAR(255) NOT NULL,
    public_data_menu_ingredient TEXT NOT NULL,
    public_data_menu_image TEXT,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='공공데이터요리레시피' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_manual (
    recipe_manual_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_manual_step INT NOT NULL,
    manual_menu_image TEXT,
    manual_content TEXT NOT NULL,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id) ON DELETE CASCADE
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='요리메뉴얼' DEFAULT CHARSET=UTF8;

CREATE TABLE recipe_board_manual (
    recipe_board_manual_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_board_manual_step INT NOT NULL,
    recipe_board_manual_image TEXT,
    recipe_board_manual_content TEXT NOT NULL,
    recipe_board_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_board_id) REFERENCES recipe_board(recipe_board_id)
) ENGINE=INNODB AUTO_INCREMENT=1 COMMENT='나만의레시피메뉴얼' DEFAULT CHARSET=UTF8;

-- 테이블 생성 후 확인
SHOW TABLES;

-- 티어 데이터 삽입
INSERT INTO tier (tier_name, tier_criteria,tier_image)
VALUES
    ('브론즈', 0,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/bronze_tier.png'),    -- likes >= 0
    ('실버', 1,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/silver_tier.png'),      -- likes >= 1
    ('골드', 5,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/gold_tier.png'),      -- likes >= 5
    ('플레티넘', 10,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/platinum_tier.png'), -- likes >= 10
    ('다이아', 20,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/diamond_tier.png'),   -- likes >= 20
    ('챌린저', 50,'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/challenger_tier.png');   -- likes >= 50

-- 회원 데이터 삽입 (likes에 따라 tier_id 자동 할당)
INSERT INTO user (user_name, password, nickname, email, profile_image, created_at, signup_path, user_identifier, user_auth_id, user_status, user_role, accept_status, tier_id, user_likes)
VALUES
    -- 관리자는 tier_id와 likes 없이 삽입
    ('조찬국', '$2a$10$3v.mR/vxwbRrExd0503Dte3eyw5ycrMwZKfwlqvn5E5Kb146/ZoGC', '조찬국', 'alice@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/admin_default_profile.png', '2023-08-01 10:30:00', 'ADMIN', CONCAT('ADMIN_', 'yoribogo'), 'yoribogo', 'ACTIVE', 'ADMIN', 'Y', NULL, NULL),  -- 관리자
    ('김범기', '$2a$10$3v.mR/vxwbRrExd0503Dte3eyw5ycrMwZKfwlqvn5E5Kb146/ZoGC', 'princess_d', 'diana@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/admin_default_profile.png', '2023-08-04 09:15:00', 'ADMIN', CONCAT('ADMIN_', 'diana789'), 'diana789', 'ACTIVE', 'ADMIN', 'Y', NULL, NULL),  -- 관리자
    
    -- 일반 회원은 likes에 따라 tier_id 할당
    ('김석민', 'password123', 'bob_the_builder', 'bob@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png', '2023-08-02 11:45:00', 'NAVER', CONCAT('NAVER_', '987654321'), '987654321', 'ACTIVE', 'ENTERPRISE', 'Y', 5, 25),  -- 다이아 (likes >= 20)
    ('조국찬', 'password456', '조국찬', 'changuk0308@naver.com','https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png', '2023-08-03 12:00:00', 'NORMAL', CONCAT('NORMAL_', 'abc123xyz'), 'abc123xyz', 'ACTIVE', 'ENTERPRISE', 'Y', 4, 15),  -- 플레티넘 (likes >= 10)
    ('이소라', 'password789', 'eve_123', 'eve@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png', '2023-08-05 14:20:00', 'KAKAO', CONCAT('KAKAO_', 'kakao98765'), 'kakao98765', 'ACTIVE', 'ENTERPRISE', 'Y', 3, 8),  -- 골드 (likes >= 5)
    ('박민수', 'password123', 'minsu_p', 'minsu@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png', '2023-09-01 10:00:00', 'NORMAL', CONCAT('NORMAL_', 'minsu123'), 'minsu123', 'ACTIVE', 'ENTERPRISE', 'Y', 2, 3),  -- 실버 (likes >= 1)
    ('이정현', 'password456', 'jung_h', 'jung@example.com', 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png', '2023-09-05 09:30:00', 'KAKAO', CONCAT('KAKAO_', 'jung123'), 'jung123', 'ACTIVE', 'ENTERPRISE', 'Y', 1, 0);  -- 브론즈 (likes >= 0)


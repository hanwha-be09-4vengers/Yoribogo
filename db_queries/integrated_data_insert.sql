USE yoribogodb;


-- 티어 데이터 삽입
INSERT INTO tier (tier_name, tier_criteria)
VALUES
    ('브론즈', 0),    -- likes >= 0
    ('실버', 1),      -- likes >= 1
    ('골드', 5),      -- likes >= 5
    ('플레티넘', 10), -- likes >= 10
    ('다이아', 20),   -- likes >= 20
    ('챌린저', 50);   -- likes >= 50

-- 회원 데이터 삽입 (likes에 따라 tier_id 자동 할당)
INSERT INTO user (user_name, password, nickname, email, profile_image, created_at, signup_path, user_identifier, user_auth_id, user_status, user_role, accept_status, tier_id, user_likes)
VALUES
    -- 관리자는 tier_id와 likes 없이 삽입
    ('조찬국', '1234', '조찬국', 'alice@example.com', NULL, '2023-08-01 10:30:00', 'ADMIN', CONCAT('ADMIN_', 'changuk1234'), 'changuk1234', 'ACTIVE', 'ADMIN', 'Y', NULL, NULL),  -- 관리자
    ('김범기', 'password123', 'princess_d', 'diana@example.com', NULL, '2023-08-04 09:15:00', 'ADMIN', CONCAT('ADMIN_', 'diana789'), 'diana789', 'ACTIVE', 'ADMIN', 'Y', NULL, NULL),  -- 관리자
    
    -- 일반 회원은 likes에 따라 tier_id 할당
    ('김석민', 'password123', 'bob_the_builder', 'bob@example.com', NULL, '2023-08-02 11:45:00', 'NAVER', CONCAT('NAVER_', '987654321'), '987654321', 'ACTIVE', 'ENTERPRISE', 'Y', 5, 25),  -- 다이아 (likes >= 20)
    ('전현서', 'password456', 'charlie_chaplin', 'charlie@example.com', NULL, '2023-08-03 12:00:00', 'NORMAL', CONCAT('NORMAL_', 'abc123xyz'), 'abc123xyz', 'ACTIVE', 'ENTERPRISE', 'Y', 4, 15),  -- 플레티넘 (likes >= 10)
    ('이소라', 'password789', 'eve_123', 'eve@example.com', NULL, '2023-08-05 14:20:00', 'KAKAO', CONCAT('KAKAO_', 'kakao98765'), 'kakao98765', 'ACTIVE', 'ENTERPRISE', 'Y', 3, 8),  -- 골드 (likes >= 5)
    ('박민수', 'password123', 'minsu_p', 'minsu@example.com', NULL, '2023-09-01 10:00:00', 'NORMAL', CONCAT('NORMAL_', 'minsu123'), 'minsu123', 'ACTIVE', 'ENTERPRISE', 'Y', 2, 3),  -- 실버 (likes >= 1)
    ('이정현', 'password456', 'jung_h', 'jung@example.com', NULL, '2023-09-05 09:30:00', 'KAKAO', CONCAT('KAKAO_', 'jung123'), 'jung123', 'ACTIVE', 'ENTERPRISE', 'Y', 1, 0);  -- 브론즈 (likes >= 0)

-- 추천 요리 데이터 삽입
INSERT INTO recommended_menu (satisfaction, recommended_menu_status, recommended_menu_created_at, user_id, recipe_id)
VALUES
	 ('GOOD', 'ACTIVE', CURRENT_TIMESTAMP(), 3, 1),
	 ('GOOD', 'INACTIVE', CURRENT_TIMESTAMP(), 3, 2),
	 ('BAD', 'ACTIVE', CURRENT_TIMESTAMP(), 3, 3),
	 ('BAD', 'INACTIVE', CURRENT_TIMESTAMP(), 3, 4),
	 ('GOOD', 'ACTIVE', CURRENT_TIMESTAMP(), 3, 5),
	 ('GOOD', 'ACTIVE', CURRENT_TIMESTAMP(), 3, 6);


-- 데이터 확인
SELECT * FROM tier;
SELECT * FROM user;
-- 그 뒤 테이블 조회

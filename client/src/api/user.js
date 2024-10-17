import apiClient from '@/api/axios';  // Axios 설정이 적용된 apiClient 사용

/**
 * 회원가입 시 인증번호 이메일 전송 함수
 */
export const sendSignupVerificationEmail = async (email) => {
  try {
    const response = await apiClient.post('/users/verification-email/signup', { email });
    return response.data;
  } catch (error) {
    console.error('sendSignupVerificationEmail 에러:', error);
    throw error;
  }
};

/**
 * 아이디 찾기 시 인증번호 이메일 전송 함수
 */
export const sendAuthIdVerificationEmail = async (nickname, email) => {
  try {
    const response = await apiClient.post('/users/verification-email/auth-id', {
      nickname,
      email
    });
    return response.data;
  } catch (error) {
    console.error('sendAuthIdVerificationEmail 에러:', error);
    throw error;
  }
};

/**
 * 비밀번호 찾기 시 인증번호 이메일 전송 함수
 */
export const sendPasswordResetVerificationEmail = async (userAuthId, email) => {
  try {
    const response = await apiClient.post('/users/verification-email/user-password', {
      user_auth_id: userAuthId,
      email
    });
    return response.data;
  } catch (error) {
    console.error('sendPasswordResetVerificationEmail 에러:', error);
    throw error;
  }
};

/**
 * 인증번호 확인 함수
 */
export const confirmVerificationCode = async (email, code) => {
  try {
    const response = await apiClient.post('/users/verification-email/confirmation', {
      email,
      code
    });
    return response.data;
  } catch (error) {
    console.error('confirmVerificationCode 에러:', error);
    throw error;
  }
};

/**
 * 일반 회원가입 함수
 */
export const signupUser = async (userData) => {
  try {
    const response = await apiClient.post('/users/signup/normal', userData);
    return response.data;
  } catch (error) {
    console.error('signupUser 에러:', error);
    throw error;
  }
};

/**
 *  카카오 로그인 처리 함수
 */
export const handleKakaoLogin = async (code) => {
  try {
    const response = await apiClient.post('/users/oauth2/kakao', { code });
    return response.data;
  } catch (error) {
    console.error('카카오 로그인 처리 중 오류:', error);
    throw error;
  }
};

/**
 *  네이버 로그인 처리 함수
 */
export const handleNaverLogin = async (code, state) => {
  try {
    const response = await apiClient.post('/users/oauth2/naver', { code, state });
    return response.data;
  } catch (error) {
    console.error('네이버 로그인 처리 중 오류:', error);
    throw error;
  }
};

// 사용자 프로필 정보 조회 함수
export const fetchUserProfile = async (userIdentifier, accessToken) => {
  try {
    const response = await apiClient.get(`/users/identifier`, {
      params: { user_identifier: userIdentifier },
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return response.data;
  } catch (error) {
    console.error('사용자 프로필 조회 중 오류:', error);
    throw error;
  }
};

/**
 * user_auth_id로 사용자 정보 조회 함수
 */
export const fetchUserByAuthId = async (userAuthId, accessToken) => {
  try {
    const response = await apiClient.get(`/users/userAuthId`, {
      params: { user_auth_id: userAuthId }, // 여기에 user_auth_id가 포함되어야 합니다.
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    return response.data;
  } catch (error) {
    console.error('user_auth_id로 사용자 정보 조회 중 오류:', error);
    throw error;
  }
};

/**
 * user_id로 사용자 정보 조회 함수
 */
export const fetchUserById = async (userId, accessToken) => {
  try {
    const response = await apiClient.get(`/users/${userId}`, {
      headers: { Authorization: `Bearer ${accessToken}` }, // 인증 토큰을 포함
    });
    return response.data;  // 반환된 사용자 정보
  } catch (error) {
    console.error('user_id로 사용자 정보 조회 중 오류:', error);
    throw error;
  }
};

/**
 * 회원 재활성화 함수
 */
export const reactivateUser = async (userId) => {
  try {
    const response = await apiClient.patch(`/user-service/api/users/${userId}/activate`);
    return response.data;
  } catch (error) {
    console.error('reactivateUser 에러:', error);
    throw error;
  }
};

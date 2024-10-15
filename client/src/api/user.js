import axios from 'axios';

// 인증 헤더가 필요한 요청에 대해 헤더 추가
const headersWithAuth = (accessToken) => ({
  headers: {
    Authorization: `Bearer ${accessToken}`,
  }
});

/**
 * 회원가입 시 인증번호 이메일 전송 함수
 */
export const sendSignupVerificationEmail = async (email) => {
    try {
      const response = await axios.post('/api/users/verification-email/signup', {
        email: email
      });
      return response.data;
    } catch (error) {
      console.error('sendSignupVerificationEmail 에러:', error);
      throw error;
    }
  };

  /**
 * 아이디 찾기 시 인증번호 이메일 전송 함수
 */
export const sendAuthIdVerificationEmail = async (nickname, email, accessToken) => {
    try {
      const response = await axios.post('/api/users/verification-email/auth-id', {
        nickname: nickname,
        email: email
      }, headersWithAuth(accessToken));
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
      const response = await axios.post('/api/users/verification-email/user-password', {
        user_auth_id: userAuthId,
        email: email
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
      const response = await axios.post('/api/users/verification-email/confirmation', {
        email: email,
        code: code
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
      const response = await axios.post('/api/users/signup/normal', userData);
      return response.data;
    } catch (error) {
      console.error('signupUser 에러:', error);
      throw error;
    }
  };
  
  /**
   * 카카오 로그인 함수
   */
  export const kakaoLogin = async (code) => {
    try {
      const response = await axios.post('/api/users/oauth2/kakao', {
        code: code
      });
      return response.data;
    } catch (error) {
      console.error('kakaoLogin 에러:', error);
      throw error;
    }
  };
  
  /**
   * 네이버 로그인 함수
   */
  export const naverLogin = async (code) => {
    try {
      const response = await axios.post('/api/users/oauth2/naver', {
        code: code
      });
      return response.data;
    } catch (error) {
      console.error('naverLogin 에러:', error);
      throw error;
    }
  };
  
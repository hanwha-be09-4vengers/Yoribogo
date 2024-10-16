import { defineStore } from 'pinia';
import { reactive } from 'vue';
import axios from 'axios';

export const useTokenStore = defineStore('token', () => {
  const token = reactive({
    userId: null, // userId를 포함한 사용자 정보 초기화
    userIdentifier: null,
    accessToken: null,
    accessTokenExpiry: null,
    refreshToken: null,
    refreshTokenExpiry: null,
  });

  // 토큰 데이터 설정 메서드 (로그인 성공 시 호출)
  const setTokenData = async (tokenData) => {
    // API 응답에서 받은 tokenData를 상태에 저장
    token.userId = tokenData?.user_id || null;  // user_id 포함
    token.userIdentifier = tokenData?.user_identifier || null;
    token.accessToken = tokenData?.access_token || null;
    token.accessTokenExpiry = tokenData?.access_token_expiry || null;
    token.refreshToken = tokenData?.refresh_token || null;
    token.refreshTokenExpiry = tokenData?.refresh_token_expiry || null;

    // 로컬 스토리지에 저장 (동기적)
    localStorage.setItem('token', JSON.stringify(token));
    console.log('토큰이 저장되었습니다:', token);
  };

  // 사용자 정보 조회 함수
  const fetchUserId = async (userIdentifier) => {
    try {
      const response = await axios.get('/api/users/identifier', {
        headers: {
          Authorization: `Bearer ${token.accessToken}`, // 올바른 액세스 토큰을 전달
        },
        params: {
          user_identifier: userIdentifier,
        },
      });

      if (response.data.success) {
        token.userId = response.data.data.user_id; // user_id 저장
        console.log('사용자 정보 조회 성공:', response.data.data);
      } else {
        console.error('사용자 정보를 가져오지 못했습니다.');
      }
    } catch (error) {
      console.error('사용자 정보 조회 오류:', error);
    }
  };

  // 새 액세스 토큰 발급 함수 (리프레시 토큰 사용)
  const refreshAccessToken = async () => {
    try {
      const response = await axios.post('/api/auth/refresh-token', {
        refresh_token: token.refreshToken,
      });

      if (response.data.success) {
        const newTokenData = response.data.data;
        await setTokenData(newTokenData); // 새로운 토큰을 저장
        return newTokenData.access_token; // 새로운 액세스 토큰 반환
      } else {
        console.error('리프레시 토큰으로 토큰 갱신 실패:', response.data.error);
        return null;
      }
    } catch (error) {
      console.error('리프레시 토큰 오류:', error);
      return null;
    }
  };

  // 애플리케이션 초기화 시 로컬 스토리지에서 데이터 복원
  const initializeState = () => {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      const parsedToken = JSON.parse(storedToken);

      // 상태 복원
      token.userId = parsedToken.userId || null;
      token.userIdentifier = parsedToken.userIdentifier || null;
      token.accessToken = parsedToken.accessToken || null;
      token.accessTokenExpiry = parsedToken.accessTokenExpiry || null;
      token.refreshToken = parsedToken.refreshToken || null;
      token.refreshTokenExpiry = parsedToken.refreshTokenExpiry || null;

      console.log('로컬 스토리지에서 토큰이 복원되었습니다:', token);
      
      // 토큰이 유효한 경우 사용자 정보 확인
      if (token.accessToken && token.userIdentifier) {
        fetchUserId(token.userIdentifier); // userId 복원
      }
    } else {
      console.log('로컬 스토리지에 저장된 토큰이 없습니다. 기본값으로 설정합니다.');
    }
  };

  // 로그아웃 처리
  const logout = () => {
    // 모든 토큰 및 사용자 데이터 초기화
    token.userId = null;
    token.userIdentifier = null;
    token.accessToken = null;
    token.accessTokenExpiry = null;
    token.refreshToken = null;
    token.refreshTokenExpiry = null;

    // 로컬 스토리지에서도 제거
    localStorage.removeItem('token');
    console.log('로그아웃되었습니다. 토큰이 초기화되었습니다.');
  };

  return {
    token,
    setTokenData,
    refreshAccessToken,
    fetchUserId,
    initializeState,
    logout,
  };
});

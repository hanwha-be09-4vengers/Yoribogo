<template class="root-container">
  <router-view /> <!-- 여기에 라우터가 활성화된 컴포넌트를 렌더링 -->
</template>


<script>
import { reactive, provide, onMounted } from 'vue';
import axios from 'axios'; // axios 임포트 추가

export default {
  setup() {
    // 사용자 토큰 상태 관리
    const token = reactive({
      userId: null,
      userIdentifier: null, // user_identifier도 상태에 추가
      accessToken: null,
      accessTokenExpiry: null,
      refreshToken: null,
      refreshTokenExpiry: null,
    });

    // 사용자 토큰 저장 메서드 (로그인 성공 시 토큰 저장)
    const setTokenData = async (tokenData) => {
  token.userIdentifier = tokenData.user_identifier;
  token.accessToken = tokenData.access_token;
  token.accessTokenExpiry = tokenData.access_token_expiry;
  token.refreshToken = tokenData.refresh_token;
  token.refreshTokenExpiry = tokenData.refresh_token_expiry;

  // 토큰이 설정된 후에 사용자 정보를  조회
  if (token.accessToken) {
    await fetchUserId(token.userIdentifier);
  } else {
    console.log('accessToken이 없습니다. 사용자 정보를 조회할 수 없습니다.');
  }

  localStorage.setItem('token', JSON.stringify(token));
  console.log('로그인 혹은 로그아웃 성공! 토큰 정보:', {
    userId: token.userId,
    userIdentifier: token.userIdentifier,
    accessToken: token.accessToken,
    refreshToken: token.refreshToken,
  });
};

  const fetchUserId = async (userIdentifier) => {
    try {
      // 쿼리 매개변수를 통해 user_identifier를 전송
      const response = await axios.get('/user-service/api/users/identifier', {
        headers: {
          Authorization: `Bearer ${token.accessToken}` // 올바른 액세스 토큰을 포함
        },
        params: { // params 객체를 사용하여 쿼리 매개변수 전송
          user_identifier: userIdentifier
        }
      });
      console.log('API 응답:', response);

      if (response.data.success) {
        token.userId = response.data.data.user_id;
      } else {
        console.error('사용자 정보를 가져오지 못했습니다. 응답:', response.data);
      }
    } catch (error) {
      console.error('사용자 정보 조회 오류:', error);
    }
  };


    // 애플리케이션 초기화 시 localStorage에서 데이터 복원
    const initializeState = () => {
      const storedToken = localStorage.getItem('token');

      if (storedToken) {
        const parsedToken = JSON.parse(storedToken);
        Object.assign(token, parsedToken); // 복원된 토큰 상태 설정
      }
    };

    // 초기화 함수 호출 (마운트 시 실행)
    onMounted(() => {
      initializeState();
    });

    // 사용자 상태 제공
    provide('token', token);
    provide('setTokenData', setTokenData); // setTokenData 메서드도 제공


    return {
      token,
      setTokenData,
    };
  },
};
</script>

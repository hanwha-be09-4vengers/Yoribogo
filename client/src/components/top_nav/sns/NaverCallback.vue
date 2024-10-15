<template>
    <div>
      <p v-if="loading">네이버 로그인 중입니다...</p>
      <p v-if="error">{{ error }}</p>
      
      <!-- 닉네임이 없을 경우 프로필 설정 모달 표시 -->
      <ProfileOAuthModal v-if="showProfileModal" @close="closeProfileModal" />
    </div>
  </template>
  
  <script setup>
  import { onMounted, ref, inject } from 'vue';
  import { useRouter } from 'vue-router';
  import axios from 'axios';
  import ProfileOAuthModal from '@/components/top_nav/login/ProfileOAuthModal.vue';
  
  // 상위 컴포넌트(App.vue)에서 제공된 토큰 저장 함수 가져오기
  const setTokenData = inject('setTokenData');
  const router = useRouter();
  const loading = ref(true);
  const error = ref('');
  const showProfileModal = ref(false); // 프로필 모달 표시 여부
  
  // 네이버에서 전달받은 `code`와 `state`를 서버로 전달하여 토큰 및 사용자 정보 받기
  const handleNaverCallback = async () => {
    const code = router.currentRoute.value.query.code;
    const state = router.currentRoute.value.query.state;  // 네이버는 state도 필요합니다.

    console.log('네이버 로그인 code:',code);
    console.log('네이버 로그인 state:',state);
  
    if (code && state) {
      try {
        const response = await axios.post('http://localhost:8080/api/users/oauth2/naver', { code, state });
  
        if (response.data.success) {
          const tokenData = response.data.data;
          console.log('네이버 로그인 성공 후 받은 토큰:', tokenData);
  
          await setTokenData(tokenData);
  
          // 올바른 user_identifier로 사용자 정보 조회
          const userProfileResponse = await axios.get(`/api/users/identifier`, {
            params: { user_identifier: tokenData.user_identifier },
            headers: { Authorization: `Bearer ${tokenData.access_token}` }
          });
  
          // API 응답에서 userProfile을 안전하게 처리
          const userProfile = userProfileResponse?.data?.data || {};
          
          console.log('userProfile 정보: ', userProfile);
  
          if (!userProfile.nickname || userProfile.nickname === '') {
            showProfileModal.value = true;  // 닉네임이 없는 경우 프로필 설정 모달 표시
          } else {
            router.push('/'); // 닉네임이 있으면 홈으로 리다이렉트
          }
        } else {
          throw new Error(response.data.error || '네이버 로그인 실패');
        }
      } catch (err) {
        error.value = '로그인 처리 중 오류가 발생했습니다.';
        console.error('네이버 로그인 처리 중 오류:', err);
      } finally {
        loading.value = false;
      }
    } else {
      error.value = '네이버 인증 코드 또는 state가 없습니다.';
      loading.value = false;
    }
  };
  
  // 모달 닫기 함수
  const closeProfileModal = () => {
    showProfileModal.value = false;
  };
  
  onMounted(() => {
    handleNaverCallback();
  });
  </script>
  
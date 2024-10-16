<template>
  <div>
    <p v-if="loading">네이버 로그인 중입니다...</p>
    <p v-if="error">{{ error }}</p>

    <!-- 닉네임이 없을 경우 프로필 설정 모달 표시 -->
    <ProfileOAuthModal v-if="showProfileModal" @close="closeProfileModal" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { handleNaverLogin, fetchUserProfile } from '@/api/user'; // 로그인 및 사용자 정보 관련 API 함수
import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어

const router = useRouter();
const tokenStore = useTokenStore();

const loading = ref(true);
const error = ref('');
const showProfileModal = ref(false); // 프로필 모달 표시 여부

// 네이버 로그인 처리 함수
const handleNaverCallback = async () => {
  const code = router.currentRoute.value.query.code;
  const state = router.currentRoute.value.query.state;

  if (code && state) {
    try {
      const response = await handleNaverLogin(code, state);

      if (response.success) {
        const tokenData = response.data;
        console.log('네이버 로그인 성공 후 받은 토큰:', tokenData);

        // 사용자 프로필 조회
        const userProfileResponse = await fetchUserProfile(tokenData.user_identifier, tokenData.access_token);
        const userProfile = userProfileResponse?.data || {};

        console.log('userProfile 정보:', userProfile);

        // Pinia 스토어에 토큰 저장 (user_id 포함)
        await tokenStore.setTokenData({
          ...tokenData,
          user_id: userProfile.user_id, // 프로필에서 user_id 추가
        });

        // // 닉네임이 없으면 프로필 설정 모달 표시
        // if (!userProfile.nickname || userProfile.nickname === '') {
        //   showProfileModal.value = true;
        // } else {
        //   router.push('/'); // 닉네임이 있으면 홈으로 리다이렉트
        // }
        router.push('/'); // 닉네임이 있으면 홈으로 리다이렉트
      } else {
        throw new Error(response.error || '네이버 로그인 실패');
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

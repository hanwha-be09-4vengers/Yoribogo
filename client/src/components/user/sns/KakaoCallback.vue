<template>
  <div>
    <p v-if="loading">카카오 로그인 중입니다...</p>
    <p v-if="error">{{ error }}</p>

    <!-- 닉네임이 없을 경우 프로필 설정 모달 표시 -->
    <ProfileOAuthModal v-if="showProfileModal" @close="closeProfileModal" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import ProfileOAuthModal from '@/components/user/sns/ProfileOAuthModal.vue';
import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어에서 토큰 관리

const router = useRouter();
const tokenStore = useTokenStore();  // Pinia 스토어 사용

const loading = ref(true);
const error = ref('');
const showProfileModal = ref(false);  // 프로필 모달 표시 여부

// Kakao에서 전달받은 `code`를 서버로 전달하여 토큰 및 사용자 정보 받기
const handleKakaoCallback = async () => {
  const code = router.currentRoute.value.query.code;

  if (code) {
    try {
      // 서버로 로그인 요청을 보내서 토큰을 받아옴
      const response = await axios.post('/api/users/oauth2/kakao', { code });

      if (response.data.success) {
        const tokenData = response.data.data;
        console.log('카카오 로그인 성공 후 받은 토큰:', tokenData);

        // 사용자 프로필 조회
        const userProfileResponse = await axios.get(`/api/users/identifier`, {
          params: { user_identifier: tokenData.user_identifier },
          headers: { Authorization: `Bearer ${tokenData.access_token}` },
        });

        // API 응답에서 userProfile을 안전하게 처리
        const userProfile = userProfileResponse?.data?.data || {};
        console.log('userProfile 정보:', userProfile);

        // user_id 포함한 토큰과 사용자 정보를 Pinia 스토어에 저장
        await tokenStore.setTokenData({
          ...tokenData,           // 기존의 토큰 데이터들 (access_token, refresh_token 등)
          user_id: userProfile.user_id,  // 조회한 user_id 추가
        });

        // 프로필 설정 모달 또는 리다이렉트
        router.push('/');
      } else {
        throw new Error(response.data.error || '카카오 로그인 실패');
      }
    } catch (err) {
      error.value = '로그인 처리 중 오류가 발생했습니다.';
      console.error('카카오 로그인 처리 중 오류:', err);
    } finally {
      loading.value = false;
    }
  } else {
    error.value = '카카오 인증 코드가 없습니다.';
    loading.value = false;
  }
};

// 모달 닫기 함수
const closeProfileModal = () => {
  showProfileModal.value = false;
};

onMounted(() => {
  handleKakaoCallback();
});
</script>

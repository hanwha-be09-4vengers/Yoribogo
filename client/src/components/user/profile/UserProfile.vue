<template>
  <div class="profile-container" v-if="userProfile">
    <!-- 프로필 이미지 -->
    <div class="avatar">
      <img :src="userProfile.profileImage" alt="프로필 사진" />
    </div>

    <!-- 사용자 정보 -->
    <div class="user-info">
      <!-- 이름과 티어 이미지를 가로로 나란히 배치 -->
      <div class="name-tier-container">
        <h2>{{ userProfile.nickname }} 님</h2>
        <img :src="userProfile.tierImage" alt="티어 이미지" class="tier-image" />
      </div>

      <button @click="openEditProfileModal">회원정보 수정</button>
    </div>

    <!-- 프로필 수정 모달 -->
    <ProfileEditModal v-if="isModalOpen" @close="closeEditProfileModal" />
  </div>

  <!-- 프로필 정보 로딩 중 표시 -->
  <div v-else>
    <p>프로필 정보를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, defineEmits } from 'vue';
import { useTokenStore } from '@/stores/tokenStore'; // Pinia store 사용
import { fetchUserDetailProfile } from '@/api/user'; // user.js의 새로운 프로필 API 호출
import ProfileEditModal from './ProfileEditModal.vue'; // 모달 컴포넌트 임포트

// 사용자 프로필 상태
const userProfile = ref(null);

// 모달 상태 관리
const isModalOpen = ref(false);

// Pinia의 tokenStore 사용
const tokenStore = useTokenStore();

// 프로필 수정 모달 열기 함수
const openEditProfileModal = () => {
  isModalOpen.value = true;
};

// 프로필 수정 모달 닫기 함수
const closeEditProfileModal = () => {
  isModalOpen.value = false;
};

// API 호출을 통해 사용자 프로필 정보 로드
const loadUserProfile = async () => {
  try {
    const userId = tokenStore.token.userId;
    const accessToken = tokenStore.token.accessToken;

    if (userId && accessToken) {
      const profileData = await fetchUserDetailProfile(userId, accessToken);
      if (profileData.success) {
        userProfile.value = {
          profileImage: profileData.data.profileImage || defaultProfileImage,
          nickname: profileData.data.nickname || '닉네임 없음',
          tierImage: profileData.data.tierImage || defaultTierImage,
        };
      } else {
        console.error('프로필 정보를 불러오는 데 실패했습니다.');
      }
    } else {
      console.error('userId 또는 accessToken이 없습니다.');
    }
  } catch (error) {
    console.error('프로필 정보를 가져오는 중 오류 발생:', error);
  }
};

// 컴포넌트가 마운트될 때 사용자 프로필 로드
onMounted(() => {
  loadUserProfile();
});
</script>

<style scoped>
.profile-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  background-color: transparent;
  padding: 2rem;
  border-radius: 80px 80px 10px 10px;
  max-width: 300px;
  margin: auto;
}

.avatar img {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  border: 5px solid white;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.name-tier-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.name-tier-container h2 {
  margin: 0;
  font-size: 3rem;
  font-weight: bold;
  color: #333;
  line-height: 1;
}

.tier-image {
  width: 2.8rem;
  height: 2.8rem;
  object-fit: contain;
  margin-left: 0.5rem;
}

button {
  background-color: #ececec;
  color: black;
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 1rem;
  font-family: 'Noto Sans KR', 'Noto Sans', sans-serif;
  font-size: 1.4rem;
  font-weight: 500;
  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.15);
  transition: box-shadow 0.2s ease;
}

button:hover {
  box-shadow: 0px 6px 8px rgba(0, 0, 0, 0.2);
}
</style>

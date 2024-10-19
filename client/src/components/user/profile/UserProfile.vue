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

      <button @click="editProfile">회원정보 수정</button>
    </div>
  </div>

  <!-- 프로필 정보 로딩 중 표시 -->
  <div v-else>
    <p>프로필 정보를 불러오는 중입니다...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useTokenStore } from '@/stores/tokenStore'; // Pinia store 사용
import { fetchUserDetailProfile } from '@/api/user'; // user.js의 새로운 프로필 API 호출

// 사용자 프로필 상태
const userProfile = ref(null);

// Pinia의 tokenStore 사용
const tokenStore = useTokenStore();

// 프로필 수정 이벤트
const editProfile = () => {
  emit('editProfile');
};

// API 호출을 통해 사용자 프로필 정보 로드
const loadUserProfile = async () => {
  try {
    // tokenStore에서 userId와 accessToken 가져오기
    const userId = tokenStore.token.userId;
    const accessToken = tokenStore.token.accessToken;

    if (userId && accessToken) {
      const profileData = await fetchUserDetailProfile(userId, accessToken); // API 호출
      if (profileData.success) {
        // 프로필 데이터 설정 (이미지가 null일 경우 기본 이미지 사용)
        userProfile.value = {
          profileImage: profileData.data.profileImage || defaultProfileImage, // 기본 이미지 처리
          nickname: profileData.data.nickname || '닉네임 없음',
          tierImage: profileData.data.tierImage || defaultTierImage, // 기본 티어 이미지 처리
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
  flex-direction: column; /* 세로 방향으로 정렬 */
  align-items: center; /* 중앙 정렬 */
  gap: 1.5rem;
  background-color: transparent; /* 흰색 배경 */
  padding: 2rem;
  border-radius: 80px 80px 10px 10px; /* 상단은 둥글게, 하단은 살짝 둥글게 */
  max-width: 300px; /* 적당한 너비 설정 */
  margin: auto; /* 화면 중앙에 배치 */
}

.avatar img {
  width: 120px; /* 프로필 사진 크기 */
  height: 120px;
  border-radius: 50%; /* 원형으로 만들기 */
  object-fit: cover;
  border: 5px solid white; /* 테두리 추가 */
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center; /* 텍스트 가운데 정렬 */
}

/* 이름과 티어 이미지를 가로로 배치 */
.name-tier-container {
  display: flex;
  align-items: center; /* 세로 중앙 정렬 */
  justify-content: center; /* 가로 중앙 정렬 */
  gap: 0.5rem; /* 이름과 티어 이미지 사이 간격 */
  margin-bottom: 1rem; /*프로필 수정 버튼과 간격*/ 
}

.name-tier-container h2 {
  margin: 0;
  font-size: 3rem;
  font-weight: bold;
  color: #333;
  line-height: 1; /* 텍스트의 라인 높이를 조정 */
  display: inline-block; /* 이미지가 텍스트와 겹치지 않게 조정 */
}

.tier-image {
  width: 2.8rem; /* 티어 이미지 크기 */
  height: 2.8rem;
  object-fit: contain; /* 이미지 비율 유지 */
  margin-left: 0.5rem; /* 이름과 티어 이미지 간격 추가 */
  /* 더 이상 position을 사용하지 않음 */
}



button {
  background-color: #ececec;
  color: black;
  border: none;
  padding: 0.7rem 1.5rem;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 1rem; /* 버튼 위에 간격 추가 */
  
  /* 폰트 설정 */
  font-family: 'Noto Sans KR', 'Noto Sans', sans-serif;  /* Noto Sans KR 폰트 적용 */
  font-size: 1.4rem;  /* 폰트 크기 조정 */
  font-weight: 500;   /* 글씨 두께 조정 (보통) */

  /* 그림자 추가 */
  box-shadow: 0px 3px 6px rgba(0, 0, 0, 0.15);  /* x축, y축, 번짐, 투명도 */
  transition: box-shadow 0.2s ease;  /* 그림자 애니메이션 추가 */
}

button:hover {
  background-color: #ececec;

  /* hover 시 그림자 조금 더 강하게 */
  box-shadow: 0px 6px 8px rgba(0, 0, 0, 0.2);  /* 그림자 진하게 */
}


</style>

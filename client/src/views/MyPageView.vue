<template>
  <div class="mypage-view">

    <header>
      <NotificationButton class="notification-btn"></NotificationButton>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>

    <!-- 사용자 프로필 -->
    <MainBoard :cur="'mypage'">
      <div class="user-profile-section">
         <!-- 다른 컴포넌트들 -->
        <UserProfile @openEditProfileModal="openEditProfileModal" @openAccountOptionsModal="openAccountOptionsModal" />
      </div>

      <!-- 탭 이름 및 탭 컴포넌트 추가 -->
      <div class="tabs-container">
        <!-- 탭 이름을 먼저 표시 -->
        <h2 class="tab-title">{{ currentTab }}</h2>

        <!-- 탭 버튼 섹션 -->
        <div class="tab-section">
          <button 
            v-for="tab in tabs" 
            :key="tab" 
            @click="selectTab(tab)" 
            :class="{ active: currentTab === tab }"
            class="tab-button"
          >
            {{ tab }}
          </button>
        </div>

        <!-- 선택한 탭에 따른 컴포넌트 표시 -->
        <div class="tab-content">
          <component :is="currentComponent"></component>
        </div>
      </div>
    </MainBoard>
    
    <!-- 모달들 -->
    <ProfileEditModal v-if="isEditProfileModalOpen" @close="closeEditProfileModal" />
    <AccountOptionsModal v-if="isAccountOptionsModalOpen" 
      @close="closeAccountOptionsModal"
      @openChangePasswordModal="openChangePasswordModal"
      @openAccountDeactivationModal="openAccountDeactivationModal"
    />
    <ChangePasswordModal v-if="isChangePasswordModalOpen" @close="closeChangePasswordModal" />
    <AccountDeactivationModal v-if="isAccountDeactivationModalOpen" @close="closeAccountDeactivationModal" />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import HomeButton from '@/components/common/HomeButton.vue';
import MainBoard from '@/components/common/MainBoard.vue';
import ProfileButton from '@/components/common/ProfileButton.vue';
import NotificationButton from '@/components/common/NotificationButton.vue';

// 회원 프로필 관련 모달창 
import UserProfile from '@/components/user/profile/UserProfile.vue';
import ProfileEditModal from '@/components/user/profile/ProfileEditModal.vue';
import ChangePasswordModal from '@/components/user/profile/ChangePasswordModal.vue';
import AccountDeactivationModal from '@/components/user/profile/AccountDeactivationModal.vue';
import AccountOptionsModal from '@/components/user/profile/AccountOptionsModal.vue';

// 탭에 보여줄 컴포넌트들 import
import BookmarkedRecipes from '@/components/mypage/BookmarkedRecipes.vue';
import SatisfiedRecipes from '@/components/mypage/SatisfiedRecipes.vue';
import MyRecipes from '@/components/mypage/MyRecipes.vue';

// 탭 목록 정의
const tabs = ['북마크한 레시피', '만족했던 레시피', '내가 작성한 레시피'];
const currentTab = ref(tabs[0]); // 기본값은 첫 번째 탭
const currentComponent = ref(BookmarkedRecipes); // 기본 컴포넌트 설정

// 탭 선택 함수
const selectTab = (tab) => {
  currentTab.value = tab;
  switch (tab) {
    case '북마크한 레시피':
      currentComponent.value = BookmarkedRecipes;
      break;
    case '만족했던 레시피':
      currentComponent.value = SatisfiedRecipes;
      break;
    case '내가 작성한 레시피':
      currentComponent.value = MyRecipes;
      break;
    default:
      currentComponent.value = BookmarkedRecipes; // 기본값
  }
};

/* 모달 상태관리 */
const isEditProfileModalOpen = ref(false);
const isAccountOptionsModalOpen = ref(false);
const isChangePasswordModalOpen = ref(false);
const isAccountDeactivationModalOpen = ref(false);

const openEditProfileModal = () => {
  console.log('openEditProfileModal called'); // 디버깅용 로그
  isEditProfileModalOpen.value = true;
};

const closeEditProfileModal = () => {
  isEditProfileModalOpen.value = false;
};

const openAccountOptionsModal = () => {
  console.log('openAccountOptionsModal called'); // 디버깅용 로그
  isAccountOptionsModalOpen.value = true;
};

const closeAccountOptionsModal = () => {
  isAccountOptionsModalOpen.value = false;
};

const openChangePasswordModal = () => {
  isChangePasswordModalOpen.value = true;
  closeAccountOptionsModal();
};

const closeChangePasswordModal = () => {
  isChangePasswordModalOpen.value = false;
};

const openAccountDeactivationModal = () => {
  isAccountDeactivationModalOpen.value = true;
  closeAccountOptionsModal();
};

const closeAccountDeactivationModal = () => {
  isAccountDeactivationModalOpen.value = false;
};
</script>



<style scoped>
.mypage-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

/* 프로필 사진 고정된 위치 (탭과 독립) */
.user-profile-section {
  position: absolute;
  top: -10rem;
  left: 12rem; /* 고정된 위치 */
  transform: translateX(-50%);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10; /* 다른 요소 위로 위치 */
}

/* 탭 컨테이너 */
.tabs-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  margin-top: 8rem; /* 상단으로부터 여유 공간 추가 */
}

/* 탭 이름 스타일 */
.tab-title {
  font-size: 2.8rem;
  font-weight: bold;
  margin-bottom: 1rem; /* 탭 버튼과의 간격 */
  margin-top: 0; /* 여백이 위쪽으로 밀리지 않도록 */
  text-align: center;
}

/* 탭 버튼 섹션 */
.tab-section {
  display: flex;
  justify-content: center;
  position: relative;
  margin-bottom: 1rem; /* 탭과 콘텐츠 사이 간격 */
  margin-top: 1rem; /* 탭 제목과의 간격 */
}

/* 탭 버튼 스타일 */
.tab-button {
  background-color: #DADADA;
  border: none;
  padding: 1rem 3.4rem;
  margin: 0 -1.8rem; /* 버튼들이 겹치게 하는 마진 */
  border-radius: 30px;
  cursor: pointer;
  font-size: 1.6rem;
  color: white;
  z-index: 1; /* 기본 z-index 값 */
  position: relative;
  font-family: 'Noto Sans KR', sans-serif; /* Noto Sans KR 폰트 지정 */
}

.tab-button.active {
  background-color: #2c3e50;
  color: white;
  z-index: 2; /* 활성화된 버튼을 다른 버튼 위로 */
}

/* 탭 콘텐츠 영역 */
.tab-content {
  width: 100%;
  display: flex;
  justify-content: center;
  margin-top: 8rem; /* 탭과 콘텐츠 간의 간격 */
}

/* 상단 버튼들 위치 */
.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
}

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 20rem;
}
</style>

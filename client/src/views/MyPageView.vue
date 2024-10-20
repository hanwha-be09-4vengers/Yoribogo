<template>
  <div class="mypage-view">
    <header>
      <NotificationButton class="notification-btn"></NotificationButton>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>

    <!-- 사용자 프로필 -->
    <MainBoard :cur="'mypage'">
      <section class="user-profile-section">
        <!-- 다른 컴포넌트들 -->
        <UserProfile @openEditProfileModal="openEditProfileModal" @openAccountOptionsModal="openAccountOptionsModal" />
      </section>

      <!-- 탭 이름 및 탭 컴포넌트 추가 -->
      <div class="tabs-container">
        <!-- 탭 이름을 먼저 표시 -->
        <h2 class="tab-title">{{ currentTab }}</h2>

        <!-- 탭 버튼 섹션 -->
        <section class="tab-section">
          <button
            v-for="tab in tabs"
            :key="tab"
            @click="selectTab(tab)"
            :class="{ active: currentTab === tab }"
            class="tab-button"
          >
            {{ tab }}
          </button>
        </section>

        <!-- 선택한 탭에 따른 컴포넌트 표시 -->
        <div class="tab-content">
          <component :is="currentComponent" :menuList="menuList" :isEmpty="isEmpty"></component>
        </div>
      </div>
    </MainBoard>

    <!-- 회원 관련 모달들 -->
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
import { ref, shallowRef, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // vue-router 임포트
import { useTokenStore } from '@/stores/tokenStore'; // Pinia store 사용
import axios from 'axios';

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
const tabs = ['만족했던 레시피', '북마크한 레시피', '내가 작성한 레시피'];
const currentTab = ref(tabs[1]); // 기본값은 첫 번째 탭
const currentComponent = shallowRef(BookmarkedRecipes); // 기본 컴포넌트 설정

// 탭 선택 함수
const selectTab = (tab) => {
  currentTab.value = tab;
  switch (tab) {
    case '북마크한 레시피':
      currentComponent.value = BookmarkedRecipes;
      menuList.value = bookmarkedRecipeList.value
      break;
    case '만족했던 레시피':
      currentComponent.value = SatisfiedRecipes;
      menuList.value = satisfiedRecipeList.value
      break;
    case '내가 작성한 레시피':
      currentComponent.value = MyRecipes;
      menuList.value = myRecipeList.value
      break;
    default:
      currentComponent.value = SatisfiedRecipes; // 기본값
  }

  if(menuList.value.length === 0) isEmpty.value = true
  else isEmpty.value = false
};

/* 모달 상태관리 */
const isEditProfileModalOpen = ref(false);
const isAccountOptionsModalOpen = ref(false);
const isChangePasswordModalOpen = ref(false);
const isAccountDeactivationModalOpen = ref(false);

const openEditProfileModal = () => {
  console.log('openEditProfileModal called');
  isEditProfileModalOpen.value = true;
};

const closeEditProfileModal = () => {
  isEditProfileModalOpen.value = false;
};

const openAccountOptionsModal = () => {
  console.log('openAccountOptionsModal called');
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

// 라우터 사용
const router = useRouter();

const satisfiedRecipeList = ref([])
const bookmarkedRecipeList = ref([])
const myRecipeList = ref([])
const menuList = ref([])
const isEmpty = ref(true)

// 만족했던 레시피
const fetchSatisfiedRecipes = async (userId, page) => {
  try {
    const response = (await axios.get(`/api/recommended-menus?user=${userId}&page=${page}`)).data
    if (response.success) {
      satisfiedRecipeList.value = response.data.content
    } else {
      satisfiedRecipeList.value = []
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 북마크한 레시피
const fetchBookmarkedRecipes = async (userId,page) => {
  try {
    const response = (await axios.get(`/api/recipe-board/favorites/users/${userId}?pageNo=${page}`)).data
    if (response.success) {
      bookmarkedRecipeList.value = response.data.content
      menuList.value = bookmarkedRecipeList.value
      isEmpty.value = false
    } else {
      bookmarkedRecipeList.value = []
      menuList.value = bookmarkedRecipeList.value
      isEmpty.value = true
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

// 내가 작성한 레시피
const fetchMyRecipes = async (userId, page) => {
  try {
    const response = (await axios.get(`/api/recipe-board/users/${userId}/boards?pageNo=${page}`)).data
    if (response.success) {
      myRecipeList.value = response.data.content
    } else {
      myRecipeList.value = []
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}


// 토큰 확인 및 리다이렉트 처리
const tokenStore = useTokenStore();
onMounted(async () => {
  if (!tokenStore.token.accessToken) {
    alert('마이페이지를 보시려면 로그인이 필요합니다!');
    router.push('/login'); // 로그인 페이지로 리다이렉트
    return;
  }

  const userId = JSON.parse(localStorage.getItem('token')).userId;
  await fetchBookmarkedRecipes(userId,1);
  await fetchMyRecipes(userId,1);
  await fetchSatisfiedRecipes(userId,1);
});
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
  margin-top: 0; /* 여백이 위쪽으로 밀리지 않도록 */
  text-align: center;
}

/* 탭 버튼 섹션 */
.tab-section {
  display: flex;
  justify-content: center;
  position: relative;
  margin-top: 2rem; /* 탭 제목과의 간격 */
}

/* 탭 버튼 스타일 */
.tab-button {
  background-color: #DADADA;
  border: none;
  padding: 1rem 4.4rem;
  margin: 0 -1.75rem; /* 버튼들이 겹치게 하는 마진 */
  border-radius: 3rem;
  cursor: pointer;
  font-size: 1.6rem;
  color: white;
  z-index: 1; /* 기본 z-index 값 */
  position: relative;
}

.tab-button.active {
  background-color: #2c3e50;
  color: white;
  z-index: 2; /* 활성화된 버튼을 다른 버튼 위로 */
}

/* 탭 콘텐츠 영역 */
.tab-content {
  display: flex;
  justify-content: center;
  width: 100%;
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

@media screen and (max-width: 1024px) {
  .tab-button {
    padding: 1.1rem 4rem;
    margin: 0 -1.6rem;
    font-size: 1.4rem;
  }
}

@media screen and (max-width: 960px) {
  .tab-button {
    padding: 1.1rem 4rem;
    margin: 0 -1.4rem;
    font-size: 1.35rem;
  }
}

@media screen and (max-width: 900px) {
  .tab-title {
    margin-top: 10rem;
  }

  .tab-section {
    flex-direction: column;
    position: absolute;
    right: -8rem;
    gap: 1rem;
  }

  .tab-button {
    padding: 1rem 3rem;
    font-size: 1.4rem;
  }
}

@media screen and (max-width: 600px) {
  .tab-section {
    right: -6.5rem;
    gap: 1rem;
  }

  .tab-button {
    padding: 1rem 2.5rem;
    font-size: 1.3rem;
  }

  .user-profile-section {
    top: -9rem;
    left: 9rem;
  }
}

@media screen and (max-width: 425px) {
  .user-profile-section {
    top: -9rem;
    left: 8.5rem;
  }
}

@media screen and (max-width: 370px) {
  .user-profile-section {
    top: -9rem;
    left: 8rem;
  }
}
</style>

<template>
  <div class="mypage-view">
    <header>
      <NotificationButton class="notification-btn"></NotificationButton>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>

    <!-- MainBoard 내부에 UserProfile을 추가 -->
    <MainBoard :cur="'mypage'">
      <!-- MainBoard를 기준으로 UserProfile 배치 -->
      <div class="user-profile-section">
        <UserProfile :userProfile="userProfile" @editProfile="openEditProfileModal" />
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue';
import MainBoard from '@/components/common/MainBoard.vue';
import ProfileButton from '@/components/common/ProfileButton.vue';
import NotificationButton from '@/components/common/NotificationButton.vue';
import UserProfile from '@/components/user/profile/UserProfile.vue'; // UserProfile 추가

// 예시 사용자 데이터
const userProfile = {
  avatarUrl: 'https://example.com/user-avatar.jpg',
  name: '요리왕',
  statusMessage: '요리의 세계에 오신 것을 환영합니다!',
};

// 프로필 수정 모달 열기 함수
const openEditProfileModal = () => {
  console.log('프로필 수정 모달 열기');
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

/* MainBoard를 기준으로 UserProfile 배치 */
.user-profile-section {
  position: absolute; /* MainBoard를 기준으로 절대 위치 */
  top: -8rem; /* MainBoard 내부 상단에서 8rem 위에 겹치게 위치 */
  left: 4rem; /* MainBoard 내부에서 살짝 왼쪽
  transform: translateX(-50%); /* 수평 중앙 정렬 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
}

/* MainBoard 스타일에서 position relative 추가 */
mainboard {
  position: relative;
}

/* 반응형 디자인 */
@media screen and (max-width: 480px) {
  .notification-btn {
    left: 10rem;
  }

  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .notification-btn {
    left: 9rem;
  }

  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .notification-btn {
    left: 8rem;
  }

  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
</style>

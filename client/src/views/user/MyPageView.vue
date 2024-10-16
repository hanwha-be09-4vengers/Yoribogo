<template>
  <div class="mypage-view">
    <ProfileButton></ProfileButton>
    <HomeButton></HomeButton>
    <MainBoard :cur="'mypage'"></MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/top_nav/HomeButton.vue'
import MainBoard from '@/components/MainBoard.vue'
import ProfileButton from '@/components/ProfileButton.vue'
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

@media screen and (max-width: 960px) {
  .wiki-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 480px) {
  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
</style>

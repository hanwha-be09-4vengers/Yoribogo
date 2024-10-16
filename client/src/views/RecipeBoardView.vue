<template>
  <div class="recipe-board-view">
    <header>
      <ProfileButton></ProfileButton>
      <HomeButton></HomeButton>
    </header>
    <MainBoard :cur="'recipe-board'"></MainBoard>
  </div>
</template>

<script setup>
<<<<<<< HEAD
import HomeButton from '@/components/top_nav/HomeButton.vue'
import MainBoard from '@/components/MainBoard.vue'
import ProfileButton from '@/components/ProfileButton.vue'
=======
import HomeButton from '@/components/common/HomeButton.vue'
import MainBoard from '@/components/common/MainBoard.vue'
import ProfileButton from '@/components/common/ProfileButton.vue'
>>>>>>> 42713b21f0ee8f0aeca18f13638fd649cb4b9e08
</script>

<style scoped>
.recipe-board-view {
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

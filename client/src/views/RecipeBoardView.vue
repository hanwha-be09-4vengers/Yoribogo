<template>
  <div class="recipe-board-view">
    <HomeButton></HomeButton>
    <MainBoard :cur="'recipe-board'"></MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/HomeButton.vue'
import MainBoard from '@/components/MainBoard.vue'
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

.home-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}
</style>

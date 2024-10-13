<template>
    <div class="wiki-view">
      <HomeButton></HomeButton>
    </div>
</template>

<script setup>
import HomeButton from '@/components/HomeButton.vue';
</script>

<style scoped>
.wiki-view {
  position: relative;
  display: flex;
  flex-direction: column;
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
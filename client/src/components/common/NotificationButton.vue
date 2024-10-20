<template>
  <div class="notification-btn">
    <div class="notification-icon-wrapper" @click="toggleModal">
      <i class="fa-solid fa-bell"></i>
    </div>

    <!-- 모달창을 조건부로 렌더링 -->
    <NotificationModal v-if="isModalVisible" @close="toggleModal" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import NotificationModal from './NotificationModal.vue'

const isModalVisible = ref(false)

const toggleModal = () => {
  isModalVisible.value = !isModalVisible.value // 모달의 열림/닫힘을 토글
}
</script>

<style scoped>
.notification-btn {
  display: flex;
  flex-direction: column;
  width: 6.3rem;
  gap: 1rem;
}

.notification-icon-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6.3rem;
  height: 6.3rem;
  border: none;
  border-radius: 50%;
  background-color: var(--white-color);
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
  z-index: 9999;
}

.notification-icon-wrapper i {
  font-size: 3rem;
}

.notification-icon-wrapper:hover {
  transition: 0.3s ease;
  box-shadow: 0rem 0.4rem 0.4rem 0.1rem rgba(60, 60, 60, 0.5);
}
</style>

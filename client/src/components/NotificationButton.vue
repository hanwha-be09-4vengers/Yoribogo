<template>
  <div class="notification-btn">
    <div class="notification-icon-wrapper" @click="toggleMenu">
      <i class="fa-solid fa-bell"></i>
    </div>
    <ul class="notification-list" v-show="isMenuVisible">
      <!-- 알림 목록을 반복해서 보여줌 -->
      <li class="notification" v-for="notification in notifications" :key="notification.id">
        <span>{{ notification.notificationContent }}</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { connectSSE } from '@/api/sserequest'

const notifications = ref([]); // 알림 리스트
const isMenuVisible = ref(false); // 알림 창 보임 여부

// SSE 연결 함수 호출
const eventSource = connectSSE();

// SSE에서 'notification' 이벤트를 수신할 때마다 알림을 배열에 추가
eventSource.addEventListener('notification', (event) => {
  notifications.value.push({
    id: Date.now(), // 고유 ID 생성 (시간 기반)
    notificationContent: event.data, // 서버로부터 받은 알림 데이터
  });
});

// 알림 메뉴를 열고 닫는 함수
const toggleMenu = () => {
  isMenuVisible.value = !isMenuVisible.value; // 클릭 시 토글
}
</script>

<style scoped>
.notification-btn {
  display: flex;
  flex-direction: column;
  width: 25rem;
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

.notification-list {
  display: flex;
  flex-direction: column;
  background-color: var(--white-color);
  width: 28rem;
  padding: 1rem;
  border-radius: 1.2rem;
  box-shadow: 0.1rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  z-index: 9999;
}

.notification {
  display: flex;
  align-items: center;
  width: 100%;
  height: 5rem;
  padding-left: 1rem;
  gap: 1rem;
  cursor: pointer;
  border: none;
  border-radius: 1rem;
  font-size: 1.6rem;
  font-weight: 500;
  color: var(--black-color);
}

.notification span {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notification:hover {
  background-color: var(--light-pink-color);
}
</style>

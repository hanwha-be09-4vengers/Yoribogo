<template>
  <div class="notification-btn">
    <div class="notification-icon-wrapper" @click="toggleMenu">
      <i class="fa-solid fa-bell"></i>
      <!-- 읽지 않은 알림의 개수 표시 -->
      <span v-if="unreadCount > 0" class="notification-count">{{ unreadCount }}</span>
    </div>

    <!-- 모달창을 조건부로 렌더링하고 notifications 데이터를 전달 -->
    <NotificationModal v-if="isMenuVisible" 
                       @close="toggleMenu" 
                       :notifications="notifications" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import NotificationModal from './NotificationModal.vue'
import { fetchNotifications } from '@/api/userNotification'
import { connectSSE } from '@/api/sserequest';  // SSE 연결을 위한 모듈 가져오기

const isMenuVisible = ref(false);
const notifications = ref([]);  // 알림 데이터를 저장할 상태

// 읽지 않은 알림의 개수를 계산하는 computed
const unreadCount = computed(() => {
  return notifications.value.filter(notification => notification.notificationStatus === 'UNREAD').length;
});

// SSE 연결 설정 (실시간 알림 처리)
const eventSource = connectSSE();

eventSource.addEventListener('notification', (event) => {
  const notification = JSON.parse(event.data);
  notifications.value.unshift({
    notificationId: notification.notificationId,
    notificationContent: notification.notificationContent,
    notificationStatus: notification.notificationStatus,
    notificationCreatedAt: notification.notificationCreatedAt,
    notificationReadAt: notification.notificationReadAt
  });
});

// 모달 열림/닫힘 토글
const toggleMenu = async () => {
  isMenuVisible.value = !isMenuVisible.value;

  if (isMenuVisible.value) {
    await loadNotifications();  // 모달이 열릴 때만 API 호출
  }
};

// 서버로부터 알림 데이터를 받아오는 함수
const loadNotifications = async () => {
  const token = localStorage.getItem('token');  // 로컬 스토리지에서 token 가져오기
  if (!token) {
    console.error('Token is not found in localStorage');
    return;
  }

  try {
    // token을 JSON으로 파싱
    const parsedToken = JSON.parse(token);
    const userId = parsedToken.userId;  // userId 추출

    if (!userId) {
      console.error('User ID is not found in token');
      return;
    }

    // API 요청 보내기
    const response = await fetch(`/api/notifications/send/${userId}`);
    if (response.ok) {
      const data = await response.json();
      notifications.value = data.data;  // 서버에서 받은 알림 데이터를 저장
    } else {
      console.error('Failed to fetch notifications');
    }
  } catch (error) {
    console.error('Error parsing token or fetching notifications:', error);
  }
};
</script>

<style scoped>
.notification-btn {
  position: relative;
  display: inline-block;
  width: 6.3rem;
  height: fit-content;
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

.notification-count {
  position: absolute;
  top: -0.5rem;
  right: -0.5rem;
  background-color: red;
  color: white;
  border-radius: 50%;
  padding: 0.2rem 0.6rem;
  font-size: 1.4rem;
  font-weight: bold;
  min-width: 2rem;
  height: 2rem;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
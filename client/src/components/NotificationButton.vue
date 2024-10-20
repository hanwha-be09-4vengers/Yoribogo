<template>
  <div class="notification-btn">
    <!-- 알림 아이콘과 카운트가 같은 wrapper 안에 있음 -->
    <div class="notification-icon-wrapper" @click="toggleMenu">
      <i class="fa-solid fa-bell"></i>
      <span v-if="unreadCount > 0" class="notification-count">{{ unreadCount }}</span>
    </div>
    <div class="notification-modal" v-show="isMenuVisible">
      <ul class="notification-list">
        <li class="notification" v-for="notification in notifications" :key="notification.notificationId"
            :class="{ 'unread': notification.notificationStatus === 'UNREAD' }">
          <span>{{ notification.notificationContent }}</span>
          <span class="notification-date">{{ formatDate(notification.notificationCreatedAt) }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { connectSSE } from '@/api/sserequest'

const notifications = ref([]);
const isMenuVisible = ref(false);

const unreadCount = computed(() => {
  return notifications.value.filter(notification => notification.notificationStatus === 'UNREAD').length;
});

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

const toggleMenu = () => {
  isMenuVisible.value = !isMenuVisible.value;
}

const formatDate = (dateString) => {
  const date = new Date(dateString);
  return date.toLocaleString();
}
</script>

<style scoped>
.notification-btn {
  position: relative;
  display: inline-block;
  width: 30em;
  height: fit-content;
}

.notification-icon-wrapper {
  position: relative; /* 자식 요소를 위한 상대 위치 설정 */
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

/* 카운트를 아이콘의 상단 우측에 고정 */
.notification-count {
  position: absolute;
  top: -0.5rem; /* 아이콘 위쪽으로 살짝 띄우기 */
  right: -0.5rem; /* 오른쪽으로 살짝 이동 */
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

.notification-modal {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: var(--white-color);
  width: 28rem;
  border-radius: 1.2rem;
  box-shadow: 0.1rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  z-index: 9998;
  max-height: 40rem;
  overflow-y: auto;
}

.notification-list {
  padding: 1rem;
  margin: 0;
  list-style-type: none;
}

.notification {
  display: flex;
  flex-direction: column;
  padding: 1rem;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.notification:last-child {
  border-bottom: none;
}

.notification.unread {
  font-weight: bold;
  background-color: #f0f0f0;
}

.notification span {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.notification-date {
  font-size: 1.2rem;
  color: #666;
  margin-top: 0.5rem;
}

.notification:hover {
  background-color: var(--light-pink-color);
}
</style>

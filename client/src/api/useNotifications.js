// 알림 기능에 대한 모든 함수를 모아놓음 @/api/userNotification.js
import axios from 'axios'

// 알림 상태 업데이트 API 호출
export function updateNotificationStatus(notificationId, status) {
  return axios.put(`http://localhost:8080/api/notification/updateStatus/${notificationId}`, { status })
}

// 알림 목록을 가져오는 API (필요에 따라 호출 가능)
export function fetchNotifications() {
  return axios.get('http://localhost:8080/api/notification/list') // 필요 시 경로 변경
}

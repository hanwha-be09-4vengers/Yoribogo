let eventSource = null
// import { useTokenStore } from '@/stores/tokenStore'; // Pinia 스토어 임포트
// const tokenStore = useTokenStore(); // Pinia 스토어 사용

export function connectSSE() {
  if (!eventSource) {
    // eventSource = new EventSource('/api/notifications/sseconnect');
    eventSource = new EventSource(`/api/notifications/sseconnect`, {
      headers: {
        Authorization: `Bearer ${JSON.parse(localStorage.getItem('token')).accessToken}`
      },
      withCredentials: true
    })

    // 알림 이벤트 수신
    eventSource.addEventListener('notification', (event) => {
      console.log('새로운 알림:', event.data)
    })

    // 에러 처리
    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error)
    }
  }

  return eventSource
}

export function closeSSE() {
  if (eventSource) {
    eventSource.close()
    eventSource = null
  }
}

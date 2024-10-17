import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어에서 토큰 가져오기
import { EventSourcePolyfill } from 'event-source-polyfill';

export function connectSSE() {
  const tokenStore = useTokenStore();
  const accessToken = tokenStore.token.accessToken;

  if (!accessToken) {
    console.error('토큰이 없습니다. SSE 연결을 할 수 없습니다.');
    return;
  }

  // EventSourcePolyfill을 통해 JWT를 Authorization 헤더에 담아 SSE 연결
  const eventSource = new EventSourcePolyfill('/api/notifications', {
    headers: {
      'Authorization': `Bearer ${accessToken}`  // JWT를 Authorization 헤더에 포함
    }
  });

  // 연결 완료 로그
  eventSource.onopen = () => {
    console.log('SSE 연결이 완료되었습니다.');
  };

  // 메시지 수신 처리
  eventSource.onmessage = (event) => {
    console.log('알림 수신:', event.data);
    // 여기서 수신한 메시지(event.data)를 적절히 처리하면 돼
  };

  // 오류 처리
  eventSource.onerror = (error) => {
    console.error('SSE 연결 오류:', error);
    eventSource.close();
  };

  return eventSource;
}

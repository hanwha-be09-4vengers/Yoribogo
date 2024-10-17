import { useTokenStore } from '@/stores/tokenStore';  // Pinia 스토어에서 토큰 가져오기
import { EventSourcePolyfill } from 'event-source-polyfill';

export function connectSSE() {
  const tokenStore = useTokenStore();
  const accessToken = tokenStore.token.accessToken;
  // 리프레시 토큰 고려하여 리펙토링 필요 ( 사용자 재로그인 시도 하지 않도록 )

  if (!accessToken) {
    console.error('토큰이 없습니다. SSE 연결을 할 수 없습니다.');
    return;
  }

  // EventSourcePolyfill을 통해 JWT를 Authorization 헤더에 담아 SSE 연결
  const eventSource = new EventSourcePolyfill('/api/notifications', {
    headers: {
       // EventSourcePolyfill을 사용하여 JWT를 Authorization 헤더에 포함
      'Authorization': `Bearer ${accessToken}` 
    }
  });

  // 연결 완료 로그
  eventSource.onopen = () => {
    console.log('SSE 연결이 완료되었습니다.');
  };

  // 서버에서 스케쥴러를 통해 이벤트 객체 생성 시 자동으로 호출
  eventSource.onmessage = (event) => {
    console.log('알림 수신:', event.data);
    // 여기서 수신한 메시지(event.data)를 UI를 고려하여 처리
  };

  // 오류 처리
  eventSource.onerror = (error) => {
    console.error('SSE 연결 오류:', error);
    eventSource.close();
  };

  return eventSource;
}

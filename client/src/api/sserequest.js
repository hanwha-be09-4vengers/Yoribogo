export function connectSSE() {
    const eventSource = new EventSource('/api/notification/sseconnect');
  
    // SSE 연결 시 나오는 메세지 
    eventSource.onopen = () => {
      console.log('SSE 연결이 열렸습니다.');
    };
  
    // SSE 연결 시 서버가 최초로 보내는 메세지 
    eventSource.addEventListener('connect', (event) => {
      console.log('서버로부터 연결 메시지:', event.data);
    });

    eventSource.onmessage = (event) => {
      console.log('새로운 알림:', event.data);
      // 여기에서 알림을 처리하는 로직을 추가할 수 있습니다.
    };
  
    eventSource.onerror = (error) => {
      console.error('SSE 연결 오류:', error);
      eventSource.close();
    };
  
    return eventSource;
}
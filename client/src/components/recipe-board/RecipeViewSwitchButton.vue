<template>
    <div class="toggle-container">
        <div
        :class="['toggle-button', isWeekly ? 'active' : '']"
        @click="toggleToWeekly"
        >
        주간 인기 레시피
        </div>
        <div
        :class="['toggle-button', !isWeekly ? 'active' : '']"
        @click="toggleToAll"
        >
        전체 레시피
        </div>
    </div>

    <!-- 토글된 상태에 따른 목록 표시 -->
    <div v-if="isWeekly">
        <!-- 주간 인기 레시피 목록 -->
        <p>주간 인기 레시피 목록을 보여줍니다.</p>
    </div>
    <div v-else>
        <!-- 전체 레시피 목록 -->
        <p>전체 레시피 목록을 보여줍니다.</p>
    </div>
</template>

<script>
    export default {
    data() {
        return {
        isWeekly: true, // 초기 상태를 주간 인기 레시피로 설정
        };
    },
    methods: {
        toggleToWeekly() {
        this.isWeekly = true;
        },
        toggleToAll() {
        this.isWeekly = false;
        },
    },
    };
</script>

<style scoped>
    .toggle-container {
    display: flex;
    justify-content: space-between;
    max-width: 40%;
    margin: 0 auto;
    border-radius: 50px;
    background-color: #e0e0e0; /* 회색 배경 */
    padding: 0.5rem;
    position: relative;
    width: 100%;
    box-sizing: border-box;
    margin-top: 2rem;
    }

    .toggle-button {
    flex: 1;
    text-align: center;
    padding: 1rem 0;
    border-radius: 50px;
    cursor: pointer;
    font-size: 2.3rem;
    color: white;
    transition: background-color 0.3s ease;
    width: 100%; /* 버튼의 가로 길이를 100%로 설정 */
    }

    .toggle-button.active {
    background-color: #374151; /* 활성화된 버튼의 배경색 */
    }

    .toggle-button:not(.active) {
    background-color: transparent;
    /* color: #374151;  */
    /* 비활성화된 버튼의 글자색 */
    color: #858585;
    }

    /* 반응형: 작은 화면일 때 */
    @media screen and (max-width: 950px) {
    .toggle-container {
        max-width: 60%;
        padding: 0.5rem;
    }

    .toggle-button {
        font-size: 2rem;
        padding: 1rem 0;
        width: 100%; /* 버튼 가로 길이도 줄어듦 */
    }
    }
</style>
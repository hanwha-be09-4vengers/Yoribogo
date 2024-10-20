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
        <div class="recipe-grid">
            <div class="recipe-card" v-for="(recipe, index) in weeklyRecipes" :key="index">
            <img :src="recipe.image" alt="레시피 이미지" class="recipe-image" />
            <p class="recipe-title">{{ recipe.title }}</p>
            </div>
        </div>
        </div>
        <div v-else>
        <!-- 전체 레시피 목록 -->
        <div class="recipe-grid">
            <div class="recipe-card" v-for="(recipe, index) in allRecipes" :key="index">
            <img :src="recipe.image" alt="레시피 이미지" class="recipe-image" />
            <p class="recipe-title">{{ recipe.title }}</p>
            </div>
        </div>
        </div>
    </template>
    
    <script setup>
    import { ref } from 'vue';
    
    // 상태 관리: 주간 인기 레시피인지 전체 레시피인지 여부
    const isWeekly = ref(true);
    
    // 주간 인기 레시피 목록
    const weeklyRecipes = ref([
        { title: "샐러드", image: "/path/to/image1.jpg" },
        { title: "매운요거트 토마토샐러드", image: "/path/to/image2.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" },
        { title: "해산물샐러드와 미나리소스", image: "/path/to/image3.jpg" }
    ]);
    
    // 전체 레시피 목록
    const allRecipes = ref([
        { title: "맛살 미역줄기전", image: "/path/to/image4.jpg" },
        { title: "황금팽이 비빔국수", image: "/path/to/image5.jpg" },
        { title: "양파토마토스튜", image: "/path/to/image6.jpg" },
        // 추가 레시피...
    ]);
    
    // 주간 인기 레시피 토글 함수
    const toggleToWeekly = () => {
        isWeekly.value = true;
    };
    
    // 전체 레시피 토글 함수
    const toggleToAll = () => {
        isWeekly.value = false;
    };
    </script>
    
    <style scoped>
    /* 그리드 컨테이너 스타일 */
    .recipe-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr); /* 한 줄에 3개씩 */
        gap: 1rem; /* 카드 간격 */
        padding: 1rem;
    }
    
    /* 레시피 카드 스타일 */
    .recipe-card {
        background-color: #f9f9f9;
        border-radius: 10px;
        text-align: center;
        padding: 1rem;
        box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
    }
    
    /* 레시피 이미지 스타일 */
    .recipe-image {
        width: 100%;
        height: auto;
        border-radius: 10px;
    }
    
    /* 레시피 제목 스타일 */
    .recipe-title {
        margin-top: 0.5rem;
        font-size: 1.5rem;
        color: #333;
    }
    
    /* 반응형 디자인 */
    @media screen and (max-width: 950px) {
        .recipe-grid {
        grid-template-columns: repeat(2, 1fr); /* 작은 화면에서는 2개씩 표시 */
        }
    }
    
    @media screen and (max-width: 600px) {
        .recipe-grid {
        grid-template-columns: 1fr; /* 더 작은 화면에서는 1개씩 표시 */
        }
    }
    
    .toggle-container {
        display: flex;
        justify-content: space-between;
        max-width: 40%;
        margin: 0 auto;
        border-radius: 50px;
        background-color: #e0e0e0;
        padding: 0.5rem;
        margin-top: 2rem;
        width: 50rem;
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
        text-wrap: nowrap;
    }
    
    .toggle-button.active {
        background-color: #374151;
    }
    
    .toggle-button:not(.active) {
        background-color: transparent;
        color: #858585;
        width: 30rem;
        padding: 1rem;
    }
        

    </style>
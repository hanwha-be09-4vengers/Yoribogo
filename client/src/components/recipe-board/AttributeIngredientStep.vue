    <template>
        <div class="ingredient-step-container">
            <!-- 재료 영역 -->
            <div class="section">
                <div class="section-header">재료</div>
                <div class="section-content" style="width: 100%;">
                    <span v-if="ingredients.length === 0">재료 항목이 비어있습니다.</span>
                    <div v-else v-for="(ingredient, index) in ingredients" :key="index" class="ingredient-item">
                        <div style="display: flex; justify-content: space-evenly; gap: 4px;">
                            &nbsp;
                            {{ ingredient }}
                            <button @click="removeItem(index)">✕</button>

                        </div>
                </div>
                </div>
            </div>
    
            <!-- 조리 순서 영역 -->
            <div class="section">
                <div class="section-header">조리 순서</div>
                <div class="section-content">
                    <span v-if="steps.length === 0">요리 방법 항목이 비어있습니다.</span>
                    <ul v-else>
                        <li v-for="(step, index) in steps" :key="index">{{ step }}</li>
                    </ul>
                </div>
            </div>
        </div>
    </template>
    
    <script setup>
    import { ref,  watch } from 'vue';
    
    // 재료와 조리 순서 배열 선언 (초기값이 없으면 비어있는 메시지 출력)
    const ingredients = ref([]);
    const steps = ref([]);

    const props = defineProps({
        ingredients: {
            type: Array,
            required: true
        }
    });




    // 재료 삭제 함수 
    const emit = defineEmits(['remove-item'])
    const removeItem = (index) => {
        emit('remove-item', index);
    };


    // props.ingredients가 변경될 때 ingredients를 업데이트
    watch(() => props.ingredients, (newIngredients) => {
        ingredients.value = newIngredients;
        console.log("변경된 배열" ,props.ingredients);
    });
    

    </script>
    
    <style scoped>
    .ingredient-step-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 10rem;
    }
    
    .section {
        width: 100%;
        max-width: 600px;
        display: flex;
        align-items: center;
        flex-direction: column;
    }
    
    .section-header {
        background-color: #2d3e50;
        color: white;
        padding: 1rem 1.3rem;
        font-size: 1.5rem;
        font-weight: 600;
        border-radius: 0.4rem;
        text-align: center;
        width: 80px;
    }
    
    .section-content {
        justify-content: center;
        align-items: center;
        padding: 0.7rem 1rem;
        border-radius: 50px;
        font-size: 1.2rem;
        color: #666;
        text-align: center;
        margin-top: 2rem;
    }


    /* 부모 div 크기를 넘지 않도록 */
    .section-content {
        display: flex;
        flex-wrap: wrap;
        gap: 1rem; /* 항목 간의 간격 */
    }

    /* 재료 담는 박스 */
    .ingredient-item{
        display: flex;
        align-items: center;
        padding: 0.4rem;
        border: 1px solid #1c2e3e; /* 테두리 색상 */
        border-radius: 2rem; /* pill 모양을 위해 radius를 높게 설정 */
        background-color: white;
        font-size: 1.6rem;
        color: #000;
        gap: 1rem; /* 텍스트와 버튼 사이의 간격 */

    }
    .ingredient-item button{
        display: flex;
        justify-content: center;
        align-items: center;
        width: 2.5rem;
        height: 2.5rem;
        background-color: #1c2e3e; /* X 버튼의 배경색 */
        color: white;
        border: none;
        border-radius: 50%; /* 원형 버튼 */
        cursor: pointer;
        font-size: 1.5rem;
        line-height: 1;
    }

    .section-content span{
        font-size: 1.7rem;
    }
    
    ul {
        padding: 0;
        list-style: none;
        margin: 0;
    }
    
    li {
        padding: 0.5rem 0;
        font-size: 1.3rem;
    }
    </style>
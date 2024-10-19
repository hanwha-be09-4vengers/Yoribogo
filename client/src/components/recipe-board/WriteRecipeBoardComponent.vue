<template>
  <div class="write-recipe-board" style="display: flex; flex-direction: column;">
    <div style="display: flex; ">
      <div class="attribute-board" :class="{ first: !isSmallScreen }">
        <AttributeMenuNameInput
          :name="'메뉴명'"
          :placeholder="'메뉴명을 입력하세요'"
          :show-button="false"
          v-model="menu_name"
        ></AttributeMenuNameInput>
        <AttributeImageInput
          :name="'대표 사진'"
          :placeholder="'여기에 이미지를 드래그하거나 클릭하여 추가'"
        ></AttributeImageInput>
        <AttributeIngredientInput
          :name="'재료'"
          :placeholder="'재료를 하나씩 입력하고 추가해주세요'"
          :show-button="true"
          :ingredients="ingredients" 
          @add-item="addIngredient"
        ></AttributeIngredientInput>
        <AtrributeTextAndImageInput
          :name="'조리 순서'"
          :placeholder="'단계별 요리법을 작성해주세요'"
          @add="addStep"
        ></AtrributeTextAndImageInput>
      </div>

      <div class="attribute-board second" v-if="!isSmallScreen">
        <AttributeIngredientStep 
        :ingredients="ingredients" 
        :manual_step="manual_step"
        @remove-item="removeIngredient"
        ></AttributeIngredientStep>
      </div>

    </div>


    <!-- 등록버튼 -->
    <div class="submit-button-container">
      <button class="submit-button" @click="submitRecipe">등록하기</button>
    </div>

  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted } from 'vue';
import AttributeTextInput from '@/components/recipe-board/AttributeMenuNameInput.vue';
import AttributeImageInput from '@/components/recipe-board/AtrributeImageInput.vue';
import AtrributeTextAndImageInput from '@/components/recipe-board/AtrributeTextAndImageInput.vue';
import AttributeIngredientStep from './AttributeIngredientStep.vue';
import AttributeMenuNameInput from '@/components/recipe-board/AttributeMenuNameInput.vue';
import AttributeIngredientInput from './AttributeIngredientInput.vue';
import axios from 'axios';

const isSmallScreen = ref(window.innerWidth <= 1280);
const handleResize = () => {
  isSmallScreen.value = window.innerWidth <= 1280;
};

// 메뉴명, 대표사진, 재료, 조리순서 값 초기화
const menu_name = ref('');
const board_image = ref('');
const ingredients = ref(['']);
const manual_step = ref([]);


// 로컬스토리지에서 데이터를 불러오는 함수
const loadFromLocalStorage = () => {

    const storedMenuName = localStorage.getItem('menu_name');
    const storedIngredients = localStorage.getItem('ingredients');
    const storedSteps = localStorage.getItem('manual_step');

    if (storedMenuName) {
      menu_name.value = storedMenuName;
    }

    if (storedIngredients) {
      ingredients.value = JSON.parse(storedIngredients) || [];
    } 

    if (storedSteps) {
      manual_step.value = JSON.parse(storedSteps) || [];
    }
};




onMounted(() => {
  window.addEventListener('resize', handleResize);
  console.log("초기화된 배열")
  console.log(ingredients.value);

  loadFromLocalStorage(); // 페이지 로드 시 로컬 스토리지에서 데이터 불러오기

  console.log("로컬스토리지에서 가져온 배열")
    console.log('메뉴명', menu_name.value);
    console.log('재료들', ingredients.value);
    console.log('조리방법:', manual_step.value);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});

// 재료 추가 함수
const addIngredient = (ingredient) => {
  if (ingredient) {
    ingredients.value.push(ingredient);
    saveToLocalStorage(); // 추가 후 로컬 스토리지 업데이트
    console.log("addIngredient 메소드 작동")
    console.log(ingredients.value); // 여기서 즉시 출력

  }
};
//재료 삭제 함수
const removeIngredient = (index) => {
  ingredients.value.splice(index, 1);
  saveToLocalStorage();
  console.log("removeIngredient 메소드 작동")
  console.log(ingredients.value);
};

// 조리 순서 추가 함수
const addStep = ({ step, image }) => {
  if (step || image) {
    manual_step.value.push({ step, image });
    saveToLocalStorage(); // 추가 후 로컬 스토리지 업데이트
  }
};

// 로컬 스토리지에 저장하는 함수
const saveToLocalStorage = () => {
  try {
    localStorage.setItem('menu_name', menu_name.value);
    localStorage.setItem('ingredients', JSON.stringify(ingredients.value));
    localStorage.setItem('manual_step', JSON.stringify(manual_step.value));
  } catch (e) {
    console.error('Failed to save to localStorage:', e);
  }
};




</script>





<style scoped>
.write-recipe-board {
  display: flex;
  justify-content: space-between;
  width: 90%;
  height: 100rem;
  margin-top: 8rem;
  margin-bottom: 8rem;
}

.attribute-board {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 100%;
  gap: 3rem;
}

.first {
  width: 50%;
  /* align-items: flex-start; */
  border-right: 2px solid var(--light-gray-color);
}

.second {
  width: 50%;
  /* align-items: flex-end; */
}

.write-recipe-board {
  display: flex;
  flex-direction: column; /* 수직으로 배치 */
  justify-content: space-between; /* 공간 배분 */
  width: 90%;
  height: 100rem;
  margin-top: 8rem;
  margin-bottom: 8rem;
  gap: 3rem; /* 내용 간격 */
}

.submit-button-container{
  justify-content: center;
  align-items: center;
  display: flex;
}

.submit-button{
  background-color: var(--navy-color);
  padding: 1rem 2rem;
  border: transparent;
  border-radius: 3px;
  color: white;
}
</style>

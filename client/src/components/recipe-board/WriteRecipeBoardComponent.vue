<template>
  <div class="write-recipe-board" style="display: flex; flex-direction: column;">
    <div style="display: flex;">
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
          v-model="board_image"
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
          v-model="manual_step"
        ></AtrributeTextAndImageInput>
      </div>

      <div class="attribute-board second" v-if="!isSmallScreen">
        <AttributeIngredientStep
          :ingredients="ingredients"
          :manual_step="manual_step"
          @add-step="addStep"
          @remove-item="removeIngredient"
        ></AttributeIngredientStep>
      </div>
    </div>

    <!-- 등록버튼 -->
    <div class="submit-button-container">
      <button class="submit-button" @click="submitRecipe">등록하기</button>
    </div>

    <!-- 로딩 스피너 -->
    <LoadingSpinner v-if="loading" />
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted, watch } from 'vue';
import AttributeTextInput from '@/components/recipe-board/AttributeMenuNameInput.vue';
import AttributeImageInput from '@/components/recipe-board/AtrributeImageInput.vue';
import AtrributeTextAndImageInput from '@/components/recipe-board/AtrributeTextAndImageInput.vue';
import AttributeIngredientStep from './AttributeIngredientStep.vue';
import AttributeMenuNameInput from '@/components/recipe-board/AttributeMenuNameInput.vue';
import AttributeIngredientInput from './AttributeIngredientInput.vue';
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'; // 스피너 컴포넌트 추가
import axios from 'axios';
import { useTokenStore } from '@/stores/tokenStore';

const isSmallScreen = ref(window.innerWidth <= 1280);
const handleResize = () => {
  isSmallScreen.value = window.innerWidth <= 1280;
};

// 메뉴명, 대표사진, 재료, 조리순서 값 초기화
const menu_name = ref('');
const board_image = ref('');
const ingredients = ref([]);
const manual_step = ref([]);
const loading = ref(false); // 로딩 상태 관리

// 로컬스토리지에서 데이터를 불러오는 함수
const loadFromLocalStorage = () => {
  const storedMenuName = localStorage.getItem('menu_name');
  const storedFileName = localStorage.getItem('board_image');
  const storedIngredients = localStorage.getItem('ingredients');
  const storedSteps = localStorage.getItem('manual_step');

  if (storedMenuName) {
    menu_name.value = storedMenuName;
  }
  if (storedFileName) {
    board_image.value = storedFileName;
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
  loadFromLocalStorage(); // 페이지 로드 시 로컬 스토리지에서 데이터 불러오기
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});

// 메뉴가 변경되는 경우 로컬스토리지 변경사항 저장
watch(menu_name, (newValue) => {
  localStorage.setItem('menu_name', newValue);
});

// 대표사진이 변경되는 경우 로컬스토리지 변경사항 저장
watch(board_image, (newValue) => {
  localStorage.setItem('board_image', newValue);
});

// 배열에 재료 추가 함수
const addIngredient = (ingredient) => {
  if (ingredient && !ingredients.value.includes(ingredient)) {
    ingredients.value = [...ingredients.value, ingredient]; // 새 배열로 재할당하여 반응성 유지
    saveToLocalStorage(); // 추가 후 로컬 스토리지 업데이트
  } else {
    alert("재료를 다시 확인하세요.");
  }
};

//배열에 재료 삭제 함수
const removeIngredient = (index) => {
  ingredients.value = ingredients.value.filter((_, i) => i !== index); // 새로운 배열 할당
  saveToLocalStorage();
};

// 배열에 조리 방법 추가 함수
const addStep = (newStep) => {
  console.log("새로운 단계 데이터:", newStep); // newStep 객체 확인
  if (newStep && newStep.step) {
    manual_step.value = [...manual_step.value, { step: newStep.step, image: newStep.image }];
    console.log("업데이트된 manual_step 배열:", manual_step.value);
    saveToLocalStorage(); // 추가 후 로컬 스토리지 업데이트
  } else {
    alert("단계 내용을 확인하세요.");
  }
};

// 로컬 스토리지에 저장하는 함수
const saveToLocalStorage = () => {
  try {
    localStorage.setItem('menu_name', menu_name.value);
    localStorage.setItem('board_image', board_image.value);
    localStorage.setItem('ingredients', JSON.stringify(ingredients.value));
    localStorage.setItem('manual_step', JSON.stringify(manual_step.value));
  } catch (e) {
    console.error('Failed to save to localStorage:', e);
  }
};

const tokenStore = useTokenStore();

// 레시피 등록 요청
const submitRecipe = async () => {
  try {
    loading.value = true; // 로딩 시작

    const formData = new FormData();
    formData.append('menu_name', menu_name.value);
    formData.append('ingredients', ingredients.value.join(', '));
    formData.append('user_id', tokenStore.token.userId);

    // 이미지 파일이 제대로 들어가도록 수정
    if (board_image.value && board_image.value instanceof File) {
      formData.append('boardImage', board_image.value); // 이미지 파일 추가
    } else {
      console.error('board_image is not a File instance');
    }

    // 게시글 등록
    const boardResponse = await axios.post('/api/recipe-board/create', formData);

    // 매뉴얼 데이터 확인 로그 추가
    const manualData = manual_step.value.map((step, index) => ({
      manual_step: index + 1,
      manual_content: step.step,
      manual_image: step.image instanceof File ? step.image : null, // 이미지가 파일일 경우에만 추가
    }));

    console.log("전송할 매뉴얼 데이터:", manualData);

    // 매뉴얼 등록
    if (manualData.length > 0) {
      const manualFormData = new FormData();
      manualData.forEach((manual, index) => {
        manualFormData.append('manual_steps', manual.manual_step);
        manualFormData.append('manual_contents', manual.manual_content);
        if (manual.manual_image) {
          manualFormData.append('manual_images', manual.manual_image); // 이미지 파일 추가
        }
      });

      await axios.post(`/api/recipe-board/create/manual/${boardResponse.data.data.board_id}`, manualFormData);
    }

    alert('레시피가 성공적으로 등록되었습니다.');
  } catch (error) {
    console.error('레시피 등록 실패:', error);
    alert('레시피 등록에 실패했습니다.');
  } finally {
    loading.value = false; // 로딩 종료
  }
};
</script>

<style scoped>
.write-recipe-board {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 90%;
  height: auto;
  margin-top: 8rem;
  margin-bottom: 8rem;
  gap: 3rem;
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
  border-right: 2px solid var(--light-gray-color);
}

.second {
  width: 50%;
}

.submit-button-container {
  justify-content: center;
  align-items: center;
  display: flex;
}

.submit-button {
  background-color: var(--navy-color);
  padding: 1rem 2rem;
  border: transparent;
  border-radius: 3px;
  color: white;
}

/* 로딩 스피너 스타일 */
.loading-container {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>

<template>
  <div class="write-recipe-board">
    <div class="attribute-board" :class="{ first: !isSmallScreen }">
      <AttributeTextInput
        :name="'메뉴명'"
        :placeholder="'메뉴명을 입력하세요'"
      ></AttributeTextInput>
      <AttributeImageInput
        :name="'대표 사진'"
        :placeholder="'여기에 이미지를 드래그하거나 클릭하여 추가'"
      ></AttributeImageInput>
      <AttributeTextInput
        :name="'재료'"
        :placeholder="'재료를 하나씩 입력하고 추가해주세요'"
      ></AttributeTextInput>
      <AtrributeTextAndImageInput
        :name="'조리 순서'"
        :placeholder="'단계별 요리법을 작성해주세요'"
      ></AtrributeTextAndImageInput>
    </div>
    <div class="attribute-board second" v-if="!isSmallScreen"></div>
  </div>
</template>

<script setup>
import { ref, onBeforeUnmount, onMounted } from 'vue'
import AttributeTextInput from '@/components/recipe-board/AttributeTextInput.vue'
import AttributeImageInput from '@/components/recipe-board/AtrributeImageInput.vue'
import AtrributeTextAndImageInput from '@/components/recipe-board/AtrributeTextAndImageInput.vue'

const isSmallScreen = ref(window.innerWidth <= 1280)

const handleResize = () => {
  isSmallScreen.value = window.innerWidth <= 1280
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
})
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
  align-items: flex-start;
  border-right: 2px solid var(--light-gray-color);
}

.second {
  width: 50%;
  align-items: flex-end;
}
</style>

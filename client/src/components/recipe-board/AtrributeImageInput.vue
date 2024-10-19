<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
    </div>
    <div class="attribute-input-wrapper" @dragover.prevent @drop.prevent="handleFileDrop">
      <label for="file-upload" class="custom-file-upload">
        <i v-if="!uploadedFileName" class="fa-solid fa-image"></i>
        <!-- 이미지 미리보기 -->
        <div v-if="uploadedImageUrl" style="display: flex; justify-content: center; align-items: center;">
          <img :src="uploadedImageUrl" alt="미리보기 이미지" style="max-width: 50rem; max-height: 30rem;"/>
        </div>
        <span v-if="!uploadedFileName">{{ props.placeholder }}</span>
        <span v-else>{{ uploadedFileName }}</span>
      </label>
      <input id="file-upload" type="file" @change="handleFileChange" />


        
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const uploadedFileName = ref('') // 파일 이름을 저장할 변수
const uploadedImageUrl = ref('') // 이미지 URL 저장

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  },
  modelValue: { // v-model을 사용하려면 modelValue prop을 받아야 함
    type: [String, Object], // 파일 또는 문자열을 허용 (상황에 따라)
    default: ''
  }
})

const emit = defineEmits(['update:modelValue']); // 'add' 이벤트 발생

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    // uploadedFileName.value = file.name // 선택된 파일 이름을 저장
    // emit('add', { image: file }); // 파일을 상위 컴포넌트로 전달 (optional)
    uploadedFileName.value = file.name // 파일 이름 저장
    uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성

    console.log("파일 변경이 감지됨 by click", file.name);

    emit('update:modelValue', file); 
  }
}

const handleFileDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file && file.type.startsWith('image/')) {
    // uploadedFileName.value = file.name // 드롭된 파일 이름을 저장
    // emit('add', { image: file }); // 파일을 상위 컴포넌트로 전달 (optional)

    uploadedFileName.value = file.name // 파일 이름 저장
    uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성

    console.log("파일 변경이 감지됨 by drop", file.name)

    emit('update:modelValue', file); 
  }
}
</script>



<style scoped>
.attribute-input-container {
  display: flex;
  flex-direction: column;
  width: 90%;
  height: fit-content;
}

.attribute-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 9rem;
  height: 3rem;
  align-items: center;
  background-color: var(--navy-color);
  color: var(--white-color);
  border-radius: 0.8rem 0.8rem 0rem 0;
  font-size: 1.5rem;
  font-weight: 500;
}

.attribute-input-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 30rem;
  background-color: var(--light-gray-color);
  border-radius: 0 0.8rem 0.8rem 0.8rem;
  border: 2px dashed var(--gray-color);
}

.attribute-input-wrapper i {
  font-size: 4rem;
  text-align: center;
}

.custom-file-upload {
  display: flex;
  flex-direction: column;
  padding: 0.5rem 1rem;
  gap: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

.attribute-input-wrapper input[type='file'] {
  display: none;
}

.custom-file-upload span{
  color: gray;
}
.fa-solid{
  color: gray;
}
</style>

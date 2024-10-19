<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
    </div>
    <div class="attribute-input-wrapper" @dragover.prevent @drop.prevent="handleFileDrop">
      <label for="file-upload" class="custom-file-upload">
        <i class="fa-solid fa-image"></i>
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

const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['add']); // 'add' 이벤트 발생

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    uploadedFileName.value = file.name // 선택된 파일 이름을 저장
    emit('add', { image: file }); // 파일을 상위 컴포넌트로 전달 (optional)
  }
}

const handleFileDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file) {
    uploadedFileName.value = file.name // 드롭된 파일 이름을 저장
    emit('add', { image: file }); // 파일을 상위 컴포넌트로 전달 (optional)
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

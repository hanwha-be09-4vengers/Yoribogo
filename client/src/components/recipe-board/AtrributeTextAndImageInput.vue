<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
    </div>
    <div class="attribute-input-wrapper">
      <div class="text-input">
        <input type="text" :placeholder="props.placeholder" />
      </div>
      <div
        class="image-input"
        @dragover.prevent
        @dragleave="handleDragLeave"
        @drop.prevent="handleFileDrop"
        @dragenter="handleDragEnter"
      >
        <label for="file-upload" class="custom-file-upload">
          <i class="fa-solid fa-image"></i>
          <span v-if="!fileName">{{ props.placeholder }}</span>
          <span v-else>{{ fileName }}</span>
          <!-- 파일 이름 표시 -->
        </label>
        <input id="file-upload" type="file" @change="handleFileChange" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isDragging = ref(false)
const fileName = ref('') // 파일 이름을 저장할 변수

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

const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    fileName.value = file.name // 선택된 파일 이름 저장
  }
}

const handleFileDrop = (event) => {
  const file = event.dataTransfer.files[0]
  if (file) {
    fileName.value = file.name // 드롭된 파일 이름 저장
  }
  isDragging.value = false
}

const handleDragEnter = () => {
  isDragging.value = true
}

const handleDragLeave = () => {
  isDragging.value = false
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
}

.text-input {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  height: 70%;
  border-right: 1px solid #dcdcdc;
}

.image-input {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  transition: background-color 0.3s ease;
}

/* 드래그 중일 때 배경색 변경 */
.image-input.dragging {
  background-color: var(--primary-color-light);
  border: 2px dashed var(--primary-color);
}

.text-input input {
  width: 100%;
  background-color: transparent;
  border: none;
  outline: none;
  padding-left: 3rem;
  font-size: 1.5rem;
  text-align: center;
}

.image-input i {
  font-size: 4rem;
  text-align: center;
}

.custom-file-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0.5rem 1rem;
  gap: 1rem;
  cursor: pointer;
  font-size: 1.5rem;
}

.image-input input[type='file'] {
  display: none;
}

@media screen and (max-width: 768px) {
  .text-input input {
    font-size: 1.4rem;
  }

  .custom-file-upload {
    font-size: 1.4rem;
  }
}

@media screen and (max-width: 425px) {
  .text-input input {
    font-size: 1.25rem;
  }

  .custom-file-upload {
    font-size: 1.25rem;
  }
}

@media screen and (max-width: 375px) {
  .text-input input {
    font-size: 1.2rem;
  }

  .custom-file-upload {
    font-size: 1.2rem;
  }
}
</style>

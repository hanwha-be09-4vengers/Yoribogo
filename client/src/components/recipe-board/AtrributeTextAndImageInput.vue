<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
      <button @click="handleAdd"> 추가 + </button>
    </div>

    <div class="attribute-input-wrapper">
      <div
        class="image-input"
        @dragover.prevent
        @dragleave="handleDragLeave"
        @drop.prevent="handleFileDrop"
        @dragenter="handleDragEnter"
      >
        <label for="file-upload-step" class="custom-file-upload">
          <i v-if="!uploadedFileName" class="fa-solid fa-image"></i>
          <!-- 이미지 미리보기 -->
          <div v-if="uploadedImageUrl" style="display: flex; justify-content: center; align-items: center;">
            <img :src="uploadedImageUrl" alt="미리보기 이미지" style="max-height: 15rem;"/>
          </div>
          <span v-if="!uploadedFileName">
            <!-- {{ "여기에 이미지를 드래그하거나 클릭하여 추가"}} -->
            {{ props.name === '조리 순서' ? '여기에 이미지를 드래그하거나 클릭하여 추가' : props.placeholder }}
          </span>
          <span v-else>{{ uploadedFileName }}</span>
        </label>
        <input id="file-upload-step" type="file" @change="handleFileChange" />
      </div>
      <div class="text-input">
        <input type="text" :placeholder="props.placeholder" v-model="stepText" />
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref, defineProps, defineEmits } from 'vue';

  const uploadedFileName = ref('') // 파일 이름을 저장할 변수
  const uploadedImageUrl = ref('') // 이미지 URL 저장


  // const imageUploadName = ref(''); // 파일 이름을 저장할 변수
  const stepText = ref('');


  const props = defineProps({
    name: {
      type: String,
      required: true,
    },
    placeholder: {
      type: String,
      required: true,
    },
    modelValue: { // v-model을 사용하려면 modelValue prop을 받아야 함
    type: [String, Object], // 파일 또는 문자열을 허용 (상황에 따라)
    default: ''
    }
  });

  const emit = defineEmits(['update:modelValue', 'add']);


  const handleFileChange = (event) => {
  const file = event.target.files[0];
  console.log("파일 선택 이벤트 발생:", file); // 로그 추가

  if (file && file.type.startsWith('image/')) {
    uploadedFileName.value = file.name // 파일 이름 저장
    uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성

    console.log("파일 변경이 감지됨 by click: 조리방법", file.name);
  }
}


  const handleFileDrop = (event) => {
    const file = event.dataTransfer.files[0];
    if (file && file.type.startsWith('image/')) {
      uploadedFileName.value = file.name // 파일 이름 저장
      uploadedImageUrl.value = URL.createObjectURL(file) // 이미지 미리보기 URL 생성

    console.log("파일 변경이 감지됨 by drop: 조리방법", file.name);

    // emit('update:modelValue', file); 

    }
  };



// "추가" 버튼 클릭 시 호출할 함수
const handleAdd = () => {
  if (stepText.value) { // stepText가 반드시 있어야 함
    const newStep = {
      step: stepText.value,
      image: uploadedFileName.value
    };


    // 상위 컴포넌트에 emit
    emit('add', newStep); // 전체 배열을 emit


    console.log("Added step:", newStep); // 추가된 내용 출력

    // 입력 필드 초기화
    stepText.value = ''; 
    uploadedFileName.value = ''; 
    uploadedImageUrl.value = '';
  } else {
    console.log("조리 방법을 입력해야 합니다."); // 조리 방법이 없을 경우 경고 메시지
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
  justify-content: space-between;
}

.attribute-name-wrapper span{
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
  flex-direction: column;
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
  height: 70%;
  width: 90%;
  border-top: 1px solid #DCDCDC;
  position: relative;
}

.image-input {
  display: flex;
    justify-content: center;
    align-items: center;
    transition: background-color 0.3s ease;
    position: relative;
    width: 100%;
    height: auto;
}

/* 이미지 아이콘 색 */
.fa-solid{
  color: gray;
}
.file-upload span{
  color: gray;
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

/* 추가 버튼 */
.attribute-name-wrapper button{
  margin-left: 1rem;
  padding: 0.5rem 1rem;
  background-color: var(--pink-color);
  color: white;
  border: none;
  border-radius: 0.5rem 0.5rem 0 0;
  cursor: pointer;
  top: 0px;
  right: 0px;
  font-size: 1.5rem;
}

.image-input i {
  font-size: 4rem;
  text-align: center;
}

.custom-file-upload {
  display: flex;
  justify-content: center;
    flex-direction: column;
    align-items: center;
    padding: 0.5rem 1rem;
    gap: 1rem;
    cursor: pointer;
    font-size: 1.4rem;
    height: 20rem;
}
.custom-file-upload span{
  color: gray;
}


.image-input input[type='file'] {
  display: none;
}

@media screen and (max-width: 768px) {
  .text-input input {
    font-size: 1.4rem;
  }

  .custom-file-upload span{
    color: gray;
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

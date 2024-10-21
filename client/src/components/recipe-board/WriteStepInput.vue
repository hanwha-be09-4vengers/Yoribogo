<template>
    <div class="step-input-container">
        
        <div class="step-content">
            <div class="step-title">
                {{ index + 1 }}단계
            </div>
            <div class="step-image">
                <!-- 이미지가 있는 경우 보여줌 -->
                <img v-if="stepImage" :src="uploadedImageUrl" alt="조리 이미지" />
                <input v-else type="file" @change="onImageChange" />
            </div>
            <div class="step-description">
                <input type="text" v-model="stepText" placeholder="조리 방법을 입력하세요" @input="emitStepUpdate" />
            </div>
            <button class="step-add-button" @click="addStepToNextIndex">조리 순서 추가</button>
        </div>
        
        <button class="step-remove-button" @click="removeStep">✕</button>
    </div>
</template>


<script setup>

    import { ref , onMounted, watch, onBeforeUnmount} from 'vue';



    // Props: 현재 단계 인덱스와 초기 데이터
    const props = defineProps({
    index: {
        type: Number,
        required: true
    },
    initialStep: {
        type: Object,
        required: true
    }
    });

    // Emits: 상위 컴포넌트에 변경 알림을 보낼 이벤트
    const emit = defineEmits(['add-step', 'remove-step', 'update-step']);

    // 데이터 바인딩
    const stepText = ref(props.initialStep.step);
    const stepImage = ref(props.initialStep.image);

    const uploadedImageUrl = ref('') 

    onMounted(() => {
    console.log(`현재 인덱스: ${props.index}`); // 현재 인덱스 확인
    const storedImage = localStorage.getItem(`step_image_${props.index}`);
    if (storedImage) {
        stepImage.value = storedImage;
        console.log(`로컬 스토리지에서 불러온 이미지: ${storedImage}`);
    } else {
        console.log(`로컬 스토리지에서 ${`step_image_${props.index}`} 키로 이미지가 없음`);
    }
});


const onImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
        const fileName = file.name; // 파일 이름 가져오기
        console.log('선택된 파일 이름:', fileName);

        // 미리보기 이미지 넣기 위한 URL
        uploadedImageUrl.value = URL.createObjectURL(file); // Blob URL 생성

        // manual_step의 image에 파일 이름을 저장
        const manualSteps = JSON.parse(localStorage.getItem('manual_step')) || [];
        manualSteps[props.index] = { step: stepText.value, image: fileName }; // 파일 이름 저장
        localStorage.setItem('manual_step', JSON.stringify(manualSteps)); // 로컬 스토리지 업데이트
        
        // 로컬 스토리지 상태 출력
        console.log('로컬 스토리지 업데이트:', manualSteps);
        
        // 추가로 stepImage.value를 업데이트하려면 다음을 추가
        stepImage.value = fileName; // fileName을 stepImage에 저장
    }
};




// 단계 정보를 업데이트하는 메소드
const updateLocalStorage = () => {
    const manualSteps = JSON.parse(localStorage.getItem('manual_step')) || [];
    manualSteps[props.index] = { step: stepText.value, image: stepImage.value };
    localStorage.setItem('manual_step', JSON.stringify(manualSteps));
    console.log('로컬 스토리지 업데이트:', manualSteps);
};


    // 텍스트와 이미지의 변화를 감시
    watch([stepText, stepImage], () => {
        console.log("변화 감지");
        updateLocalStorage(); // 값이 변경될 때마다 로컬 스토리지에 업데이트
    });




    // 단계 삭제
    const removeStep = (index) => {
    steps.value.splice(index, 1); // 해당 인덱스의 단계 삭제
    localStorage.setItem('manual_step', JSON.stringify(steps.value)); // 업데이트된 배열을 로컬 스토리지에 저장
    };

    // 조리 순서 추가 버튼이 클릭되면 활성화될 메소드
    const addStepToNextIndex = () => {
        // 빈 단계 객체 추가
        emit('add-step', { step: '', image: '' });
    }



</script>

<style scoped>
.step-input-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 2rem;
    border: 1px solid #dcdcdc;
    border-radius: 0.8rem;
    background-color: #fff;
    margin-bottom: 1rem;
    position: relative;
    width: 58rem;
}

.step-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    width: 100%;
}

.step-title {
    font-weight: bold;
    font-size: 1.6rem;
    text-align: center;
    color: #333;
}

.step-image {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 12rem;
    background-color: #f5f5f5;
    border-radius: 0.8rem;
    overflow: hidden;
}

.step-image img {
    max-width: 100%;
    max-height: 12rem;
    object-fit: cover;
}

.step-description {
    width: 100%;
    display: flex;
    justify-content: center;
}

.step-description input {
    width: 100%;
    padding: 1rem;
    font-size: 1.4rem;
    border: 1px solid #dcdcdc;
    border-radius: 0.8rem;
    text-align: center;
}

.step-add-button {
    width: 100%;
    padding: 1rem;
    font-size: 1.4rem;
    background-color: #243b55;
    color: #fff;
    border: none;
    border-radius: 0.8rem;
    cursor: pointer;
    text-align: center;
}

.step-add-button:hover {
    background-color: #1e2f44;
}

.step-remove-button {
    position: absolute;
    top: 1rem;
    right: 1rem;
    background-color: #ff4c4c;
    border: none;
    border-radius: 50%;
    width: 2.5rem;
    height: 2.5rem;
    color: white;
    cursor: pointer;
}

.step-remove-button:hover {
    background-color: #e84343;
}
</style>
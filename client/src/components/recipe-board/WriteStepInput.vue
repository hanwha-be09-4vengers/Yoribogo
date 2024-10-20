<template>
    <div class="step-input-container">
        
        <div class="step-content">
            <div class="step-title">
                {{ index + 1 }}단계
            </div>
            <div class="step-image">
                <!-- 이미지가 있는 경우 보여줌 -->
                <img v-if="stepImage" :src="stepImage" alt="조리 이미지" />
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

    import { ref , onMounted} from 'vue';

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
        stepImage.value = URL.createObjectURL(file); // 이미지 URL 생성
        console.log('생성된 Blob URL:', stepImage.value); // Blob URL 확인

        // manual_step의 image에 Blob URL을 저장
        const manualStep = JSON.parse(localStorage.getItem('manual_step')) || {}; // 기존의 manual_step 가져오기
        manualStep.image = stepImage.value; // 이미지 URL 저장
        localStorage.setItem('manual_step', JSON.stringify(manualStep)); // 로컬 스토리지에 업데이트
        
        // 저장 후 로컬 스토리지의 모든 키와 값을 출력
        console.log('로컬 스토리지 상태:', localStorage);
        console.log( "로컬스토리지 저장 확인",`step_image_${props.index}`, stepImage.value);
        console.log('stepImage.value:', stepImage.value); // Blob URL
        console.log('stepImage:', stepImage); // ref 객체
        
        emit('update-step', { step: stepText.value, image: stepImage.value }); // Emit으로 업데이트
    }
};
    // 조리 순서 추가
    const addStep = () => {
    emit('add-step', props.index);
    };

    // 조리 순서 삭제
    const removeStep = () => {
    emit('remove-step', props.index);
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
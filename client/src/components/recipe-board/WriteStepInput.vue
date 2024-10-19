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

    import { ref } from 'vue';

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

    /* 이벤트 발생 시 상위 컴포넌트로 데이터 emit */
    // 이미지 변경 시 호출
    const onImageChange = (e) => {
    const file = e.target.files[0];
    stepImage.value = URL.createObjectURL(file);
    emit('update-step', { step: stepText.value, image: stepImage.value });
    };

    // 조리 순서 추가
    const addStep = () => {
    emit('add-step', props.index);
    };

    // 조리 순서 삭제
    const removeStep = () => {
    emit('remove-step', props.index);
    };




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
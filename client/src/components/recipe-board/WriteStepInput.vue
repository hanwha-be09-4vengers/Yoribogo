<template>
    <div class="step-input-container">
        <div>
            {{ index }}단계
        </div>
        <div>
            <input type="file" @change="onImageChange" />
        </div>
        <div>
            <input type="text" v-model="stepText" placeholder="조리 방법을 입력하세요" />
        </div>
        <button @click="addStep">조리 순서 추가</button>
        <button @click="removeStep">삭제</button>
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
    flex-direction: column;
    gap: 10px;
    }


</style>
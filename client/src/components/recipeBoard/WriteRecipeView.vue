<template>
    <div class="write-recipe-view-container">
            <!-- 입력창 컨테이너 -->
        <div class="input-container" style="width: 50%;">
                <!-- 메뉴명 입력 필드 -->
            <WriteRecipeInput
            id="menu-name"
            label="메뉴 명"
            type="text"
            placeholder="메뉴명을 입력해주세요"
            />

            <!-- 대표사진 입력 필드 -->
            <WriteRecipeInput
            id="menu-image"
            label="대표 사진"
            type="image"
            placeholder="여기에 이미지를 드래그하거나 클릭하여 추가"
            />

            <!-- 재료 입력 필드 -->
            <WriteRecipeInput
            id="ingredient"
            label="재료"
            type="text"
            placeholder="재료를 하나씩 입력하고 추가해주세요"
            />

            <!-- 조리 순서 입력 필드 -->
            <WriteRecipeInput
            id="cooking-order"
            label="조리 순서"
            type="text"
            placeholder="단계별 요리법을 작성해주세요"
            />
        </div>

    

        <!-- 보여지는 창 컨테이너 -->
        <div class="show-container">
            
        </div>

    </div>
</template>

<script>
    import WriteRecipeInput from '@/components/recipeBoard/WriteRecipeInput.vue';

    export default {
    components: {
        WriteRecipeInput
    }
};

</script>

<style scoped>

.write-recipe-view-container{
    width: 100%;
}
.input-container{
    width: 100%;
    padding: 1rem ;
}

.show-container{
    width: 100%;
}


</style>
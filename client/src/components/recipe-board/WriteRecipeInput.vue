<template>
    <div class="input-container">
        <label class="input-label" :for="id" :style="dynamicLabelStyle">{{ label }}</label>
        <input v-if="type === 'text'" :id="id" v-model="inputValue" :placeholder="placeholder" :class="inputStyle" />
        <button class="add-btn" :class="addButtonStyle">추가 + </button>
        
        <!-- 이미지 업로드 필드 -->
        <div v-if="type === 'image'" class="image-upload" @click="uploadImage" :class="imageUploadStyle">
            <input type="file" @change="handleFileChange" />
            <img v-if="imageUrl" :src="imageUrl" alt="Uploaded image" />
            <span v-else>{{ placeholder }}</span>
        </div>

        <!-- 조리 순서 (텍스트 입력과 이미지 업로드 포함) -->
        <div v-if="type === 'step'" class="step-container">
            <input v-model="inputValue" :placeholder="placeholder" class="step-input" />
            <div class="image-upload" @click="uploadImage">
                <input type="file" @change="handleFileChange" />
                <img v-if="imageUrl" :src="imageUrl" alt="Uploaded image" />
                
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        label: {
            type: String,
            required: true
        },
        id: {
            type: String,
            required: true
        },
        type: {
            type: String,
            default: 'text' // 기본은 텍스트 필드
        },
        placeholder: {
            type: String,
            default: ''
        }
    },
    computed: {
        dynamicLabelStyle() {
            // label에 따라 다른 스타일을 반환
            if (this.label === '메뉴 명') {
                return { width: '7.5rem' };
            } else if (this.label === '대표 사진') {
                return { width: '9rem' };
            } else if (this.label === '재료') {
                return { width: '5.8rem' };
            }
            return { width: '9rem' };
        },
        inputStyle() {
            // 조건에 따라 input 필드의 스타일을 반환
            if (this.label === '메뉴 명') {
                return 'menu-input';
            } else if (this.label === '재료') {
                return 'ingredient-input';
            } else if (this.label === '조리 순서') {
                return 'step-input';
            }
            return 'default-input';
        },
        imageUploadStyle() {
            // 조건에 따라 image-upload div의 스타일을 반환
            if (this.label === '대표 사진' || this.label === '조리 순서') {
                return 'image-upload-large';
            }
            return 'image-upload';
        },
        addButtonStyle(){
            // 재료 입력과 조리 순서만 add 버튼 추가 
            if (this.label === '재료' || this.label === '조리 순서'){
                return  'add-btn-yes'
            }
            return 'add-btn-no'
        }
    },
    data() {
        return {
            inputValue: '',
            imageUrl: ''
        };
    },
    methods: {
        uploadImage() {
            // 이미지 업로드 로직
        },
        handleFileChange(event) {
            const file = event.target.files[0];
            this.imageUrl = URL.createObjectURL(file); // 이미지 미리보기
        }
    }
};
</script>

<style scoped>

.input-field {
    margin-bottom: 1rem;
}

.input-container {
    display: flex;
    flex-direction: column;
    padding: 1rem 0;
}

.input-label {
    background-color: var(--navy-color);
    color: white;
    padding: 0.5rem 1.3rem;
    border-radius: 0.3rem 0.3rem 0 0;
    display: inline-block;
    width: auto;
    font-size: 1.6rem;
}


/* 기본 input 스타일 */
.default-input {
    padding: 3rem;
    background-color: var(--light-grey-color);
    border-radius: 0 5px 5px 5px;
}

/* 메뉴 명 input 스타일 */
.menu-input {
    border-style: none;
    padding: 3rem;
    background-color: var(--light-grey-color);
    border-radius: 0 5px 5px 5px;
    height: 70px;
}

/* 재료 input 스타일 */
.ingredient-input {
    border-style: none;
    padding: 3rem;
    background-color: var(--light-grey-color);
    border-radius: 0 5px 5px 5px;
    height: 70px;
}

/* 조리 순서 input 스타일 */
.step-input {
    border-style: none;
    padding: 2rem;
    background-color: var(--light-grey-color);
    border-radius: 0 5px 5px 5px;
    height: 180px;
}

/* 기본 이미지 업로드 스타일 */
.image-upload {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    border-left: 1px solid black;
    cursor: pointer;
    text-align: center;
}

/* 큰 이미지 업로드 스타일 */
.image-upload-large {
    border-style: none;
    padding: 3rem;
    background-color: var(--light-grey-color);
    border-radius: 0 5px 5px 5px;
    height: 150px;
}

/* 추가 버튼 */ 
.add-btn-no{
    display: none;
    /* 아예 숨겨버리기 */
}

.add-btn{
    background-color: var(--pink-color);
    border: transparent;
    border-radius: 3px;
    color: white;
}

.ingredient-input + .add-btn{
    position: absolute;
    top: 9px;
    right: 8px;
    padding: 0.5rem 1.3rem;
}

.step-container{
    background-color: var(--light-grey-color);
    height: auto;
    display: flex;

}

.input-label + .add-btn{
    background-color: var(--pink-color);
    position: absolute;
    right: 8px;
    padding: 0.5rem 1.3rem;
}

.input-container .step-container{
    background-color: var(--light-grey-color);
    width: 100%;
}

.step-container .step-input{
    width: 50%;
}

.step-container .image-upload{
    display: flex;
    background-color: transparent;
    width: 50%;
    flex-direction: column;
}

</style>
<template>
  <div class="menu-item">
    <div class="menu-img-wrapper">
      <img :src="props.menuImage || defaultImage" loading="lazy" alt="MenuImage" />
    </div>
    <div class="menu-name-wrapper">
      <span class="menu-name">{{ props.menuName }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const props = defineProps({
  menuImage: {
    type: String,
    required: true
  },
  menuName: {
    type: String,
    required: true
  }
})

const defaultImage = ref(
  'https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1728804967802_a4720492-2dd2-4e59-8f31-79b55e6a169e_%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%E1%84%8B%E1%85%B5%E1%84%86%E1%85%B5%E1%84%8C%E1%85%B5.svg'
)
</script>

<style scoped>
.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 32rem;
  background-color: var(--white-color); /* 배경색을 추가하여 구분 */
  padding: 1rem;
  border-radius: 0.5rem;
  box-shadow: 0 0.4rem 1rem rgba(0, 0, 0, 0.1);
  transition: 0.3s ease;
}

.menu-item:hover {
  box-shadow: 0 1rem 2rem rgba(0, 0, 0, 0.1);
}

.menu-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 80%;
  overflow: hidden;
  border-radius: 1.6rem;
}

.menu-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.menu-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 20%;
  width: 100%;
}

.menu-name {
  font-size: 2rem;
  font-weight: 500;
  line-height: 2rem;
  text-overflow: ellipsis;
  color: var(--black-color);
}

@media screen and (max-width: 1280px) {
  .menu-item {
    height: 30rem;
  }

  .menu-name {
    font-size: 1.8rem;
  }
}

@media screen and (max-width: 1024px) {
  .menu-item {
    height: 28rem;
  }

  .menu-name {
    font-size: 1.6rem;
  }
}

@media screen and (max-width: 425px) {
  .menu-item {
    height: 22rem;
  }

  .menu-name {
    font-size: 1.3rem;
  }
}

@media screen and (max-width: 320px) {
  .menu-item {
    height: 20rem;
  }

  .menu-name {
    font-size: 1.1rem;
  }
}
</style>

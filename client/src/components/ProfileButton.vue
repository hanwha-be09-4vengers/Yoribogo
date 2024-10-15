<template>
  <div class="profile-btn">
    <div class="profile-img-wrapper" @click="toggleMenu">
      <img
        src="https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1720072494119_21792adc-7e3e-4d3e-8e69-3b55fa58615c_IMG_2316.jpeg"
        alt="Profile"
      />
    </div>
    <ul class="menu-list" v-show="isMenuVisible">
      <li class="menu">
        <i class="fa-solid fa-user"></i>
        <span>마이페이지</span>
      </li>
      <li class="menu">
        <i class="fa-solid fa-arrow-right-from-bracket"></i>
        <span>로그아웃</span>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const isMenuVisible = ref(false)

const toggleMenu = () => {
  isMenuVisible.value = !isMenuVisible.value
}
</script>

<style scoped>
.profile-btn {
  display: flex;
  flex-direction: column;
  width: 20rem;
  gap: 1rem;
}

.profile-img-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6.3rem;
  height: 6.3rem;
  border: none;
  border-radius: 50%;
  overflow: hidden;
  background-color: var(--light-yellow-color);
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
  z-index: 9999;
}

.profile-img-wrapper img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.profile-img-wrapper:hover {
  transition: 0.3s ease;
  box-shadow: 0rem 0.4rem 0.4rem 0.1rem rgba(60, 60, 60, 0.5);
}

.menu-list {
  display: flex;
  flex-direction: column;
  background-color: var(--white-color);
  width: 20rem;
  padding: 1rem;
  border-radius: 1.2rem;
  box-shadow: 0.1rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  z-index: 9999;
}

.menu {
  display: flex;
  align-items: center;
  width: 100%;
  height: 4rem;
  padding-left: 1rem;
  gap: 1rem;
  cursor: pointer;
  border: none;
  border-radius: 1rem;
  font-size: 1.4rem;
  font-weight: 500;
  color: var(--black-color);
}

.menu:hover {
  background-color: var(--light-pink-color);
}
</style>

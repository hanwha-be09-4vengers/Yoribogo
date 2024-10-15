<template>
  <nav id="main-nav" class="main-nav">
    <div id="logo" class="logo">
      <img src="../assets/logo.svg" alt="Logo" />
      <span>Yoribogo</span>
    </div>
    <ul class="menu">
      <li class="search-recipe" @click="goWiki">
        <div class="search-bar">
          <span>레시피 검색</span>
          <i class="fa-solid fa-magnifying-glass"></i>
        </div>
      </li>
      <li class="signup">회원가입</li>
      <li class="login">로그인</li>
    </ul>
  </nav>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goWiki = () => {
  router.push('/wiki')
}
</script>

<style scoped>
.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: calc(100% - 8rem);
  height: 100%;
  z-index: 9999;
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo img {
  width: 6rem;
  height: 6rem;
}

.logo span {
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}

.menu {
  display: flex;
  align-items: center;
  gap: 4rem;
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}

ul li {
  cursor: pointer;
}

.search-recipe {
  display: flex;
  background-color: var(--blended-light-color);
  padding: 1rem 3.6rem;
  border: none;
  border-radius: 2rem;
}

.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.search-bar span {
  line-height: 2rem;
  font-weight: 400;
}

.search-bar i {
  color: var(--yellow-color);
}

@media screen and (max-width: 320px) {
  .menu {
    gap: 2.6rem;
  }
}
</style>

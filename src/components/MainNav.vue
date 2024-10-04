<template>
  <nav id="main-nav" class="main-nav">
    <div id="logo" class="logo">
      <img src="../assets/logo.svg" alt="Logo" />
      <span>Yoribogo</span>
    </div>
    <ul class="menu">
      <li class="signup">회원가입</li>
      <li class="login">로그인</li>
    </ul>
  </nav>
</template>

<style scoped>
.main-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: calc(100% - 12rem);
  height: 100%;
  z-index: 9999;
}

.logo {
  display: flex;
  align-items: center;
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
  gap: 4rem;
  font-size: 2rem;
  font-weight: 600;
  color: var(--black-color);
}
</style>

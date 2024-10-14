<template>
  <div class="search-bar">
    <span>레시피 검색</span>
    <i class="fa-solid fa-magnifying-glass"></i>
  </div>
</template>

<style scoped>
.search-bar {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50%;
  padding: 1rem 2rem;
  gap: 1rem;
  border-radius: 1.6rem;
  background-color: var(--light-pink-color);
}

.search-bar span {
  font-size: 2.3rem;
  font-weight: 500;
  color: var(--black-color);
  line-height: 2.3rem;
}

.search-bar i {
  font-size: 2rem;
  color: var(--red-color);
}
</style>

<template>
    <div class="recipe-list">
      <div class="recipe-grid">
        <div v-for="recipe in satisfiedRecipes" :key="recipe.id" class="recipe-card">
          <img :src="recipe.image" alt="Recipe Image" />
          <h3>{{ recipe.title }}</h3>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  // 만족했던 레시피 데이터 (예시 데이터)
  const satisfiedRecipes = ref([
    { id: 1, title: '양파토마토스튜', image: 'https://example.com/recipe3.jpg' },
    { id: 2, title: '황금곰탕 비빔국수', image: 'https://example.com/recipe4.jpg' },
    { id: 3, title: '맛살 미역줄기전', image: 'https://example.com/recipe5.jpg' },
    // 추가 레시피 데이터...
  ]);
  </script>
  
  <style scoped>
  .recipe-list {
    padding: 2rem;
    text-align: center;
  }
  
  .recipe-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 1.5rem;
  }
  
  .recipe-card {
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    text-align: center;
  }
  
  .recipe-card img {
    width: 100%;
    height: 150px;
    object-fit: cover;
  }
  
  .recipe-card h3 {
    margin: 1rem 0;
    font-size: 1.4rem;
    color: #333;
  }
  </style>
  
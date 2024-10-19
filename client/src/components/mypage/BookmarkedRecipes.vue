<template>
    <div class="recipe-list">
      <div class="recipe-grid">
        <div v-for="recipe in bookmarkedRecipes" :key="recipe.id" class="recipe-card">
          <img :src="recipe.image" alt="Recipe Image" />
          <h3>{{ recipe.title }}</h3>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  // 북마크한 레시피 데이터 (예시 데이터)
  const bookmarkedRecipes = ref([
    { id: 1, title: '해산물샐러드와 미나리소스', image: 'https://example.com/recipe1.jpg' },
    { id: 2, title: '매운요거트 토마토샐러드', image: 'https://example.com/recipe2.jpg' },
    { id: 3, title: '양파토마토스튜', image: 'https://example.com/recipe3.jpg' },
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
  
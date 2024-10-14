<template>
  <div class="wiki-view">
    <HomeButton></HomeButton>
    <MainBoard :cur="'wiki'">
      <div class="wiki-container">
        <SearchBar></SearchBar>
        <LoadingSpinner
          v-if="isLoading"
          :loadingColor="'var(--pink-color)'"
          :textColor="'var(--pink-color)'"
        >
        </LoadingSpinner>
        <div class="wiki-list" v-if="!isLoading">
          <MenuItem
            v-for="item in menuList"
            :key="item.recipe_id"
            :menuName="item.menu_name"
            :menuImage="item.menu_image"
          ></MenuItem>
        </div>
      </div>
    </MainBoard>
    <PaginationComponent :data="pageInfo" @changePage="handlePageChange"></PaginationComponent>
  </div>
</template>

<script setup>
import HomeButton from '@/components/HomeButton.vue'
import MainBoard from '@/components/MainBoard.vue'
import SearchBar from '@/components/SearchBar.vue'
import MenuItem from '@/components/MenuItem.vue'
import LoadingSpinner from '@/components/LoadingSpinner.vue'
import PaginationComponent from '@/components/PaginationComponent.vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const isLoading = ref(true)
const menuList = ref([])
const currentPage = ref(1)
const pageInfo = ref({})

const fetchData = async () => {
  try {
    const response = (await axios.get(`/api/recipes?page=${currentPage.value}`)).data
    if (response.success) {
      console.log(response.data)
      menuList.value = response.data.content
      pageInfo.value = response.data.page
      isLoading.value = false
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

const handlePageChange = (newPage) => {
  currentPage.value = newPage
  fetchData()
  window.scrollTo({ top: 0 })
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.wiki-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.wiki-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  align-items: center;
}

.search-bar {
  margin-top: 8rem;
}

.wiki-list {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3개의 열로 구성 */
  gap: 2rem; /* 각 아이템 간의 간격 */
  width: 80%;
  margin-top: 6rem;
  margin-bottom: 8rem;
}

@media screen and (max-width: 960px) {
  .wiki-list {
    grid-template-columns: repeat(2, 1fr); /* 3개의 열로 구성 */
  }
}
</style>

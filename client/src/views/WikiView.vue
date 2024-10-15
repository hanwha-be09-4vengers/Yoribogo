<template>
  <div class="wiki-view">
    <ProfileButton></ProfileButton>
    <HomeButton></HomeButton>
    <MainBoard :cur="'wiki'">
      <div class="wiki-container">
        <SearchBar></SearchBar>
        <div class="wiki-list">
          <MenuItem
            v-for="item in menuList"
            :key="item.recipe_id"
            :menuName="item.menu_name"
            :menuImage="item.menu_image || ''"
          ></MenuItem>
        </div>
      </div>
    </MainBoard>
    <PaginationComponent :data="pageInfo" @changePage="handlePageChange"></PaginationComponent>
  </div>
</template>

<script setup>
import HomeButton from '@/components/HomeButton.vue'
import ProfileButton from '@/components/ProfileButton.vue'
import MainBoard from '@/components/MainBoard.vue'
import SearchBar from '@/components/SearchBar.vue'
import MenuItem from '@/components/MenuItem.vue'
import PaginationComponent from '@/components/PaginationComponent.vue'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router' // Vue Router 사용
import axios from 'axios'

const route = useRoute() // 현재 경로 정보 가져오기
const router = useRouter() // 라우터 인스턴스 가져오기

const menuList = ref([])
const pageInfo = ref({})

const fetchData = async (page) => {
  try {
    const response = (await axios.get(`/api/recipes?page=${page}`)).data
    if (response.success) {
      menuList.value = response.data.content
      pageInfo.value = response.data.page
    }
  } catch (error) {
    console.error('Failed to fetch data:', error)
  }
}

const handlePageChange = (newPage) => {
  fetchData(newPage)
  router.push({ path: '/wiki', query: { page: newPage } })
  window.scrollTo({ top: 0 })
}

onMounted(() => {
  const page = parseInt(route.query.page) || 1 // 페이지 번호를 가져오고 기본값은 1
  fetchData(page)
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

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 7rem;
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
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  width: 80%;
  margin-top: 6rem;
  margin-bottom: 8rem;
}

@media screen and (max-width: 960px) {
  .wiki-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 480px) {
  .profile-btn {
    right: 5rem;
  }

  .home-btn {
    right: 10rem;
  }
}

@media screen and (max-width: 425px) {
  .profile-btn {
    right: 4rem;
  }

  .home-btn {
    right: 9rem;
  }
}

@media screen and (max-width: 375px) {
  .profile-btn {
    right: 3rem;
  }

  .home-btn {
    right: 8rem;
  }
}
</style>

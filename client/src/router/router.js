import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import KakaoCallback from '@/components/user/sns/KakaoCallback.vue' // 콜백 처리용 컴포넌트
import NaverCallback from '@/components/user/sns/NaverCallback.vue' // 콜백 처리용 컴포넌트
import Signup from '@/views/user/SignupView.vue';
import Login from '@/views/user/LoginView.vue';
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/signup',
    name: 'Signup',
    component: Signup
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/question/:qid',
    name: 'question',
    component: () => import('../views/QuestionView.vue')
  },
  {
    path: '/question/result',
    name: 'result',
    component: () => import('../views/ResultView.vue')
  },
  {
    path: '/wiki',
    name: 'wiki',
    component: () => import('../views/WikiView.vue')
  },
  {
    path: '/wiki/:recipeId',
    name: 'wiki-detail',
    component: () => import('../views/WikiDetailView.vue')
  },
  {
    path: '/recipe-board',
    name: 'recipe-board',
    component: () => import('../views/RecipeBoardView.vue')
  },
  {
    path: '/mypage',
    name: 'mypage',
    component: () => import('../views/MyPageView.vue')
  },
  {
    path: '/qna',
    name: 'qna',
    component: () => import('../views/QnaView.vue')
  },
  {
    path: '/qna/:num',
    name: 'qna-detail',
    component: () => import('../views/QnaDetailView.vue'),
    props: true
  },
  {
    path: '/kakaologin',
    name: 'KakaoCallback',
    component: KakaoCallback
  },
  {
    path: '/naverlogin',
    name: 'NaverCallback',
    component: NaverCallback
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

export default router

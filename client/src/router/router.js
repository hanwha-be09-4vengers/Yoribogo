import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import KakaoCallback from '@/components/user/sns/KakaoCallback.vue' // 콜백 처리용 컴포넌트
import NaverCallback from '@/components/user/sns/NaverCallback.vue' // 콜백 처리용 컴포넌트
import SignupView from '@/views/user/SignupView.vue'
import LoginView from '@/views/user/LoginView.vue'

import WriteRecipeBoardComponent from '@/components/recipe-board/WriteRecipeBoardComponent.vue'
import RecipeListView from '@/components/recipe-board/RecipeListView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/signup',
    name: 'Signup',
    component: SignupView
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
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
    component: () => import('../views/RecipeBoardView.vue'), // 메인 레이아웃
      redirect: '/recipe-board/list', 
      // recipe-board 페이지 들어오면 기본적으로 목록을 보여주도록 -> 이후 작성 버튼 누르면 recipe/board/wrtie 페이지로 이동함

    // 하위 레이아웃 (1. 목록 보여주는 페이지, 2. 작성하는 페이지)
    children: [
      {
        path: 'list',  // 레시피 목록 페이지
        component: RecipeListView
      },
      {
        path: 'write',  // 레시피 작성 페이지
        component: WriteRecipeBoardComponent
      }
    ]
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

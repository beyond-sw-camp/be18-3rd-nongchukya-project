import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/modules/auth/views/Login.vue'
import Signup from '@/modules/auth/views/Signup.vue'   // 새 회원가입 컴포넌트

const routes = [
  { path: '/Login', name: 'Login', component: Login },
  { path: '/Signup', name: 'Signup', component: Signup }, // 회원가입 경로 추가
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

import Login from '@/modules/auth/views/Login.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {path: '/', redirect: '/login' },
  { path: '/login', component: Login }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

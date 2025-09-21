import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/modules/auth/views/Login.vue'
import Signup from '@/modules/auth/views/Signup.vue'
import ForgotPassword from '@/modules/auth/views/ForgotPassword.vue'
import ResetPassword from '@/modules/auth/views/ResetPassword.vue'
import Home from '@/views/Home.vue'  
import NotificationPage from '@/modules/chat/views/NotificationPage.vue'
import MyChatPage from '@/modules/chat/views/MyChatPage.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login, meta: { hideHeader: true } },
  { path: '/signup', name: 'Signup', component: Signup, meta: { hideHeader: true } },
  { path: '/forgot-password', name: 'ForgotPassword', component: ForgotPassword, meta: { hideHeader: true } },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword, meta: { hideHeader: true } },
  { path: '/', name: 'Home', component: Home },
  { path: '/notification', name: 'NotificationPage', component: NotificationPage},
  { path: '/chatrooms/list', name: 'MyChatPage', component: MyChatPage}
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

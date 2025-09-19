import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/modules/auth/views/Login.vue'
import Signup from '@/modules/auth/views/Signup.vue'
import ForgotPassword from '@/modules/auth/views/ForgotPassword.vue'
import ResetPassword from '@/modules/auth/views/ResetPassword.vue'
// Community
import PostList from '@/modules/community/views/PostList.vue'

import Home from '@/views/Home.vue'  

const routes = [
  // Auth
  { path: '/login', name: 'Login', component: Login, meta: { hideHeader: true } },
  { path: '/signup', name: 'Signup', component: Signup, meta: { hideHeader: true } },
  { path: '/forgot-password', name: 'ForgotPassword', component: ForgotPassword, meta: { hideHeader: true } },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword, meta: { hideHeader: true } },

  // Community
  {
    name:'post-list',
    path:'/community/post-list',
    component:PostList,
    children: [

    ]
  },

  // Home
  { path: '/', name: 'Home', component: Home }, 
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

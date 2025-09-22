import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/modules/auth/views/Login.vue'
import Signup from '@/modules/auth/views/Signup.vue'
import ForgotPassword from '@/modules/auth/views/ForgotPassword.vue'
import ResetPassword from '@/modules/auth/views/ResetPassword.vue'
import Home from '@/views/Home.vue'  
import MatchApplications from '@/modules/match/views/MatchApplications.vue'
import AddMatchApplication from '@/modules/match/views/AddMatchApplication.vue'
import Matches from '@/modules/match/views/Matches.vue'
import CompletedMatches from '@/modules/match/views/CompletedMatches.vue'

const routes = [
  { path: '/login', name: 'Login', component: Login, meta: { hideHeader: true } },
  { path: '/signup', name: 'Signup', component: Signup, meta: { hideHeader: true } },
  { path: '/forgot-password', name: 'ForgotPassword', component: ForgotPassword, meta: { hideHeader: true } },
  { path: '/reset-password', name: 'ResetPassword', component: ResetPassword, meta: { hideHeader: true } },
  { path: '/', name: 'Home', component: Home }, 
  { path: '/matchApplication/add', name: 'matchApplication/add', component: AddMatchApplication },
  { path: '/matchApplications', name: 'matchApplications', component: MatchApplications },
  { path: '/matches', name: 'matches', component: Matches },
  { path: '/completedMatches', name: 'completedMatches', component: CompletedMatches},
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

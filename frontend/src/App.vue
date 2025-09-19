<template>
  <div>
    <!-- 라우트 meta에 hideHeader가 없으면 헤더 보여줌 -->
    <HeaderComponent v-if="!$route.meta.hideHeader" />
    <router-view />
    메인 화면
  </div>
</template>

<script setup>
import HeaderComponent from './components/HeaderComponent.vue'
</script>

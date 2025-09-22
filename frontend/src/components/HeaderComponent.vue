<!-- HeaderComponent.vue -->
<template>
  <header class="header">
    <nav class="nav">
      <!-- 왼쪽: 로고 -->
      <div class="nav-left">
        <router-link to="/" class="logo-link">
          <img src="@/assets/logo.png" alt="메인 로고" class="logo" />
        </router-link>
      </div>

      <!-- 가운데: 탭 -->
      <div class="nav-center">
        <!-- Match 탭 -->
        <div class="tab dropdown-parent">
          <router-link to="/" class="tab-link">Match</router-link>
          <div class="dropdown">
            <router-link to="/">빠른 매칭 신청</router-link>
            <router-link to="/">매칭 중 조회</router-link>
            <router-link to="/">매칭 결과 조회</router-link>
            <router-link to="/">매칭 이력 조회</router-link>
          </div>
        </div>

        <!-- Match List 탭 -->
        <router-link to="/" class="tab">Match List</router-link>

        <!-- Community 탭 -->
        <div class="tab dropdown-parent">
          <router-link to="/" class="tab-link">Community</router-link>
          <div class="dropdown">
            <router-link :to="{name: 'posts'}">게시판</router-link>
            <router-link to="/friends/list">친구</router-link>
          </div>
        </div>
      </div>

      <!-- 오른쪽: 로그인 상태별 -->
      <div class="nav-right">
        <router-link v-if="isLogin" to="/notification" class="icon-btn" title="알림">
          <i class="mdi mdi-bell-outline"></i>
          <span v-if="bootstrapped " class="badge">{{ unreadCount }}</span>
        </router-link>

        <router-link v-if="isLogin" to="/chatrooms/list" class="icon-btn" title="MYCHATPAGE">
          <i class="mdi mdi-send-outline"></i>
        </router-link>

        <router-link v-if="isLogin" to="/" class="icon-btn" title="마이페이지">
          <i class="mdi mdi-account-circle-outline"></i>
        </router-link>

        <button v-if="isLogin" class="text-btn logout" @click="doLogout">
          로그아웃
        </button>

        <template v-else>
          <router-link to="/Signup" class="text-btn">회원가입</router-link>
          <router-link to="/Login" class="text-btn">로그인</router-link>
        </template>
      </div>
    </nav>
  </header>
</template>

<script setup>
import api from "@/api/axios"
import { useNotificationStore } from "@/stores/notifications"
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'
import { useRouter } from "vue-router"

const router = useRouter()
const notif = useNotificationStore();
const { unreadCount, bootstrapped } = storeToRefs(notif);

const isLogin = ref(false);

    onMounted(() => {
      const token = localStorage.getItem("accessToken");
      if(token) {
        isLogin.value = true;
      }
    });
    async function doLogout() {
      try {
        await api.post("/api/v1/auth/logout") // 쿠키도 서버에서 만료됨

        localStorage.removeItem("accessToken") // accessToken만 제거
        useNotificationStore().resetState()

        isLogin.value = false

        alert("로그아웃 되었습니다.")
        router.push('/');
      } catch (err) {
        console.error("❌ 로그아웃 실패:", err.response?.data || err.message)
        alert("로그아웃 실패: " + (err.response?.data?.message || err.message))
      }
    }

</script>


<style scoped>
/* 레이아웃 */
.header {
  background: #fff;
  border-bottom: 1px solid #e9e9e9;
}
.nav {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  justify-content: space-between;
}
.nav-left, .nav-right {
  display: flex;
  align-items: center;
  gap: 18px;
}
.nav-center {
  display: flex;
  align-items: center;
  gap: 50px;
  position: relative;
}

/* 로고 */
.logo-link {
  display: inline-flex;
  align-items: center;
}
.logo {
  width: 60px;
  height: 60px;
  object-fit: contain;
}

/* 탭 메뉴 */
.tab-link, .tab {
  font-weight: 700;
  color: #111;
  text-decoration: none;
  padding: 8px 14px;
  position: relative;
}
.tab-link:hover, .tab:hover { color: #000; }

/* 드롭다운 */
.dropdown-parent {
  position: relative;
}
.dropdown {
  display: none;
  position: absolute;
  top: 100%; /* 탭 바로 밑 */
  left: 0;
  background: #fff;
  border: 1px solid #ddd;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  flex-direction: column;
  min-width: 180px;
  z-index: 1000;
}
.dropdown a {
  padding: 10px 16px;
  color: #111;
  text-decoration: none;
  font-weight: 500;
  display: block;
}
.dropdown a:hover {
  background: #f5f5f5;
}
.dropdown-parent:hover .dropdown {
  display: flex;
}

/* 아이콘 버튼 */
.icon-btn {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  color: #111;
  text-decoration: none;
}
.icon-btn i {
  font-size: 22px;
  line-height: 1;
}
.icon-btn:hover { opacity: 0.8; }

/* 알림 배지 */
.badge {
  position: absolute;
  top: -4px;
  right: -6px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  border-radius: 8px;
  background: #ff3b30;
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

/* 텍스트 버튼 */
.text-btn {
  background: transparent;
  border: none;
  padding: 4px 6px;
  color: #111;
  font-weight: 700;
  cursor: pointer;
  text-decoration: none;
}
.text-btn:hover { opacity: 0.8; }
.logout { color: #d12; }
</style>

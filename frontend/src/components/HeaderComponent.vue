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
            <router-link to="/">게시판</router-link>
            <router-link to="/">친구</router-link>
          </div>
        </div>
      </div>

      <!-- 오른쪽: 로그인 상태별 -->
      <div class="nav-right">
        <router-link v-if="isLogin" to="/notification" class="icon-btn" title="알림">
          <i class="mdi mdi-bell-outline"></i>
          <span v-if="badgeCount > 0" class="badge">{{ badgeCount }}</span>
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

<script>
import api from "@/api/axios"

export default {
  data() {
    return {
      isLogin: false,
      badgeCount: 0,
      es: null,
    }
  },
  created() {
    const token = localStorage.getItem("accessToken")
    if (token) {
      this.isLogin = true
      this.bootstrapBadge()
      this.connectSSE()
    }
  },
  methods: {
    async bootstrapBadge() {
      try {
        const { data } = await api.get("/api/v1/notifications/unread-count")
        this.badgeCount = data?.count ?? 0
      } catch {
        /* 에러 무시 */
      }
    },
    connectSSE() {
      const token = localStorage.getItem("accessToken")
      this.es = new EventSource(
        `http://localhost:8080/api/v1/sse/notifications?token=${encodeURIComponent(
          token || ""
        )}`
      )
      this.es.addEventListener("match-confirmed", (e) => {
        this.badgeCount += 1
        window.dispatchEvent(
          new CustomEvent("notifications:new", { detail: JSON.parse(e.data) })
        )
      })
      this.es.addEventListener("match-cancelled", (e) => {
        this.badgeCount += 1
        window.dispatchEvent(
          new CustomEvent("notifications:new", { detail: JSON.parse(e.data) })
        )
      })
      window.addEventListener("notifications:reset-badge", this.resetBadge)
    },
    resetBadge() {
      this.badgeCount = 0
    },
    beforeUnmount() {
      if (this.es) this.es.close()
      window.removeEventListener("notifications:reset-badge", this.resetBadge)
    },
    async doLogout() {
  try {
    await api.post("/api/v1/auth/logout") // 쿠키도 서버에서 만료됨

    localStorage.removeItem("accessToken") // accessToken만 제거

    this.isLogin = false
    this.badgeCount = 0

    alert("로그아웃 되었습니다.")
    this.$router.push("/login")
  } catch (err) {
    console.error("❌ 로그아웃 실패:", err.response?.data || err.message)
    alert("로그아웃 실패: " + (err.response?.data?.message || err.message))
  }
},
  },
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

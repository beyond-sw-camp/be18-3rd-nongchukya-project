<template>
  <div class="mypage-container">
    <!-- 상단 헤더 -->
    <header class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">마이페이지</h1>
          <p class="page-subtitle">운동 활동과 성과를 한눈에 확인하세요</p>
        </div>
      </div>
    </header>

    <!-- 탭 -->
    <div class="tabs">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        :class="{ active: activeTab === tab.id }"
        @click="activeTab = tab.id"
      >
        {{ tab.label }}
      </button>
    </div>

    <!-- 사용자 배지 -->
    <div class="user-badge" v-if="userData">
      <div class="avatar">
        <img :src="userData.profileImage || defaultProfile" :alt="userData.name" />
      </div>
      <div class="user-info">
        <span class="welcome-text">{{ userData.name }}님</span>
        <span class="nickname-text">{{ profile?.nickname }}님</span>
      </div>
    </div>

    <!-- 프로필 섹션 (자식 컴포넌트) -->
    <ProfileSection
      v-if="activeTab === 'profile' && userData"
      :user-data="userData"
      :user-levels="userLevels"
      @update="updateUserData"
      
    />
    <!-- 신고 섹션 (자식 컴포넌트) -->
    <ReportSection v-if="activeTab === 'report'" />
    <Matching v-if="activeTab === 'matching'" :active-tab="activeTab" />
    <Setting v-if="activeTab === 'settings'" />   <!-- 추가 -->
    
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import defaultProfile from '@/assets/default_profile.png'
import ProfileSection from './ProfileSection.vue'
import ReportSection from './Report.vue'
import Setting from './Setting.vue'
import Matching from './Matching.vue'


const profile = ref(null)
const userData = ref(null)
const userLevels = ref({}) // 운동 레벨 정보
const activeTab = ref('profile')

const tabs = [
  { id: 'profile', label: '프로필' },
  { id: 'matches', label: '매치기록' },
  { id: 'matching', label: '매칭 중 경기' },
  { id : 'report', label: '신고' },
  { id: 'settings', label: '설정' }
]

onMounted(async () => {
  try {
    const token = localStorage.getItem('accessToken')

    // 프로필 정보 (응답이 객체 그대로이므로 items[0] 대신 data 사용)
    const profileRes = await axios.get('http://localhost:8080/api/v1/mypage/profile', {
      headers: { Authorization: `Bearer ${token}` }
    })

    const profileData = profileRes.data

    if (profileData) {
      profile.value = profileData
      userData.value = {
        ...profileData,
        phone: profileData.phoneNumber || '미등록',
        profileImage: profileData.profileImage || defaultProfile,
        favoriteSports: profileData.favoriteSports || [] // 선호 운동 추가
      }
    }

    // 운동 레벨 정보
    const levelsRes = await axios.get('http://localhost:8080/api/v1/mypage/levels', {
      headers: { Authorization: `Bearer ${token}` }
    })

    let levelsData = levelsRes.data
    if (typeof levelsData === 'string') {
      levelsData = JSON.parse(levelsData)
    }

    // sportName 기준으로 매핑
    userLevels.value = levelsData.reduce((acc, item) => {
      acc[item.sportName] = {
        levelId: item.levelId,
        levelName: item.levelName,
        interest: item.interest
      }
      return acc
    }, {})

  } catch (err) {
    console.error('마이페이지 호출 에러:', err)
    userData.value = {
      name: '사용자',
      email: '-',
      phone: '-',
      address: '-',
      age: '-',
      profileImage: defaultProfile,
      favoriteSports: []
    }
  }
})

const updateUserData = (data) => {
  userData.value = { ...userData.value, ...data }
}
</script>

<style scoped>
.mypage-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.tabs {
  margin-top: 1.5rem;
  margin-bottom: 2rem;
  display: flex;
  gap: 1rem;
}

.tabs button {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: 1px solid #e2e8f0;
  background: white;
  cursor: pointer;
}

.tabs button.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}

.user-badge {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  margin-top: 1rem;
  margin-bottom: 1.5rem;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  overflow: hidden;
  border: 2px solid rgba(0,0,0,0.1);
  background: #f0f0f0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.welcome-text {
  font-weight: 600;
  font-size: 1.2rem;
  color: #1e293b;
}

.nickname-text {
  font-weight: 500;
  color: #64748b;
  margin-top: 0.25rem;
}

.page-header {
  background: linear-gradient(135deg, #3B82F6 0%, #1D4ED8 100%);
  color: white;
  padding: 2rem 3rem;
  box-shadow: 0 4px 20px rgba(59, 130, 246, 0.2);
}
</style>

<template>
  <div class="settings">
    <h2 class="section-title">설정</h2>

    <div class="settings-cards">
      <!-- 계정 관리 카드 -->
      <div class="settings-card">
        <h3 class="card-title">계정 관리</h3>
        <p class="card-desc">계정과 관련된 설정을 관리하세요</p>
        <div class="card-actions">
          <button class="action-btn primary" @click="showPasswordChange = true">
            🔒 비밀번호 변경
          </button>
          <button class="action-btn warning" @click="showDeleteConfirm = true">
            🗑️ 계정 삭제
          </button>
        </div>
      </div>

      <!-- 알림 설정 카드 -->
      <div class="settings-card">
        <h3 class="card-title">알림 설정</h3>
        <p class="card-desc">앱 알림 및 이메일 알림을 설정하세요</p>
        <div class="card-actions">
          <button class="action-btn primary">🔔 알림 설정</button>
        </div>
      </div>

      <!-- 앱 정보 카드 -->
      <div class="settings-card">
        <h3 class="card-title">앱 정보</h3>
        <p class="card-desc">앱 버전 및 지원 정보를 확인하세요</p>
        <div class="app-info">
          <div class="info-row">
            <span class="info-label">버전</span>
            <span class="info-value">1.0.0</span>
          </div>
          <div class="info-row">
            <span class="info-label">최종 업데이트</span>
            <span class="info-value">2025년 9월 23일</span>
          </div>
          <div class="info-actions">
            <a href="#" class="info-link">이용약관</a>
            <a href="#" class="info-link">개인정보처리방침</a>
          </div>
        </div>
      </div>
    </div>

    <!-- 계정 삭제 모달 -->
    <div v-if="showDeleteConfirm" class="modal-overlay" @click="showDeleteConfirm = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>계정 삭제</h3>
          <button class="close-btn" @click="showDeleteConfirm = false">×</button>
        </div>
        <div class="modal-body">
          <div class="warning-content">
            <div class="warning-icon">⚠️</div>
            <h4>정말로 계정을 삭제하시겠습니까?</h4>
            <p>이 작업은 되돌릴 수 없으며, 모든 데이터가 영구적으로 삭제됩니다.</p>
            <ul class="delete-warning-list">
              <li>프로필 정보</li>
              <li>매치 기록</li>
              <li>통계 데이터</li>
              <li>설정 정보</li>
            </ul>
            <div class="form-group">
              <label for="deletePassword">비밀번호 입력</label>
              <input
                id="deletePassword"
                type="password"
                v-model="deletePassword"
                class="form-input"
                placeholder="비밀번호를 입력해주세요"
              />
            </div>
          </div>
          <div class="form-actions">
            <button class="btn-cancel" @click="showDeleteConfirm = false">취소</button>
            <button class="btn-delete" @click="deleteAccount">삭제</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const showPasswordChange = ref(false)
const showDeleteConfirm = ref(false)
const deletePassword = ref('')

const downloadData = () => {
  const data = {
    profile: '사용자 프로필 데이터',
    matches: '매치 기록 데이터',
    stats: '통계 데이터'
  }

  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'my-sports-data.json'
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

const userId = localStorage.getItem('userId') // 로그인한 사용자 ID 필요

const deleteAccount = async () => {
  if (!deletePassword.value) {
    alert("비밀번호를 입력해주세요.")
    return
  }

  try {
    const token = localStorage.getItem('accessToken')
    await axios.put(
      `http://localhost:8080/api/v1/delete-user/${userId}`,
      { password: deletePassword.value },
      { headers: { Authorization: `Bearer ${token}` } }
    )

    alert("계정이 삭제되었습니다.")
    localStorage.clear()
    window.location.href = "/login"
  } catch (err) {
    console.error(err)
    alert("계정 삭제에 실패했습니다. 비밀번호를 확인하세요.")
  } finally {
    showDeleteConfirm.value = false
    deletePassword.value = ''
  }
}
</script>

<style scoped>
.settings {
  padding: 2rem;
  max-width: 900px;
  margin: 0 auto;
  font-family: 'Noto Sans KR', sans-serif;
}

.section-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #1e293b;
}

.settings-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.settings-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  padding: 1.5rem;
  flex: 1 1 300px;
}

.card-title {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
}

.card-desc {
  font-size: 0.9rem;
  margin-bottom: 1rem;
  color: #64748b;
}

.card-actions {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.action-btn {
  padding: 0.6rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.action-btn.primary { background: #3b82f6; color: white; }
.action-btn.warning { background: #ef4444; color: white; }

/* 앱 정보 카드 */
.app-info .info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.4rem 0;
  border-bottom: 1px solid #e5e7eb;
}

.app-info .info-actions {
  margin-top: 1rem;
  display: flex;
  gap: 1rem;
}

.info-link {
  color: #3b82f6;
  text-decoration: none;
  font-size: 0.9rem;
}

/* 모달 스타일 */
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1rem;
}

.modal-content {
  background: white;
  border-radius: 12px;
  max-width: 500px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  padding: 1.5rem;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: #64748b;
}

.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1.5rem;
}

.btn-cancel {
  padding: 0.5rem 1.2rem;
  border: 1px solid #e2e8f0;
  background: white;
  color: #64748b;
  border-radius: 6px;
  cursor: pointer;
}

.btn-delete {
  padding: 0.5rem 1.2rem;
  border: none;
  background: #dc2626;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}
</style>

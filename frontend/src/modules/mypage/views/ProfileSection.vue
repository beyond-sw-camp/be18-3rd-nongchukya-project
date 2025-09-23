<template>
  <div class="profile-section">
    <!-- 프로필 헤더 + 편집 버튼 -->
    <div class="profile-header">
      <h2>프로필 정보</h2>
      <button class="edit-btn" @click="editProfileMode = !editProfileMode">
        {{ editProfileMode ? '취소' : '편집' }}
      </button>
    </div>

    <!-- 프로필 편집 모드 -->
    <div v-if="editProfileMode" class="edit-form">
      <input v-model="editableData.name" placeholder="이름" />
      <input v-model="editableData.email" placeholder="이메일" />
      <input v-model="editableData.phone" placeholder="전화번호" />
      <input v-model="editableData.address" placeholder="주소" />
      <input v-model="editableData.age" placeholder="나이" type="number" />
      <button @click="saveProfile">저장</button>
    </div>

    <!-- 기본 정보 모드 -->
    <div class="basic-info" v-else>
      <p><strong>이름:</strong> {{ editableData.name || '-' }}</p>
      <p><strong>이메일:</strong> {{ editableData.email || '-' }}</p>
      <p><strong>전화번호:</strong> {{ editableData.phone || '미등록' }}</p>
      <p><strong>주소:</strong> {{ editableData.address || '미등록' }}</p>
      <p><strong>나이:</strong> {{ editableData.age || '-' }}</p>

      <!-- 관심 있는 운동만 표시 -->
      <div class="profile-header">
        <h3>선호 운동</h3>
        <button class="edit-btn" @click="editLevelMode = !editLevelMode">
          {{ editLevelMode ? '취소' : '레벨 수정' }}
        </button>
      </div>

      <div class="favorite-sports">
        <ul>
          <li v-for="sport in interestedSports" :key="sport">
            {{ sport }} - 레벨: {{ localUserLevels[sport].levelName }}
          </li>
          <li v-if="!interestedSports.length">선호 운동이 없습니다.</li>
        </ul>
      </div>
    </div>

    <!-- 레벨 수정 모드 -->
    <div v-if="editLevelMode" class="edit-form">
      <div v-for="sport in defaultSports" :key="sport" class="sport-edit">
        <label>{{ sport }}</label>
        <select v-model="editableLevels[sport].levelId">
          <option disabled value="">레벨 선택</option>
          <option v-for="level in levelOptions" :key="level.id" :value="level.id">
            {{ level.name }}
          </option>
        </select>

        <label class="checkbox-container">
          관심 있음
          <input type="checkbox" v-model="editableLevels[sport].interest" />
          <span class="checkmark"></span>
        </label>
      </div>
      <button @click="saveLevels">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import axios from 'axios'

const props = defineProps({
  userData: Object,
  userLevels: Object
})

const emit = defineEmits(['update'])

// 기본 종목
const defaultSports = ['농구', '축구', '야구']

// 프로필 편집 모드
const editProfileMode = ref(false)
const editableData = reactive({ ...props.userData })

// 레벨 편집 모드
const editLevelMode = ref(false)
const editableLevels = reactive({})
const localUserLevels = reactive({})

// 레벨 옵션
const levelOptions = [
  { id: 1, name: 'Beginner' },
  { id: 2, name: 'Intermediate' },
  { id: 3, name: 'Advanced' }
]

// 초기 데이터 세팅
const initLevels = (userLevels) => {
  for (const sport of defaultSports) {
    const interest = userLevels?.[sport]?.interest ?? false
    localUserLevels[sport] = {
      levelId: userLevels?.[sport]?.levelId || 1,
      levelName: userLevels?.[sport]?.levelName || 'Beginner',
      interest
    }
    editableLevels[sport] = {
      levelId: userLevels?.[sport]?.levelId || 1,
      interest
    }
  }
}

// props 변경 시 초기화
watch(
  () => props.userLevels,
  (newVal) => initLevels(newVal),
  { immediate: true, deep: true }
)

// 관심 있는 운동만 계산
const interestedSports = computed(() => {
  return defaultSports.filter(sport => localUserLevels[sport]?.interest)
})

// 프로필 저장 (API 호출)
const saveProfile = async () => {
  try {
    const token = localStorage.getItem('accessToken')
    const res = await axios.put('http://localhost:8080/api/v1/mypage/profile', {
      name: editableData.name,
      email: editableData.email,
      phoneNumber: editableData.phone,
      address: editableData.address,
      age: editableData.age
    }, {
      headers: { Authorization: `Bearer ${token}` }
    })

    editableData.name = res.data.name
    editableData.email = res.data.email
    editableData.phone = res.data.phoneNumber
    editableData.address = res.data.address
    editableData.age = res.data.age

    emit('update', { ...res.data })
    editProfileMode.value = false
    alert('프로필이 업데이트되었습니다.')
  } catch (err) {
    console.error(err)
    alert('프로필 업데이트 실패')
  }
}

// 레벨 저장 (API 호출)
const saveLevels = async () => {
  const token = localStorage.getItem('accessToken')

  for (const sport of defaultSports) {
    const data = {
      sportName: sport,
      levelId: editableLevels[sport].levelId,
      interest: editableLevels[sport].interest
    }

    try {
      await axios.post('http://localhost:8080/api/v1/mypage/levels', data, {
        headers: { Authorization: `Bearer ${token}` }
      })

      // UI 즉시 반영
      localUserLevels[sport] = {
        levelId: editableLevels[sport].levelId,
        levelName: levelOptions.find(l => l.id === editableLevels[sport].levelId)?.name || 'Unknown',
        interest: editableLevels[sport].interest
      }
    } catch (err) {
      console.error(`레벨 업데이트 실패: ${sport}`, err)
    }
  }
  editLevelMode.value = false
  alert('레벨 정보가 업데이트되었습니다.')
}
</script>

<style scoped>
.profile-section { margin-top: 1rem; }
.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.profile-header h2,
.profile-header h3 {
  margin: 0;
  padding: 0;
  line-height: 1.2;
  font-weight: 600;
  font-size: 1.25rem;
}
.edit-form input,
.edit-form select {
  display: block;
  margin: 0.3rem 0;
  padding: 0.4rem 0.6rem;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #d1d5db;
}
.edit-form button {
  margin-top: 0.5rem;
  padding: 0.4rem 0.8rem;
  background-color: #10b981;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.edit-btn {
  padding: 0.3rem 0.8rem;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.basic-info {
  background: #f9fafb;
  padding: 1rem;
  border-radius: 12px;
  font-size: 0.95rem;
}
.basic-info p { margin: 0.25rem 0; }
.basic-info strong { color: #3b82f6; }
.favorite-sports h3 { margin-top: 1rem; margin-bottom: 1rem; font-weight: 600; }
.favorite-sports ul { list-style: disc; padding-left: 1.2rem; }

.checkbox-container {
  display: inline-block;
  position: relative;
  padding-left: 25px;
  margin-right: 10px;
  cursor: pointer;
  font-size: 0.95rem;
  user-select: none;
  color: #1e293b;
}
.checkbox-container input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}
.checkmark {
  position: absolute;
  top: 0;
  left: 0;
  height: 18px;
  width: 18px;
  background-color: #eee;
  border-radius: 4px;
  border: 1px solid #d1d5db;
  transition: all 0.2s ease;
}
.checkbox-container input:checked ~ .checkmark {
  background-color: #3b82f6;
  border-color: #3b82f6;
}
.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}
.checkbox-container input:checked ~ .checkmark:after {
  display: block;
}
.checkbox-container .checkmark:after {
  left: 5px;
  top: 1px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}
</style>

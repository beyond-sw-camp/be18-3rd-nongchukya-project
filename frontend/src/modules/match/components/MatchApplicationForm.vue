<template>
  <form @submit.prevent="submitClick">
    <div class="mb-3">
      <label for="sport" class="form-label">종목</label>
      <select class="form-select" v-model="formData.sport">
        <option disabled value="">종목 선택</option>
        <option v-for="sport in sports" :key="sport.id" :value="sport.name">
          {{ sport.name }}
        </option>
      </select>
    </div>

    <div class="mb-3">
      <label for="region" class="form-label">지역</label>
      <input type="text" class="form-control" id="region" v-model="formData.region">
    </div>

    <div class="mb-3">
      <label for="date" class="form-label">날짜</label>
      <input type="date" class="form-control" id="date" v-model="formData.matchDate">
    </div>

    <div class="mb-3">
      <label for="startTime" class="form-label">시작 시간</label>
      <select class="form-select time-select" id="startTime" v-model="formData.startTime">
        <option disabled value="">시작 시간 선택</option>
        <option v-for="h in 24" :key="'start-' + h" :value="`${String(h).padStart(2,'0')}:00`">
          {{ String(h).padStart(2,'0') }}:00
        </option>
      </select>
    </div>

    <div class="mb-3">
      <label for="endTime" class="form-label">종료 시간</label>
      <select class="form-select time-select" id="endTime" v-model="formData.endTime">
        <option disabled value="">종료 시간 선택</option>
        <option v-for="h in 24" :key="'end-' + h" :value="`${String(h).padStart(2,'0')}:00`">
          {{ String(h).padStart(2,'0') }}:00
        </option>
      </select>
    </div>

    <div class="mb-3">
      <label for="genderOption" class="form-label">성별</label>
      <select class="form-select" v-model="formData.genderOption">
        <option value="A">상관없음</option>
        <option value="F">여자만</option>
        <option value="M">남자만</option>
      </select>
    </div>

    <div class="btn-wrapper">
      <button type="submit" class="btn btn-primary m-2">신청</button>
    </div>
  </form>
</template>

<script setup>
import { reactive, toRaw } from 'vue'

const formData = reactive({
  sport: "",
  region: "",
  matchDate: "",
  startTime: "",
  endTime: "",
  genderOption: "A"
})

const props = defineProps({
  initFormData: Object,
  formType: {
    type: String,
    required: true
  },
  sports: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['form-submit'])

const submitClick = () => {
  emit('form-submit', toRaw(formData))
}
</script>
<style scoped>
form {
    
  background: #fff;
  padding: 24px;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0,0,0,0.08);
  animation: fadeIn 0.5s ease;
}

.form-label {
  font-weight: 600;
  color: #444;
  margin-bottom: 6px;
  display: block;
}

.form-control,
.form-select {
  border-radius: 10px;
  border: 1px solid #ddd;
  padding: 10px 14px;
  transition: all 0.25s ease;
  background: #fafafa;

  &:focus {
    border-color: #4a90e2;
    box-shadow: 0 0 8px rgba(74,144,226,0.4);
    background: #fff;
  }
}

.mb-3 {
  margin-bottom: 20px;
}

.time-select {
  padding: 10px 14px;
  border-radius: 10px;
  background: #fafafa;
  transition: all 0.25s ease;

  &:hover {
    border-color: #4a90e2;
    background: #f0f7ff;
  }
}

.btn-wrapper {
  display: flex;
  justify-content: center;
}

.btn-primary {
  background: linear-gradient(135deg, #4a90e2, #357ABD);
  border: none;
  padding: 10px 24px;
  border-radius: 12px;
  font-weight: 600;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;

  &:hover {
    background: linear-gradient(135deg, #5aa3f2, #4a90e2);
    transform: translateY(-2px);
    box-shadow: 0 6px 14px rgba(74,144,226,0.4);
  }

  &:active {
    transform: scale(0.97);
  }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

</style>

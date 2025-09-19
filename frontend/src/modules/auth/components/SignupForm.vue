<template>
  <form class="signup-form" @submit.prevent="onSubmit">

    <div class="input-group">
      <label for="signup-email">이메일</label>
      <input
        id="signup-email"
        type="email"
        placeholder="이메일을 입력하세요."
        v-model="email"
        required
      />
    </div>

    <div class="input-group">
      <label for="signup-username">아이디</label>
      <input
        id="signup-username"
        type="text"
        placeholder="아이디를 입력하세요."
        v-model="username"
        required
      />

      <div v-if="username">
        <span v-if="isUsernameAvailable === true" class="username-available">
          사용 가능한 아이디입니다.
        </span>
        <span v-else-if="isUsernameAvailable === false" class="username-taken">
          이미 사용중인 아이디입니다.
        </span>
      </div>
    </div>

    <div class="input-group">
      <label for="signup-password">비밀번호</label>
      <input
        id="signup-password"
        type="password"
        placeholder="비밀번호를 입력하세요."
        v-model="password"
        required
      />
    </div>

    <div class="input-group">
      <label for="signup-password-confirm">비밀번호 확인</label>
      <input
        id="signup-password-confirm"
        type="password"
        placeholder="비밀번호를 한번 더 입력하세요."
        v-model="passwordConfirm"
        required
      />
      <div v-if="passwordMismatch">
        <span class="password-mismatch">비밀번호가 일치하지 않습니다.</span>
      </div>
    </div>

    <div class="input-group">
      <label for="signup-name">이름</label>
      <input
        id="signup-name"
        type="text"
        placeholder="이름을 입력하세요."
        v-model="name"
        required
      />
    </div>

    <div class="input-group">
      <label for="signup-birthdate">생년월일</label>
      <input
        id="signup-birthdate"
        type="date"
        v-model="birthdate"
        required
      />
    </div>

    <div class="input-group">
      <label for="signup-phone">전화번호</label>
      <div class="row-input">
        <input
          id="signup-phone"
          type="tel"
          placeholder="전화번호를 입력하세요. (예: 010-1234-5678)"
          v-model="phone"
          required
        />
        <button type="button" @click="sendVerificationCode">인증요청</button>
      </div>
    </div>

    <div class="input-group" v-if="verificationSent">
      <label for="signup-verification">인증번호 입력</label>
      <div class="row-input">
        <input
          id="signup-verification"
          type="text"
          placeholder="인증번호를 입력하세요."
          v-model="verificationCode"
        />
        <button type="button" @click="checkVerificationCode">확인</button>
      </div>
    </div>

    <button type="submit" class="submit-btn">회원가입</button>

    <p class="login-text">
      이미 계정이 있으신가요?
      <router-link to="/login">로그인</router-link>
    </p>
  </form>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const email = ref('')
const username = ref('')
const password = ref('')
const passwordConfirm = ref('')
const name = ref('')
const birthdate = ref('')
const phone = ref('')
const verificationCode = ref('')
const verificationSent = ref(false)

// 아이디 중복 확인 (testuser만 막음)
const isUsernameAvailable = ref(null)
let debounceTimer = null
watch(username, (val) => {
  clearTimeout(debounceTimer)
  if (!val) {
    isUsernameAvailable.value = null
    return
  }
  debounceTimer = setTimeout(() => {
    isUsernameAvailable.value = val !== 'testuser'
  }, 1500) 
})

const passwordMismatch = computed(() => {
  return passwordConfirm.value && password.value !== passwordConfirm.value
})

const sendVerificationCode = () => {
  if (!phone.value) {
    alert('전화번호를 입력해주세요.')
    return
  }
  verificationSent.value = true
  alert(`인증번호가 ${phone.value}로 전송되었습니다.`)
}

const checkVerificationCode = () => {
  if (verificationCode.value === '1234') {
    alert('전화번호 인증 성공!')
  } else {
    alert('인증번호가 올바르지 않습니다.')
  }
}

const onSubmit = () => {
  if (passwordMismatch.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }
  if (isUsernameAvailable.value === false) {
    alert('이미 사용중인 아이디입니다.')
    return
  }

  alert('회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.')
  router.push('/login')
}
</script>

<style scoped>

.username-available {
  color: #1abc1a;
  font-size: 0.92rem;
  margin-top: 0.2rem;
}
.username-taken,
.password-mismatch {
  color: #e74c3c;
  font-size: 0.92rem;
  margin-top: 0.2rem;
}

.signup-form {
  display: flex;
  flex-direction: column;
  width: 80%;
  max-width: 600px;
  margin: 0 auto;
}

input {
  height: 40px;
  padding: 0 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
  font-size: 1rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1.5rem;
}

.input-group label {
  font-size: 0.75rem;
  color: #444;
  margin-bottom: 0.3rem;
  font-weight: 500;
}

.row-input {
  display: flex;
  gap: 0.5rem;
}
.row-input input {
  flex: 3;
}
.row-input button {
  flex: 1;
  height: 40px;
  border-radius: 6px;
  background-color: #1D61E7;
  color: white;
  border: none;
  cursor: pointer;
}
.row-input button:hover {
  background-color: #5d90f5;
}

.submit-btn {
  align-self: center;
  width: 30%;
  height: 44px;
  margin-top: 1rem;
  color: #ffffff;
  background-color: #1D61E7;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 400;
}
.submit-btn:hover {
  background-color: #5d90f5;
}

.login-text {
  margin-top: 1rem;
  font-size: 0.9rem;
  color: #444;
  text-align: center;
}
.login-text a {
  color: #1D61E7;
  font-weight: 600;
  text-decoration: none;
}
.login-text a:hover {
  color: #5d90f5;
  text-decoration: underline;
}
</style>

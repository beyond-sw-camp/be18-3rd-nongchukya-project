<template>
  <div class="forgot-password-page">
    <div class="forgot-password-center">
      <img src="@/assets/Key.png" alt="Key Icon" class="key-icon" />
      <h1>Forgot Password?</h1>
      <p>이메일 인증이 완료되면<br>새로운 비밀번호를 입력하실 수 있습니다.</p>

       <form class="forgot-form" @submit.prevent="verifyCode">
        <!-- 이메일 + 인증요청 버튼 -->
        <div class="input-group">
          <label for="forgot-email">이메일</label>
          <div class="row-input">
            <input
              id="forgot-email"
              v-model="email"
              type="email"
              placeholder="이메일을 입력하세요."
              required
            />
            <button type="button" @click="sendCode">인증요청</button>
          </div>
        </div>

        <div class="input-group">
          <label for="forgot-code">인증번호</label>
          <input
            id="forgot-code"
            v-model="code"
            type="text"
            placeholder="인증번호를 입력하세요."
            required
          />
        </div>

        <button class="submit-btn" type="submit">확인</button>
      </form>

      <p v-if="message" class="message">{{ message }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import { useRouter } from "vue-router"

const email = ref("")
const code = ref("")
const message = ref("")
const router = useRouter()

function sendCode() {
  if (!email.value) {
    alert("이메일을 입력하세요.")
    return
  }
  message.value = "인증번호가 이메일로 전송되었습니다 (테스트: 1234)."
}

function verifyCode() {
  if (code.value === "1234") {
    message.value = "인증 성공!"
    router.push({ name: "ResetPassword" })
  } else {
    alert("인증번호가 올바르지 않습니다.")
  }
}
</script>

<style scoped>
.forgot-password-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
}
.forgot-password-center {
  width: 100%;
  max-width: 600px;
  margin: 0.5rem auto 10rem auto;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.key-icon {
  display: block;
  margin: 0 auto 0rem;
  width: 50px;
  height: 50px;
}
h1 {
  text-align: center;
  margin: 0;
}
p {
  text-align: center;
  color: #8C8C8C;
  margin: 0.75rem 0 1.5rem;
  line-height: 1.4;
  font-size: 12px;
}
.forgot-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: stretch;
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
input {
  height: 40px;
  padding: 0 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
  box-sizing: border-box;
  font-size: 1rem;
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
  border-radius: 10px;
  background-color: #1D61E7;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 300;
}
.row-input button:hover {
  background-color: #5d90f5;
}

/* 확인 버튼은 회원가입 버튼 스타일처럼 */
.submit-btn {
  align-self: center;
  width: 30%;
  height: 40px;
  margin-top: 0.5rem;
  color: #ffffff;
  background-color: #1D61E7;
  border-radius: 10px;
  font-size: 1rem;
  font-weight: 350;
  border: none;
  cursor: pointer;
  transition: background 0.2s;
}
.submit-btn:hover {
  background-color: #5d90f5;
}
.message {
  color: #1abc1a;
  margin-top: 1rem;
  text-align: center;
}
</style>
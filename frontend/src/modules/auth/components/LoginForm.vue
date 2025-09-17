<template>
  <form>
    <input type="text" placeholder="아이디" />
    <input type="password" placeholder="비밀번호" />
    <button type="submit">로그인</button>

    <p class="or-text">Or login with</p>

    <div class="social-login">
      <button type="button" class="social-btn fancy-btn">
        <svg xmlns="http://www.w3.org/2000/svg" width="100%" height="100%">
          <rect class="border" width="100%" height="100%" />
        </svg>
        <img src="@/assets/KakaoIcon.png" alt="Kakao" class="kakao-icon" />
      </button>
    </div>
  </form>
</template>

<script setup>
</script>

<style scoped>
form {
  display: flex;
  flex-direction: column;
  width: 80%;
  max-width: 300px;
}

.or-text {
  text-align: center;
  color: #888;
  margin: 1.2rem 0 0.5rem 0;
  font-size: 0.95rem;
}

input {
  margin-bottom: 1rem;
  padding: 0.7rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}

button[type="submit"] {
  padding: 0.7rem;
  background-color: #FFA047;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
button[type="submit"]:hover {
  background-color: #ff8614;
}

.social-login {
  display: flex;
  justify-content: center;
  margin-top: 1rem;
}

.fancy-btn {
  position: relative;
  width: 300px;   
  height: 40px;
  background: #fff;
  border-radius: 8px;
  cursor: pointer;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
}

.kakao-icon {
  width: 24px;
  height: 24px;
  z-index: 1; 
}

.fancy-btn svg {
  position: absolute;
  top: 0;
  left: 0;
}

.fancy-btn .border {
  stroke: #000;
  stroke-width: 1;          
  fill: none;
  rx: 8; ry: 8;             

  stroke-dasharray: 680;    
  stroke-dashoffset: 680;   

  transition: stroke-dashoffset 0.7s ease, stroke-width 0.3s ease;
}

/* hover 시 */
.fancy-btn:hover .border {
  stroke-dashoffset: 0;     
  stroke-width: 2;         
}
</style>

<template>
  <main class="page-background">
    <div class="form-background">
      <MatchApplicationForm
        :init-form-data="initFormData"
        @form-submit="formSubmit"/>
    </div>
  </main>
</template>

<script setup>
import MatchApplicationForm from '../components/MatchApplicationForm.vue';
import { reactive } from 'vue';


    const initFormData = reactive({
        sport: '',
        region: '',
        date: '',
        time: '',
        gender: '상관없음'
    });

</script>

<style lang="scss" scoped>
.page-background {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to bottom, #FFA652, #EEA562, #F6EADF);
  font-family: 'Inter', sans-serif;
}

.form-background {
  padding: 1em;
  border-radius: 30px;
  display: flex;
  align-content: center;
  background-color: #F6F4F2;
}

</style>
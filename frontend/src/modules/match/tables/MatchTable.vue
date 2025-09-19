<template>
  <table class="table table-striped table-hover text-center">
    <thead>
      <tr>
        <th>종목</th>
        <th>지역</th>
        <th>경기일</th>
        <th>시간</th>
        <th>성별</th>
        <th>신청시각</th>
        <th>취소</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="matchApplication in matchApplications" :key="matchApplication.id">
        <td>{{ matchApplication.sport }}</td>
        <td>{{ matchApplication.region }}</td>
        <td>{{ matchApplication.date }}</td>
        <td>{{ matchApplication.time }}</td>
        <td>{{ matchApplication.genderOption }}</td>
        <td>{{ matchApplication.createdAt }}</td>
        <td>
          <button type="button" class="btn">취소</button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>
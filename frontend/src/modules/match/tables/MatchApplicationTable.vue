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
        <th>상태</th>
        <th>취소</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="matchApplication in matchApplications" :key="matchApplication.id">
        <td>{{ matchApplication.sport }}</td>
        <td>{{ matchApplication.region }}</td>
        <td>{{ matchApplication.matchDate }}</td>
        <td>{{ matchApplication.matchTime }}</td>
        <td>{{ getGenderText(matchApplication.genderOption) }}</td>
        <td>{{ matchApplication.createdAt }}</td>
        <td>{{ matchApplication.status }}</td>
        <td>
          <button
            v-if="matchApplication.status === 'WAITING'"
            type="button"
            class="btn btn-sm btn-danger"
            @click="$emit('cancel-click', matchApplication.id)">
            취소
          </button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
  const props = defineProps({
    matchApplications: {
      type: Array,
      required: true
    }
  });

  const emit = defineEmits(['cancel-click']);

  const getGenderText = (genderOption) => {
    if (genderOption === 'A') {
      return '상관없음';
    } else if (genderOption === 'F') {
      return '여자만';
    } else if (genderOption === 'M') {
      return '남자만';
    }
    
    return genderOption;
  };
</script>

<style lang="scss" scoped>

</style>
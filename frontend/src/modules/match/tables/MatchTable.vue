<template>
  <table class="table table-striped table-hover text-center">
    <thead>
      <tr>
        <th>종목</th>
        <th>지역</th>
        <th>경기일</th>
        <th>시간</th>
        <th>성별</th>
        <th>인원</th>
        <th>취소</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="match in matches" :key="match.id">
        <td>{{ match.sport }}</td>
        <td>{{ match.region }}</td>
        <td>{{ match.matchDate }}</td>
        <td>{{ match.matchTime }}</td>
        <td>{{ getGenderText(match.genderOption) }}</td>
        <td>{{ match.currentCount }}/{{ match.requiredCount }}</td>
        <td>
          <button
            type="button"
            class="btn btn-sm btn-danger"
            @click="$emit('cancel-click', match.id)">
            취소
          </button>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script setup>
  const props = defineProps({
    matches: {
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
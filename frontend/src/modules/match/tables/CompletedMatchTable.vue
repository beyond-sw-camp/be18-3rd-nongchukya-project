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
      <tr v-for="match in completedMatches" :key="match.id">
        <td>{{ match.sport }}</td>
        <td>{{ match.region }}</td>
        <td>{{ match.matchDate }}</td>
        <td>{{ match.matchTime }}</td>
        <td>{{ getGenderText(match.genderOption) }}</td>
        <!-- 인원을 누르면 참가자 확인할 수 있도록 수정 -->
        <td>{{ match.playerCount }}</td>
        <td>
          <button v-if="isCancellable(match.matchDate)"
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
    completedMatches: {
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

  const isCancellable = (dateString) => {
    // 1. 오늘 날짜를 가져와서 시간, 분, 초는 0으로 설정합니다. (날짜만 비교하기 위함)
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    // 2. 파라미터로 받은 경기 날짜(문자열)를 Date 객체로 변환합니다.
    const matchDate = new Date(dateString);

    // 3. 경기 날짜가 오늘보다 크거나 같으면 true를 반환합니다.
    return matchDate >= today;
  };
</script>

<style lang="scss" scoped>

</style>
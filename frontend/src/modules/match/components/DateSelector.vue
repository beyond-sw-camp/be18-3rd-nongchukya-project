<template>
  <header class="date-selector">
    <button class="arrow">&lt;</button>
    <div v-for="date in dates" :key="date.day" class="date-item" :class="{ active: date.active }">
      <div class="day-of-week">{{ date.dayOfWeek }}</div>
      <div class="day">{{ date.day }}</div>
    </div>
    <button class="arrow">&gt;</button>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';

const dates = ref([]);

// 컴포넌트가 마운트될 때 날짜 생성 함수를 실행
onMounted(() => {
  generateDates();
});

/**
 * 오늘 날짜를 기준으로 7일간의 날짜 데이터를 생성하는 함수
 */
const generateDates = () => {
  const dateList = [];
  const today = new Date();
  const koreanDays = ['일', '월', '화', '수', '목', '금', '토'];

  // 오늘을 기준으로 (앞으로 3일, 오늘, 뒤로 3일) 총 7일의 데이터를 만듭니다.
  for (let i = -3; i <= 3; i++) {
    const date = new Date(today);
    date.setDate(today.getDate() + i);

    dateList.push({
      day: String(date.getDate()),
      dayOfWeek: koreanDays[date.getDay()],
      active: i === 0, // i가 0일 때가 오늘이므로 active를 true로 설정
    });
  }

  dates.value = dateList;
};
</script>

<style scoped>
.date-selector {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.arrow {
  background: none;
  border: none;
  font-size: 24px;
  color: #888;
  cursor: pointer;
}
.date-item {
  text-align: center;
  padding: 8px 12px;
  border-radius: 20px;
  cursor: pointer;
}
.date-item.active {
  background-color: #007bff;
  color: white;
}
.day-of-week {
  font-size: 14px;
}
.day {
  font-size: 18px;
  font-weight: bold;
}
</style>
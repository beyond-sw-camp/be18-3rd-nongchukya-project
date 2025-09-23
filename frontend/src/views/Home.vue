<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="home-page">
    <DateSelector 
      :dates="dates" />
    <ImminentMatches
      :imminentMatches="matchStore.imminentMatches"/>
    <DailyMatches
      :dailyMatches="matchStore.dailyMatches"
      @change-page="changePage"/>
  </div>
</template>

<script setup>
import DailyMatches from '@/modules/match/components/DailyMatches.vue';
import DateSelector from '@/modules/match/components/DateSelector.vue';
import ImminentMatches from '@/modules/match/components/ImminentMatches.vue';
import { useMatchStore } from '@/modules/match/stores/matchStore';
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';

  const matchStore = useMatchStore();
  const currentRoute = useRoute();
  const router = useRouter();

  const changePage = ({page, totalPages}) => {
    if(page >= 1 && page <= totalPages) {
      router.push({name: 'matches', query: {page}});
    } 
  };

  onMounted (async () => {
    try {
      matchStore.pageInfo.currentPage = parseInt(currentRoute.query.page) || 1;
      
      await matchStore.fetchImminentMatches();      
      await matchStore.fetchDailyMatches('2025-09-27', matchStore.pageInfo.currentPage, 10);
    } catch (error) {
      console.log(error.response.data);
      const {status, message} = error.response.data;
      
      if(status === 'MATCH_NOT_FOUND') {
        alert(message);

      } else if(status === 'REFRESH_TOKEN_INVALID') {
        router.push({name: 'login'});
      } else if(status === 'INTERNAL_SERVER_ERROR') {
        alert('에러가 발생했습니다.')
      }
    }
  });


</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: linear-gradient(to bottom, #FFA652, #EEA562, #F6EADF);
  font-family: 'Inter', sans-serif;
}
</style>

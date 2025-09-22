import { defineStore } from "pinia";
import { reactive, ref } from "vue";
import api from "@/api/axios";

export const useMatchStore = defineStore('match', () => {
  const matches = ref([]);
  const completedMatches = ref([]);
  const imminentMatches = ref([]);
  const dailyMatches = ref([]);
  const pageInfo = reactive({
    currentPage: 1, // 현재 페이지 번호
    totalCount: 0,  // 전체 데이터 수
    pageLimit: 5,   // 페이지네이션에 보이는 페이지 수
    listLimit: 5    // 한 페이지에 표시될 리스트의 수
  });

  const fetchMatches = async (page, numOfRows) => {
    const response = await api.get(`/api/v1/match-service/me/matches`);

    matches.value = response.data.items;
    pageInfo.totalCount = response.data.totalCount;
    pageInfo.listLimit = numOfRows;
  };

  const cancelMatch = async (applicationId) => {
    const response = await api.delete(`/api/v1/match-service/match-applications/${applicationId}`);

    return response.data;
  };

  const fetchCompletedMatches = async (page, numOfRows) => {
    const response = await api.get('/api/v1/match-service/completed-matches');

    completedMatches.value = response.data.items;
    pageInfo.totalCount = response.data.totalCount;
    pageInfo.listLimit = numOfRows;  
  }

  const fetchImminentMatches = async () => {
    const response = await api.get('/api/v1/match-service/imminent-matches');

    console.log(response.data);

    imminentMatches.value = response.data; 
  }

  const fetchDailyMatches = async (date, page, numOfRows) => {
    const response = await api.get('/api/v1/match-service/matches-by-date', {params: {date:date}});

    dailyMatches.value = response.data.items;
    pageInfo.totalCount = response.data.totalCount;
    pageInfo.listLimit = numOfRows; 
  }

  return { matches, completedMatches, imminentMatches, dailyMatches, pageInfo, 
    fetchMatches, cancelMatch, fetchCompletedMatches, fetchImminentMatches, fetchDailyMatches };
});
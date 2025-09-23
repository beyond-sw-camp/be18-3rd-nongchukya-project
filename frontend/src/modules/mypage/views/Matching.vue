<template>
  <div class="matching-section">
    <h2>매칭 중 경기</h2>

    <div v-if="matches.length === 0">진행 중인 매치가 없습니다.</div>

    <div v-else class="match-list">
      <div v-for="match in paginatedMatches" :key="match.matchId" class="match-card">
        <h3>{{ match.title }}</h3>
        <p><strong>상대:</strong> {{ match.opponentNickname }}</p>
        <p><strong>일정:</strong> {{ formatDate(match.matchDate) }}</p>
        <p><strong>장소:</strong> {{ match.location }}</p>
      </div>

      <div class="pagination" v-if="totalPages > 1">
        <button :disabled="currentPage === 1" @click="prevPage">이전</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button :disabled="currentPage === totalPages" @click="nextPage">다음</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useMatchStore } from '@/modules/match/stores/matchStore'

const matchStore = useMatchStore()
const currentPage = ref(1)
const pageSize = 5

const matches = computed(() => matchStore.matches)

const props = defineProps({
  activeTab: String
})

// activeTab이 'matching'으로 바뀔 때마다 fetch
watch(() => props.activeTab, (newTab) => {
  if (newTab === 'matching') fetchMatchesPage()
})

// 페이지네이션 계산
const totalPages = computed(() => Math.ceil(matches.value.length / pageSize))
const paginatedMatches = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return matches.value.slice(start, end)
})

// 페이지 이동
const prevPage = () => { if (currentPage.value > 1) currentPage.value-- }
const nextPage = () => { if (currentPage.value < totalPages.value) currentPage.value++ }

// 매칭 데이터 fetch
const fetchMatchesPage = async () => {
  await matchStore.fetchMatches(currentPage.value, pageSize)
}

// 페이지 변경 시 fetch
watch(currentPage, fetchMatchesPage)

const formatDate = isoStr => new Date(isoStr).toLocaleString()
</script>

<style scoped>
.matching-section {
  padding: 1rem;
  background: #f9fafb;
  border-radius: 8px;
}

.match-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
}

.match-card {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 1rem;
  background: #fff;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1rem;
}

.pagination button {
  padding: 0.4rem 0.8rem;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  cursor: pointer;
}

.pagination button:disabled {
  background: #e5e7eb;
  cursor: not-allowed;
}
</style>

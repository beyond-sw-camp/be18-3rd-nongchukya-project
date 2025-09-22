<template>
  <div class="post-list">
    <!-- 제목 -->
    <h1 id="title">게시판</h1>

    <!-- 카테고리 -->
    <div class="category-tabs">
      <button
        :class="['category-btn', { active: selectedCategory === '' }]"
        @click="changeCategory('')"
      >
        전체
      </button>
      <button
        v-for="category in categories"
        :key="category"
        :class="['category-btn', { active: selectedCategory === category }]"
        @click="changeCategory(category)"
      >
        {{ category }}
      </button>
    </div>

    <!-- 검색 -->
    <div class="search-box">
      <select v-model="searchType">
        <option value="title">제목</option>
        <option value="titleAndContent">제목+내용</option>
        <option value="author">작성자</option>
      </select>
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="검색어를 입력하세요"
        @keyup.enter="searchPosts"
      />
      <button class="search-btn" @click="searchPosts">검색</button>
      <button v-if="isSearching" class="cancel-btn" @click="clearSearch">검색취소</button>
    </div>

    <!-- 정렬 드롭다운 -->
    <div class="sort-dropdown">
      <label for="sort"></label>
      <select id="sort" v-model="selectedSort" @change="changeSort">
        <option value="latest">최신순</option>
        <option value="oldest">오래된순</option>
        <option value="views">조회수</option>
        <option value="likes">좋아요</option>
        <option value="comments">댓글</option>
      </select>
    </div>

    <!-- 게시글 리스트 -->
    <div class="post-items">
      <div
        class="post-item"
        v-for="post in postStore.posts"
        :key="post.postId"
        @click="postClick(post.postId)"
      >
        <span class="post-title">
          <router-link :to="{ name: 'post-detail', params: { postId: post.postId } }" @click.stop>
            {{ post.title }}
          </router-link>
          <span class="comments">[{{ post.commentCount }}]</span>
        </span>
        <span class="writer">{{ post.userNickname }}</span>
        <span class="date">{{ formatDate(post.createdAt) }}</span>
        <span class="views">👁️{{ post.viewCount }}</span>
        <span class="likes">❤️ {{ post.likeCount }}</span>
      </div>
    </div>

    <!-- 게시글 작성 버튼 -->
    <div class="create-post-btn-container">
      <button class="create-post-btn" @click="goToCreatePost">
        게시글 작성
      </button>
    </div>

    <!-- Pagination -->
    <Pagination
      v-if="postStore.pageInfo.totalCount > 0"
      :page-info="postStore.pageInfo"
      @change-page="changePage"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter, onBeforeRouteUpdate } from 'vue-router';
import { usePostStore } from '../store/postStore';
import Pagination from '@/components/Pagination.vue';

const postStore = usePostStore();
const route = useRoute();
const router = useRouter();

const categories = ref(['자유게시판', '질문게시판', '정보공유게시판']);
const selectedCategory = ref('');

// 정렬 기본값: 최신순
const selectedSort = ref('latest'); // latest, oldest, views, likes, comments
const sortDirection = ref('desc');  // asc / desc

// 검색
const searchType = ref("title");
const searchKeyword = ref("");
const isSearching = ref(false); // 검색 상태 추적

// 날짜 포맷 함수 (시:분:초까지)
const formatDate = (dateStr) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 게시글 목록 로드 공통 함수
const loadPosts = async (page = 1) => {
  if (isSearching.value && searchKeyword.value.trim()) {
    await postStore.searchPosts(
      searchType.value,
      searchKeyword.value,
      page,
      postStore.pageInfo.listLimit,
      selectedSort.value,
      sortDirection.value
    );
  } else {
    await postStore.fetchPosts(
      page,
      postStore.pageInfo.listLimit,
      selectedCategory.value,
      selectedSort.value,
      sortDirection.value
    );
  }
};

// 정렬 변경
const changeSort = async () => {
  await loadPosts(1);
  router.replace({
    name: 'posts',
    query: {
      page: 1,
      category: selectedCategory.value,
      type: isSearching.value ? searchType.value : undefined,
      keyword: isSearching.value ? searchKeyword.value : undefined,
      sortBy: selectedSort.value,
      sortDir: sortDirection.value
    }
  });
};

// 카테고리 변경
const changeCategory = async (category) => {
  selectedCategory.value = category;
  isSearching.value = false; // 카테고리 변경 시 검색 취소
  await loadPosts(1);
  router.push({
    name: 'posts',
    query: { page: 1, category, sortBy: selectedSort.value, sortDir: sortDirection.value },
  });
};

// 검색 실행
const searchPosts = async () => {
  if (!searchKeyword.value.trim()) {
    alert("검색어를 입력해주세요.");
    return;
  }

  isSearching.value = true;
  await loadPosts(1);
  router.push({
    name: "posts",
    query: {
      page: 1,
      type: searchType.value,
      keyword: searchKeyword.value,
      sortBy: selectedSort.value,
      sortDir: sortDirection.value,
    },
  });
};

// 검색 취소
const clearSearch = async () => {
  searchKeyword.value = "";
  isSearching.value = false;
  await loadPosts(1);
  router.push({ name: 'posts', query: { page: 1, category: selectedCategory.value } });
};

// 페이지 변경
const changePage = async ({ page }) => {
  if (page < 1) return;
  await loadPosts(page);
  router.push({
    name: 'posts',
    query: {
      page,
      category: selectedCategory.value,
      type: isSearching.value ? searchType.value : undefined,
      keyword: isSearching.value ? searchKeyword.value : undefined,
      sortBy: selectedSort.value,
      sortDir: sortDirection.value
    }
  });
};

// 게시글 상세 이동
const postClick = (postId) => {
  router.push({ name: 'post-detail', params: { postId } });
};

// 게시글 작성 이동
const goToCreatePost = () => {
  router.push({ name: 'create-post' });
};

// 초기 데이터 로드
onMounted(async () => {
  const currentPage = parseInt(route.query.page) || 1;
  const category = route.query.category || '';
  const sortBy = route.query.sortBy || 'latest';
  const sortDir = route.query.sortDir || 'desc';
  const type = route.query.type || null;
  const keyword = route.query.keyword || null;

  selectedCategory.value = category;
  selectedSort.value = sortBy;
  sortDirection.value = sortDir;

  if (type && keyword) {
    searchType.value = type;
    searchKeyword.value = keyword;
    isSearching.value = true;
    await loadPosts(currentPage);
  } else {
    await loadPosts(currentPage);
  }
});

// 라우트 변경 감지
onBeforeRouteUpdate(async (to) => {
  const currentPage = parseInt(to.query.page) || 1;
  const category = to.query.category || '';
  const sortBy = to.query.sortBy || 'latest';
  const sortDir = to.query.sortDir || 'desc';
  const type = to.query.type || null;
  const keyword = to.query.keyword || null;

  selectedCategory.value = category;
  selectedSort.value = sortBy;
  sortDirection.value = sortDir;

  if (type && keyword) {
    searchType.value = type;
    searchKeyword.value = keyword;
    isSearching.value = true;
    await loadPosts(currentPage);
  } else {
    isSearching.value = false;
    await loadPosts(currentPage);
  }
});
</script>


<style scoped>
/* 모든 select 공통 스타일 */
select {
  appearance: none; /* 기본 브라우저 스타일 제거 */
  -webkit-appearance: none;
  -moz-appearance: none;

  padding: 6px 30px 6px 10px; /* 오른쪽 공간 확보 (화살표) */
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #f9f9f9;
  background-image: url('data:image/svg+xml;utf8,<svg fill="%23666" height="12" viewBox="0 0 24 24" width="12" xmlns="http://www.w3.org/2000/svg"><path d="M7 10l5 5 5-5z"/></svg>');
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 12px;
  cursor: pointer;
  transition: border-color 0.2s, box-shadow 0.2s;
}

select:hover {
  border-color: #007bff;
  box-shadow: 0 0 4px rgba(0, 123, 255, 0.3);
}

select:focus {
  outline: none;
  border-color: #1a73e8;
  box-shadow: 0 0 5px rgba(26, 115, 232, 0.4);
}

/* 옵션 텍스트 스타일 */
select option {
  padding: 6px 10px;
  font-size: 14px;
  color: #333;
}

/* 검색 박스 안 select만 적용 */
.search-box select,
.sort-dropdown select {
  flex-shrink: 0;
  min-width: 120px;
}

.post-title a {
  color: #333 !important;        /* 기본 글자색 검은색 */
  text-decoration: none !important; /* 밑줄 제거 */
  font-weight: 500;               /* 기본 글씨 굵기 */
  transition: font-weight 0.2s, color 0.2s;
}

.post-title a:hover {
  color: #1a73e8 !important;         
  text-decoration: none !important; /* 호버 시 밑줄 제거 */
  font-weight: 600;               /* 호버 시 글씨 굵게 */
}


#title {
  font-size: 35px;
  font-weight: 700;
  color: #333;
  border-bottom: 1px solid #ccc;
  padding-top: 12px;
  padding-bottom: 12px;
  padding-left: 25px;
  margin-bottom: 24px;
  letter-spacing: 1px;
}

.category-tabs {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 24px;
}

.category-btn {
  padding: 8px 20px;
  font-size: 14px;
  font-weight: 500;
  background-color: transparent;
  border: none;
  cursor: pointer;
  color: #555;
  position: relative;
  transition: color 0.2s;
}

.category-btn::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 3px;
  background-color: #007bff;
  transition: width 0.3s;
}

.category-btn:hover {
  color: #007bff;
}

.category-btn.active {
  color: #007bff;
}

.category-btn.active::after {
  width: 100%;
}

.post-items {
  display: flex;
  flex-direction: column;
  padding: 0 20px;
  gap: 8px;
}

.post-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  border-bottom: 1px solid #ccc;
  border-radius: 6px;
  cursor: pointer;
  flex-wrap: wrap;
}

.post-item:hover {
  background-color: #f9f9f9;
}

.post-title {
  flex: 2 1 60%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.comments {
  margin-left: 2px;
  font-weight: normal;
  color: #888;
}

.writer {
  font-size: 13px;
  color: #888;
}

.views,
.date,
.likes {
  flex: 1 1 5%;
  font-size: 12px;
  color: #666;
  text-align: center;
}

.sort-dropdown {
  display: flex;
  justify-content: flex-end;
  margin: 12px 20px;
  align-items: center;
  gap: 8px;
}

.sort-dropdown select {
  padding: 6px 14px;
  font-size: 14px;
  border-radius: 8px;
  border: 1px solid #ccc;
  background-color: #fff;
  cursor: pointer;
}

.create-post-btn-container {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 유지 */
  padding: 20px;
}

/* 버튼 자체에 margin 조정 */
.create-post-btn {
  padding: 8px 16px; /* 약간 줄임 */
  background-color: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
  margin-right: 40px; /* 화면 오른쪽 끝에서 왼쪽으로 이동 */
}

.create-post-btn:hover {
  background-color: #1558b0;
}

.search-box {
  display: flex;
  gap: 8px;
  margin: 12px 20px;
  align-items: center;
  padding: 6px 10px;
  background-color: #fff;
}

/* select */
.search-box select {
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #f9f9f9;
  cursor: pointer;
  flex-shrink: 0; /* 줄어들지 않게 */
}

/* input (좌우로 늘어나도록) */
.search-box input {
  flex-grow: 1;       /* 가능한 공간 모두 차지 */
  min-width: 200px;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

/* 검색 버튼 (항상 파랑색) */
.search-box button.search-btn {
  background-color: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.search-box button.search-btn:hover {
  background-color: #1558b0;
}

/* 검색취소 버튼 (회색) */
.search-box button.cancel-btn {
  background-color: #e0e0e0;  
  color: #333;
  border: none;
  border-radius: 6px;
  padding: 6px 14px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.search-box button.cancel-btn:hover {
  background-color: #bdbdbd;
}

.date {
  font-size: 13px;
  color: #888;
}
</style>

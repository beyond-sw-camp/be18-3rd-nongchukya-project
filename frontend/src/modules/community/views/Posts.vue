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

    <!-- 정렬 드롭다운 -->
    <div class="sort-dropdown">
      <label for="sort"></label>
      <select id="sort" v-model="selectedSort" @change="changeSort(selectedSort)">
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
        <span class="date">{{ post.createdAt }}</span>
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

const changeSort = async () => {
  await postStore.fetchPosts(
    1,
    postStore.pageInfo.listLimit,
    selectedCategory.value,
    selectedSort.value,
    sortDirection.value
  );

  router.replace({
    name: 'posts',
    query: {
      page: 1,
      category: selectedCategory.value,
      sortBy: selectedSort.value,
      sortDir: sortDirection.value
    }
  });
};

// 카테고리 변경
const changeCategory = async (category) => {
  selectedCategory.value = category;
  await postStore.fetchPosts(
    1,
    postStore.pageInfo.listLimit,
    selectedCategory.value,
    selectedSort.value,
    sortDirection.value
  );
  router.push({
    name: 'posts',
    query: { page: 1, category, sortBy: selectedSort.value, sortDir: sortDirection.value },
  });
};

// 게시글 상세 이동
const postClick = (postId) => {
  router.push({ name: 'post-detail', params: { postId } });
};

// 페이지 변경
const changePage = async ({ page }) => {
  if (page < 1) return;
  await postStore.fetchPosts(
    page,
    postStore.pageInfo.listLimit,
    selectedCategory.value,
    selectedSort.value,
    sortDirection.value
  );
  router.push({
    name: 'posts',
    query: { page, category: selectedCategory.value, sortBy: selectedSort.value, sortDir: sortDirection.value },
  });
};

// 게시글 작성 이동
const goToCreatePost = () => {
  router.push({ name: 'create-post' }); // 라우터에 맞춰서 이동
};

// 초기 데이터 로드
onMounted(async () => {
  const currentPage = parseInt(route.query.page) || 1;
  const category = route.query.category || '';
  const sortBy = route.query.sortBy || 'latest';
  const sortDir = route.query.sortDir || 'desc';

  selectedCategory.value = category;
  selectedSort.value = sortBy;
  sortDirection.value = sortDir;

  await postStore.fetchPosts(currentPage, postStore.pageInfo.listLimit, category, sortBy, sortDir);
});

// 라우트 변경 감지
onBeforeRouteUpdate(async (to) => {
  const currentPage = parseInt(to.query.page) || 1;
  const category = to.query.category || '';
  const sortBy = to.query.sortBy || 'latest';
  const sortDir = to.query.sortDir || 'desc';

  selectedCategory.value = category;
  selectedSort.value = sortBy;
  sortDirection.value = sortDir;

  await postStore.fetchPosts(currentPage, postStore.pageInfo.listLimit, category, sortBy, sortDir);
});
</script>

<style scoped>
/* 기존 스타일 그대로 유지 */
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

.writer,
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
  justify-content: flex-end;
  padding: 20px;
}

.create-post-btn {
  padding: 10px 20px;
  background-color: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: background-color 0.2s;
}

.create-post-btn:hover {
  background-color: #1558b0;
}
</style>

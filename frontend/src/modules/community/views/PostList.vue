<template>
    <div class="post-list">
        <!-- 제목 -->
        <h1 id="title">게시판 목록</h1>

        <!-- 카테고리 -->
        <div class="category-tabs">
            <button
                :class="['category-btn', { active: selectedCategory === ''}]"
                @click="selectedCategory=''">
                전체
            </button>

            <button v-for="category in categories" :key="category" 
                :class="['category-btn', { active: selectedCategory === category }]"
                @click="selectedCategory = category">
                {{ category }}
            </button>

        </div>

        <!-- 게시글 리스트 -->
        <div class="post-items">
            <div class="post-item" v-for="post in filteredPosts" :key="post.id">
                <span class="post-title">
                    {{ post.title }}<span class="comments">[{{ post.comments }}]</span>
                </span>
                <span class="writer">{{ post.writer }}</span>
                <span class="date">{{ post.createdAt }}</span>
                <span class="views">👁️{{ post.views }}</span>
                <span class="likes">❤️ {{ post.likes }}</span>
            </div>
        </div>


    </div>
</template>

<script setup>
    import { computed, ref } from 'vue';

    // 카테고리
    const categories = ['자유 게시판', '질문 게시판'];

    const selectedCategory = ref('');

    // 게시글 (더미, 나중에 백엔드 연동)
    const posts = ref([
        { id: 1, title: '첫 번째 게시글', writer: '홍길동', comments: 2, views: 15, createdAt: '2025-09-19', likes: 3, category: '자유 게시판' },
        { id: 2, title: '질문 게시글 예시', writer: '이몽룡', comments: 5, views: 30, createdAt: '2025-09-19', likes: 1, category: '질문 게시판' }
    ]);

    const filteredPosts = computed(() => {
        if( !selectedCategory.value ){
            return posts.value;
        } else {
            return posts.value.filter(post => post.category === selectedCategory.value);
        }

    });

</script>

<style scoped>
#title{
    border-bottom: 1px solid #ccc;
    padding-bottom: 8px;
    margin-bottom: 16px;
}

.category-tabs {
    display: flex;
    justify-content: space-between;
    padding: 0 200px;
    margin-bottom: 24px;
    padding-bottom: 4px;
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
    justify-content: space-between; /* 항목 간 공간 균등 배치 */
    align-items: center;
    padding: 10px 12px;
    border-bottom: 1px solid #ccc;
    border-radius: 6px;
    transition: background 0.2s;
    cursor: pointer;
    flex-wrap: wrap; /* 화면 줄어들면 항목 줄바꿈 */
}

.post-item:hover {
    background-color: #f9f9f9;
}

/* span별 flex 비율 설정 */
.post-title {
    flex: 2 1 60%; /* 제목은 넓게, 최소 40% 확보 */
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.comments {
    margin-left: 2px; /* 약간의 간격만 */
    font-weight: normal;
    color: #888;
}

.writer,
.views,
.date,
.likes {
    flex: 1 1 5%; /* 나머지 정보 균등 분배 */
    font-size: 12px;
    color: #666;
    white-space: nowrap;
    text-align: center;
    position: relative;
}

</style>
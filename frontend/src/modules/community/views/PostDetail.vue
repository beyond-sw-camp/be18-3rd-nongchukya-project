<template>
  <div class="post-detail-container">
    <!-- 게시글 카드 -->
    <div class="post-card">
      <!-- 제목 -->
      <h1 class="post-title">{{ post.title }}</h1>

      <!-- 작성자 정보 -->
      <div class="post-author-info">
        <div class="author-left">
          <img v-if="post.profileImg" :src="post.profileImg" alt="프로필" class="profile-img" />
          <span class="author-name">{{ post.userNickname }}</span>
        </div>
        <span class="updated-time">{{ formatDate(post.updatedAt) }}</span>
      </div>

      <!-- 내용 -->
      <div class="post-content">{{ post.content }}</div>

      <!-- 작성자 버튼 -->
      <div class="post-actions-author" v-if="post.author">
        <button @click="$router.push({ name: 'update-post', params: { postId } })">✏ 수정</button>
        <button @click="deletePost">🗑 삭제</button>
      </div>

      <!-- 좋아요 버튼 중앙 -->
      <div class="post-actions">
        <button @click="toggleLike" class="like-btn">
          <span :class="{'liked': post.isLiked}">❤️</span>
          <span class="like-count">{{ post.likeCount || 0 }}</span>
        </button>
      </div>

      <!-- 첨부파일 -->
      <div class="attachments" v-if="post.attachments?.length">
        <a
          v-for="file in post.attachments"
          :key="file.attachmentId"
          :href="`http://localhost:8080${file.fileUrl}`" target="_blank" rel="noopener"
        >
          📎 {{ file.originalName }}
        </a>
      </div>
    </div>

    <!-- 댓글 입력 -->
    <div class="comment-input">
      <input v-model="newComment" placeholder="댓글을 입력해주세요." />
      <button @click="addComment">등록</button>
    </div>

    <!-- 댓글 리스트 -->
    <ul class="comments-section">
      <CommentNode
        v-for="comment in comments"
        :key="comment.commentId"
        :comment="comment"
        :postId="postId"
        :token="token"
        @refresh-comments="fetchPost"
      />
    </ul>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";
import CommentNode from "../components/CommentNode.vue";

export default {
  components: { CommentNode },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const postId = route.params.postId;

    const post = ref({});
    const comments = ref([]);
    const newComment = ref("");
    const token = localStorage.getItem("accessToken");

    const formatDate = (dateStr) => new Date(dateStr).toLocaleString();

    const fetchPost = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        post.value = Array.isArray(res.data.items) ? res.data.items[0] : res.data.items;
        comments.value = (post.value.comments || []).map((c) => ({ ...c, replies: c.replies || [] }));
      } catch (err) {
        console.error("게시글 로딩 실패", err);
      }
    };

    const editPost = () => {
      router.push(`/posts/${postId}/edit`);
    };

    const deletePost = async () => {
      if (!confirm("정말 삭제하시겠습니까?")) return;
      try {
        await axios.delete(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        alert("게시글이 삭제되었습니다.");
        router.push("/");
      } catch (err) {
        console.error("게시글 삭제 실패", err);
      }
    };

    const addComment = async () => {
      if (!newComment.value.trim()) return;
      try {
        const res = await axios.post(
          `http://localhost:8080/api/v1/community/posts/${postId}/comments`,
          { content: newComment.value },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        comments.value.push(res.data);
        newComment.value = "";
      } catch (err) {
        console.error("댓글 작성 실패", err);
      }
    };

    const toggleLike = async () => {
      try {
        await axios.post(
          `http://localhost:8080/api/v1/community/posts/${postId}/like`,
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );
        fetchPost();
      } catch (err) {
        console.error("좋아요 실패", err);
      }
    };

    onMounted(fetchPost);

    return {
      post,
      comments,
      newComment,
      postId,
      token,
      formatDate,
      fetchPost,
      addComment,
      toggleLike,
      editPost,
      deletePost,
    };
  },
};
</script>

<style scoped>
.post-detail-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Noto Sans', sans-serif;
}

.post-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
  padding: 25px 30px;
  margin-bottom: 30px;
  transition: transform 0.2s;
}

.post-card:hover {
  transform: translateY(-2px);
}

.post-title {
  font-size: 28px;
  font-weight: 700;
  color: #222;
  border-bottom: 2px solid #eee;
  padding-bottom: 12px;
  margin-bottom: 15px;
}

/* 작성자 정보 */
.post-author-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 14px;
  color: #888;
}

.author-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile-img {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  object-fit: cover;
}

.author-name {
  font-weight: 500;
  color: #333;
}

.updated-time {
  font-size: 13px;
  color: #aaa;
}

/* 내용 */
.post-content {
  font-size: 16px;
  line-height: 1.6;
  color: #444;
  margin-bottom: 20px;
  min-height: 250px;
  max-height: 400px;
  overflow-y: auto;
}

/* 작성자 버튼 */
.post-actions-author {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-bottom: 15px;
}

.post-actions-author button {
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  color: #555;
  transition: color 0.2s;
}

.post-actions-author button:hover {
  color: #1a73e8;
}

/* 좋아요 버튼 */
.post-actions {
  display: flex;
  justify-content: center;
  margin-bottom: 25px;
}

.like-btn {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 22px;
  background-color: #ffe6e6;
  color: #e74c3c;
  border: none;
  border-radius: 50px;
  padding: 12px 24px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
}

.like-btn:hover {
  transform: scale(1.1);
  background-color: #ffcccc;
}

.like-btn .liked {
  animation: heartbeat 0.3s ease;
}

.like-count {
  font-size: 16px;
}

@keyframes heartbeat {
  0% { transform: scale(1); }
  25% { transform: scale(1.3); }
  50% { transform: scale(1); }
  75% { transform: scale(1.2); }
  100% { transform: scale(1); }
}

/* 첨부파일 */
.attachments {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 25px;
}

.attachments a {
  background: #f2f2f2;
  padding: 6px 12px;
  border-radius: 6px;
  color: #555;
  text-decoration: none;
  font-size: 14px;
  transition: background 0.2s;
}

.attachments a:hover {
  background: #e0e0e0;
}

/* 댓글 입력 */
.comment-input {
  display: flex;
  gap: 12px;
  margin-bottom: 30px;
}

.comment-input input {
  flex: 1;
  padding: 12px 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.comment-input button {
  padding: 12px 20px;
  font-size: 14px;
  border: none;
  background-color: #1a73e8;
  color: #fff;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.comment-input button:hover {
  background-color: #1558b0;
}

.comments-section {
  list-style: none;
  padding: 0;
}
</style>
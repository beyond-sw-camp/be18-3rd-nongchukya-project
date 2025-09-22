<template>
  <div class="post-detail-container">
    <!-- 게시글 -->
    <div class="post-card">
      <h1 class="post-title">{{ post.title }}</h1>
      <div class="post-content">{{ post.content }}</div>
      <div class="post-meta">
        <span>작성자: {{ post.userNickname }}</span>
        <span>{{ formatDate(post.updatedAt) }}</span>
      </div>

      <!-- 작성자 버튼 -->
      <div class="post-actions-author" v-if="post.author">
        <button
          v-if="post.author"
          @click="$router.push({ name: 'update-post', params: { postId } })"
        >
          수정
        </button>
        <button @click="deletePost">🗑 삭제</button>
      </div>
    </div>

    <!-- 좋아요 & 첨부파일 -->
    <div class="post-actions">
      <button @click="toggleLike" class="like-btn">
        👍 좋아요 ({{ post.likeCount || 0 }})
      </button>
      <div v-if="post.attachments?.length" class="attachments">
        <a
          v-for="file in post.attachments"
          :key="file.attachmentId"
          :href="file.fileUrl"
          target="_blank"
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

    // 게시글 + 댓글 불러오기
    const fetchPost = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        // post.value = res.data.items[0];
        post.value = Array.isArray(res.data.items) ? res.data.items[0] : res.data.items;

        comments.value = (post.value.comments || []).map((c) => ({
          ...c,
          replies: c.replies || [],
        }));
      } catch (err) {
        console.error("게시글 로딩 실패", err);
      }
    };

    // 게시글 수정
    const editPost = () => {
      router.push(`/posts/${postId}/edit`);
    };

    // 게시글 삭제
    const deletePost = async () => {
      if (!confirm("정말 삭제하시겠습니까?")) return;
      try {
        await axios.delete(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        alert("게시글이 삭제되었습니다.");
        router.push("/"); // 삭제 후 홈으로 이동
      } catch (err) {
        console.error("게시글 삭제 실패", err);
      }
    };

    // 댓글 작성
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

    // 좋아요
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
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}
.post-card {
  border: 1px solid #ccc;
  padding: 20px;
  margin-bottom: 20px;
}
.post-title {
  font-size: 24px;
  border-bottom: 1px#ccc;
  margin-bottom: 10px;
}
.post-content {
  min-height: 200px;
  margin-bottom: 10px;
}
.post-meta {
  font-size: 12px;
  color: gray;
  display: flex;
  align-items: center;
  gap: 10px;
}
.profile-img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}
.post-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}
.post-actions-author button {
  border: none;
  background: none;
  font-size: 18px;
  cursor: pointer;
}
.comment-input {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  width: 100%;
}
.comment-input input {
  flex: 1;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
}
.comment-input button {
  padding: 10px 15px;
  font-size: 14px;
  border: none;
  background-color: #1a73e8;
  color: white;
  border-radius: 6px;
  cursor: pointer;
}
.comments-section {
  list-style: none;
  padding: 0;
}
</style>

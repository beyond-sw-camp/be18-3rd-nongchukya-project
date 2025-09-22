<template>
  <li class="comment-item">
    <div class="comment-main">
      <span v-if="isReply" class="arrow">→</span>
      <img class="avatar" :src="comment.userProfileImage || defaultAvatar" />
      <div class="comment-content-wrapper">
        <div class="comment-header">
          <strong>{{ comment.userNickname }}</strong>
        </div>

        <template v-if="editing">
          <input v-model="editContent" class="edit-input" />
          <button @click="updateComment">💾</button>
          <button @click="cancelEdit">✖</button>
        </template>
        <template v-else>
          <div class="comment-body">{{ comment.content }}</div>
          <div class="comment-footer">
            <span class="date">{{ formatDate(comment.createdAt) }}</span>
            <button @click="toggleReplyInput">↩</button>
            <button @click="startEdit">✏</button>
            <button @click="deleteComment">🗑</button>
          </div>
        </template>
      </div>
    </div>

    <!-- 답글 입력 -->
    <div v-if="showReplyInput" class="reply-input">
      <input v-model="newReply" placeholder="댓글을 입력해주세요." />
      <button @click="addReply">등록</button>
    </div>

    <!-- 대댓글 리스트 재귀 -->
    <ul v-if="comment.replies?.length" class="replies-list">
      <CommentNode
        v-for="reply in comment.replies"
        :key="reply.commentId"
        :comment="reply"
        :post-id="postId"
        :token="token"
        :is-reply="true"
        @refresh-comments="$emit('refresh-comments')"
      />
    </ul>
  </li>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import CommentNode from "./CommentNode.vue";

const props = defineProps({
  comment: Object,
  postId: [Number, String],
  token: String,
  isReply: { type: Boolean, default: false },
});

const emit = defineEmits(["refresh-comments"]);

const showReplyInput = ref(false);
const newReply = ref("");
const editing = ref(false);
const editContent = ref("");

const defaultAvatar = "https://via.placeholder.com/40";
const formatDate = (dateStr) => new Date(dateStr).toLocaleString();

const toggleReplyInput = () => (showReplyInput.value = !showReplyInput.value);

const addReply = async () => {
  if (!newReply.value.trim()) return;
  try {
    const res = await axios.post(
      `http://localhost:8080/api/v1/community/posts/${props.postId}/comments/${props.comment.commentId}/replies`,
      { content: newReply.value },
      { headers: { Authorization: `Bearer ${props.token}` } }
    );
    if (!props.comment.replies) props.comment.replies = [];
    props.comment.replies.push(res.data);
    newReply.value = "";
    showReplyInput.value = false;
  } catch (err) {
    console.error("대댓글 작성 실패", err);
  }
};

const startEdit = () => {
  editing.value = true;
  editContent.value = props.comment.content;
};
const cancelEdit = () => {
  editing.value = false;
  editContent.value = "";
};
const updateComment = async () => {
  try {
    await axios.put(
      `http://localhost:8080/api/v1/community/posts/${props.postId}/comments/${props.comment.commentId}`,
      { content: editContent.value },
      { headers: { Authorization: `Bearer ${props.token}` } }
    );
    props.comment.content = editContent.value;
    cancelEdit();
  } catch (err) {
    console.error("댓글 수정 실패", err);
  }
};

const deleteComment = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  try {
    await axios.delete(
      `http://localhost:8080/api/v1/community/posts/${props.postId}/comments/${props.comment.commentId}`,
      { headers: { Authorization: `Bearer ${props.token}` } }
    );
    emit("refresh-comments");
  } catch (err) {
    console.error("댓글 삭제 실패", err);
  }
};
</script>

<style scoped>
.comments-list,
.replies-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.comment-item {
  margin-bottom: 10px;
}

.comment-main {
  display: flex;
  gap: 10px;
  align-items: flex-start;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.comment-content-wrapper {
  flex: 1;
}

.comment-header {
  font-weight: bold;
  margin-bottom: 5px;
}

.comment-body {
  margin-bottom: 5px;
  line-height: 1.5;
}

.comment-footer {
  display: flex;
  gap: 5px;
  font-size: 12px;
  color: gray;
}

.comment-footer button {
  background: none;       /* 배경 제거 */
  border: none;           /* 테두리 제거 */
  cursor: pointer;
  font-size: 14px;
  padding: 0;
  color: gray;            /* 기본 색상 */
  transition: color 0.2s;
}

.comment-footer button:hover {
  color: #007bff;        /* hover 시 파란색 강조 */
}

.replies-list {
  margin-top: 10px;
  margin-left: 50px;
}

.arrow {
  font-weight: bold;
  margin-right: 5px;
}

.reply-input {
  display: flex;
  gap: 10px;
  margin-top: 5px;
}

.edit-input {
  width: 70%;
  padding: 4px;
}
</style>

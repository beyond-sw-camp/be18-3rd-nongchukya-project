<template>
  <li class="comment-item">
    <div class="comment-main">
      <span v-if="isReply" class="arrow">↪</span>
      <img class="avatar" :src="comment.userProfileImage || defaultAvatar" />
      <div class="comment-content-wrapper">
        <div class="comment-header">
          <strong>{{ comment.userNickname }}</strong>
          <span class="date">{{ formatDate(comment.createdAt) }}</span>
        </div>

        <template v-if="editing">
          <input v-model="editContent" class="edit-input" />
          <div class="edit-actions">
            <button class="save-btn" @click="updateComment">💾</button>
            <button class="cancel-btn" @click="cancelEdit">✖</button>
          </div>
        </template>

        <template v-else>
          <div class="comment-body">{{ comment.content }}</div>
          <div class="comment-footer">
            <button @click="toggleReplyInput">↩ 답글</button>
            <button @click="startEdit">✏ 수정</button>
            <button @click="deleteComment">🗑 삭제</button>
          </div>
        </template>
      </div>
    </div>

    <!-- 답글 입력 -->
    <div v-if="showReplyInput" class="reply-input">
      <input v-model="newReply" placeholder="댓글을 입력하세요" />
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

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  const h = String(date.getHours()).padStart(2, "0");
  const min = String(date.getMinutes()).padStart(2, "0");
  return `${y}-${m}-${d} ${h}:${min}`;
};

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
.replies-list{
  list-style: none; /* 동그라미, 네모 제거 */
  padding-left: 0;  /* 들여쓰기 제거 */
  margin: 0;
}

.comment-item {
  margin-bottom: 12px;
}

.comment-main {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  padding: 10px;
  border-radius: 8px;
  background-color: #fafafa;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: background-color 0.2s;
}

.comment-main:hover {
  background-color: #f0f4ff;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  flex-shrink: 0;
}

.comment-content-wrapper {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
  font-weight: 600;
}

.date {
  font-size: 12px;
  color: #888;
}

.comment-body {
  margin-bottom: 6px;
  line-height: 1.5;
  word-break: break-word;
}

.comment-footer {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: #555;
}

.comment-footer button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 13px;
  padding: 0;
  color: #555;
  transition: color 0.2s;
}

.comment-footer button:hover {
  color: #1a73e8;
}

.replies-list {
  margin-top: 10px;
  margin-left: 50px;
  /* border-left: 2px solid #e0e0e0; */
  padding-left: 10px;
}

.arrow {
  font-weight: bold;
  margin-right: 6px;
  color: #1a73e8;
}

.reply-input {
  display: flex;
  gap: 10px;
  margin-top: 6px;
}

.reply-input input {
  flex: 1;
  padding: 6px 10px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.reply-input button {
  padding: 6px 14px;
  border-radius: 6px;
  border: none;
  background-color: #1a73e8;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.2s;
}

.reply-input button:hover {
  background-color: #1558b0;
}

.edit-input {
  width: 70%;
  padding: 6px 8px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.edit-actions {
  display: inline-flex;
  gap: 6px;
  margin-top: 4px;
}

.save-btn,
.cancel-btn {
  background: #1a73e8;
  color: #fff;
  border: none;
  padding: 4px 8px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  transition: background-color 0.2s;
}

.cancel-btn {
  background-color: #e0e0e0;
  color: #333;
}

.save-btn:hover {
  background-color: #1558b0;
}

.cancel-btn:hover {
  background-color: #bdbdbd;
}

</style>

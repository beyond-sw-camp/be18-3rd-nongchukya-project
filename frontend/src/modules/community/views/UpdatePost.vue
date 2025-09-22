<template>
  <div class="update-post-container">
    <h1>게시글 수정</h1>

    <!-- 카테고리 선택 -->
    <div class="form-group">
      <label for="category">카테고리</label>
      <select id="category" v-model="categoryId">
        <option value="">카테고리를 선택하세요</option>
        <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
      </select>
    </div>

    <!-- 제목 -->
    <div class="form-group">
      <label for="title">제목</label>
      <input id="title" v-model="title" type="text" placeholder="제목을 입력하세요" />
    </div>

    <!-- 내용 -->
    <div class="form-group">
      <label for="content">내용</label>
      <textarea id="content" v-model="content" placeholder="내용을 입력하세요"></textarea>
    </div>

    <!-- 첨부파일 -->
    <div class="form-group">
      <label>첨부파일</label>
      <input type="file" multiple @change="handleFilesUpload" />
      <ul>
        <li v-for="(file, index) in existingAttachments" :key="file.attachmentId">
          <a :href="file.fileUrl" target="_blank">{{ file.originalName }}</a>
          <button type="button" @click="removeExistingAttachment(index)">삭제</button>
        </li>
        <li v-for="(file, index) in newFiles" :key="index">{{ file.name }}</li>
      </ul>
    </div>

    <button @click="updatePost">저장</button>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import axios from "axios";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const postId = route.params.postId;
    const token = localStorage.getItem("accessToken");

    const title = ref("");
    const content = ref("");
    const categoryId = ref("");
    const categories = ref([
      { id: 1, name: "자유게시판" },
      { id: 2, name: "질문게시판" },
      { id: 3, name: "정보공유게시판" },
    ]);
    const existingAttachments = ref([]);
    const newFiles = ref([]);
    const deletedAttachmentIds = ref([]); // 삭제할 첨부파일 ID 저장

    // 기존 게시글 불러오기
    const fetchPost = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );
        const post = res.data.items[0];
        title.value = post.title;
        content.value = post.content;
        categoryId.value = post.categoryId; // 백엔드에서 내려주는 categoryId
        existingAttachments.value = post.attachments || [];
      } catch (err) {
        console.error("게시글 로딩 실패", err);
      }
    };

    // 파일 선택
    const handleFilesUpload = (e) => {
      newFiles.value = Array.from(e.target.files);
    };

    // 기존 첨부파일 삭제
    const removeExistingAttachment = (index) => {
      deletedAttachmentIds.value.push(existingAttachments.value[index].attachmentId);
      existingAttachments.value.splice(index, 1);

      console.log("deletedAttachmentIds:", deletedAttachmentIds.value); // 삭제 대상 확인
    };

    // 게시글 수정
    const updatePost = async () => {
      try {
        const formData = new FormData();
        const updateData = {
          title: title.value,
          content: content.value,
          categoryId: categoryId.value,
          attachmentIds: existingAttachments.value.map((f) => f.attachmentId),
          deleteAttachmentIds: deletedAttachmentIds.value,
        };

        formData.append(
          "updatePostRequestDto",
          new Blob([JSON.stringify(updateData)], { type: "application/json" })
        );

        newFiles.value.forEach((file) => formData.append("files", file));

        // 디버깅용
        console.log("updateData:", updateData);
        for (const pair of formData.entries()) {
          console.log(pair[0], pair[1]);
        }

        await axios.put(
          `http://localhost:8080/api/v1/community/posts/${postId}`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "multipart/form-data",
            },
          }
        );

        alert("게시글이 수정되었습니다.");
        router.push(`/community/posts/${postId}`);
      } catch (err) {
        console.error("게시글 수정 실패", err);
        alert("게시글 수정에 실패했습니다.");
      }
    };

    onMounted(fetchPost);

    return {
      title,
      content,
      categoryId,
      categories,
      existingAttachments,
      newFiles,
      deletedAttachmentIds,
      handleFilesUpload,
      removeExistingAttachment,
      updatePost,
    };
  },
};
</script>

<style scoped>
.update-post-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

input[type="text"],
textarea,
select {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  border-radius: 6px;
  border: 1px solid #ccc;
}

button {
  padding: 10px 20px;
  background-color: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
</style>

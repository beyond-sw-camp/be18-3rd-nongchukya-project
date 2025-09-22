<template>
  <div class="create-post-container">
    <h1>게시글 작성</h1>

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
        <li v-for="(file, index) in newFiles" :key="index">{{ file.name }}</li>
      </ul>
    </div>

    <button @click="submitPost">작성 완료</button>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";

const router = useRouter();
const token = localStorage.getItem("accessToken");

const title = ref("");
const content = ref("");
const categoryId = ref("");
const categories = ref([
  { id: 1, name: "자유게시판" },
  { id: 2, name: "질문게시판" },
  { id: 3, name: "정보공유게시판" },
]);
const newFiles = ref([]);

// 첨부파일 선택
const handleFilesUpload = (e) => {
  newFiles.value = Array.from(e.target.files);
};

// 게시글 작성
const submitPost = async () => {
  try {
    const formData = new FormData();
    const postData = {
      title: title.value,
      content: content.value,
      categoryId: categoryId.value
    };

    // ⚠️ 서버에서 @RequestPart("postRequestDto") 이므로 이름 정확히 맞춰야 함
    formData.append(
      "postRequestDto",
      new Blob([JSON.stringify(postData)], { type: "application/json" })
    );

    newFiles.value.forEach(file => formData.append("files", file)); // 서버에서도 files로 받고 있음

    // 디버깅용
    for (const pair of formData.entries()) {
      console.log(pair[0], pair[1]);
    }

    await axios.post(
      "http://localhost:8080/api/v1/community/posts",
      formData,
      {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data"
        }
      }
    );

    alert("게시글이 작성되었습니다.");
    router.push("/community/posts");

  } catch (err) {
    console.error("게시글 작성 실패", err);
    alert("게시글 작성에 실패했습니다.");
  }
};

</script>

<style scoped>
.create-post-container {
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

textarea {
  min-height: 120px;
}

button {
  padding: 10px 20px;
  background-color: #1a73e8;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

button:hover {
  background-color: #1558b0;
}
</style>

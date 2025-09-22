<template>
    <div class="friends-container">
        <header class="header">
            <h1 class="title">친구 목록</h1>
            <p class="subtitle">{{ friendsStore.friend_list.length }}명의 친구</p>
    </header>

    <!-- 검색창 -->
    <div class="search-container">
        <input
            v-model="searchQuery"
            type="text"
            placeholder="친구 검색..."
            class="search-input"
        />
    </div>

    <!-- 친구 카드 -->
    <div class="friends-grid">
        <div
            v-for="friend in filteredFriends"
            :key="friend.friend_id"
            class="friend-card"
        >
            <div class="profile-section">
                <div class="profile-image-container">
                    <img
                        :src="friend.profileImage || defaultProfileImage"
                        :alt="`${friend.nickname} 프로필`"
                        class="profile-image"
                    />
                </div>
        
                <div class="friend-info">
                    <h3 class="nickname">{{ friend.nickname }}</h3>
                    <p class="friend-date">{{ formatFriendDate(friend.createdAt) }}</p>
                </div>
            </div>

        <div class="actions">

            <button
                class="btn-with-img"
                @click="startChat(friend.nickname)"
                title="1:1 채팅"
            >
                <img src="@/assets/chatIcon.png" alt="채팅"/>
            </button>

            <button
                type="button" class="btn-close" aria-label="Close"
                @click="deleteFriend(friend.userId)"
                title="친구 삭제"
            >
            </button>
            </div>
        </div>
    </div>

    <!-- 검색 결과 없음 -->
        <div v-if="filteredFriends.length === 0" class="empty-state">
            <p>검색 결과가 없습니다.</p>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import defaultProfileImage from "@/assets/default_profile.png"
import { useFriendsStore } from "../stores/friendsStore";
import api from "@/api/axios";
import { useRouter } from "vue-router";

const friendsStore = useFriendsStore();
const searchQuery = ref("");
const router = useRouter();

// 스토어에서 친구 데이터 가져오기
onMounted(async () => {
    await friendsStore.getFriendsList();
});

// store 데이터 참조
const friends = computed(() => friendsStore.friend_list);

// 검색 필터
const filteredFriends = computed(() => {
    if (!searchQuery.value.trim()) return friends.value;
        return friend.value.filter(f =>
    f.nickname.includes(searchQuery.value.trim())
    );
});

// 날짜 포맷
const formatFriendDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString("ko-KR", {
        year: "numeric",
        month: "short",
        day: "numeric",
    });
};

// 친구 삭제
const deleteFriend = async (userId) => {
    if (confirm('정말로 삭제하시겠습니까?')) {
        const result = await friendsStore.deleteFriend(userId);

        if (result.code === 200) {
            alert('정상적으로 삭제되었습니다.');

            await friendsStore.getFriendsList();
        };
    };
};

// 1:1 채팅
async function startChat(otherNickname){
    try {
        const {data} = await api.post(`/api/v1/chatrooms/private/create`, null, { params: {otherNickname}});

        const roomId = data?.items?.[0];
        if(!roomId) throw new Error('roomId가 없습니다.');
        router.push({ path: '/chatrooms/list', query: { roomId } });
    } catch (error) {
        const res = error.response?.data;
        if (res?.status === 'USER_NOT_FOUND') {
        alert(res.message);
        } else {
        console.error("채팅방 생성 실패:", error);
        alert("채팅방을 생성할 수 없습니다.");
        }
    }
};
</script>

<style scoped>
/* 간단한 스타일 (예시) */
.friends-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 1rem;
}
.header {
    text-align: center;
    margin-bottom: 1rem;
}
.search-container {
    margin-bottom: 1rem;
}
.search-input {
    width: 100%;
    padding: 0.5rem;
}
.friends-grid {
    display: grid;
    gap: 1rem;
}
.friend-card {
    position: relative;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: #f9f9f9;
    padding: 0.75rem;
    border-radius: 8px;
}
.profile-section {
    display: flex;
    align-items: center;
}
.profile-image-container {
    width: 50px;
    height: 50px;
    margin-right: 0.75rem;
}
.profile-image {
    width: 100%;
    height: 100%;
    border-radius: 50%;
    object-fit: cover;
}
.actions {
    display: flex;
    gap: 0.5rem;
}
.btn-with-img {
    border: none;
    background: transparent;
    padding: 0;  /* 버튼 기본 여백 제거 */
    cursor: pointer;
    margin-right: 20px;
}
.btn-with-img img {
    width: 35px;   /* 이미지 크기 */
    height: 35px;
}
.btn-close {
    position: absolute;
    top: 8px;
    right: 8px;
    width: 5px;   /* 이미지 크기 */
    height: 5px;
    --bs-btn-close-color: red;
}
.empty-state {
    text-align: center;
    margin-top: 1rem;
    color: gray;
}
</style>
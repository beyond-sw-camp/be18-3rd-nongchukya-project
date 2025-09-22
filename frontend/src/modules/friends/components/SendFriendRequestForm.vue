<template>
    <div class="min-h-screen bg-gray-50 py-8">
        <div class="max-w-4xl mx-auto px-4">
            <h1 class="text-3xl font-bold text-gray-800 mb-8 text-center">친구 요청</h1>
        </div>
        <!-- 검색창 -->
        <div class="search-container">
        <input
            v-model="searchQuery"
            type="text"
            placeholder="유저 검색..."
            class="search-input"
        />
        </div>

        <div class="users-grid">
            <div
                v-for="user in filteredUsers"
                :key="user.userId"
                class="user-card"
            >
                <div class="profile-section">
                    <div class="profile-image-container">
                        <img
                            :src="user.profileImage || defaultProfileImage"
                            :alt="`${user.nickname} 프로필`"
                            class="profile-image"
                        />
                    </div>
            
                    <div class="user-info">
                        <h3 class="nickname">{{ user.nickname }}</h3>
                    </div>
                </div>

                <div class="actions">
                    <button type="button" class="btn btn-primary"
                        @click="sendFriendRequest(user.userId)">친구 요청</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, computed, onMounted, watch } from "vue";
    import { useUsersStore } from "../stores/usersStore";
    import { useFriendRequestsStore } from "../stores/friendRequestsStore";
    import defaultProfileImage from "@/assets/defaultProfileImage.png"

    const usersStore = useUsersStore();
    const friendRequestsStore = useFriendRequestsStore();
    const searchQuery = ref("");
    const users = computed(() => usersStore.users);

    onMounted(async () => {
        await usersStore.getSearchUsersByNickname();
    });

    watch(searchQuery, async (newVal) => {
        if (newVal.trim()) {
            await usersStore.getSearchUsersByNickname(newVal);
        } else {
            usersStore.users = []; // 검색어 없으면 비우기
        }
    });

    const filteredUsers = computed(() => users.value);

    const sendFriendRequest = async (userId) => {
        const result = await friendRequestsStore.sendFriendRequest(userId);

        if (result.code === 200) {
            alert('친구 요청을 보냈습니다.');
            
            if (searchQuery.value.trim()) {
                await usersStore.getSearchUsersByNickname(searchQuery.value);
            }
        };
    }
</script>

<style lang="scss" scoped>
    .search-container {
        margin-bottom: 1rem;
    }
    .search-input {
        width: 100%;
        padding: 0.5rem;
    }
    .users-grid {
        display: grid;
        gap: 1rem;
    }
    .user-card {
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
</style>
<template>
<div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4">
        <h1 class="text-3xl font-bold text-gray-800 mb-8 text-center">친구 요청</h1>
    </div>

        <!-- 탭 메뉴 -->
<div>
    <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
        <input type="radio" class="btn-check" id="Received" value="received" v-model="activeTab">
        <label class="btn btn-outline-primary" for="Received">받은 요청</label>

        <input type="radio" class="btn-check" id="Sent" value="sent" v-model="activeTab">
        <label class="btn btn-outline-primary" for="Sent">보낸 요청</label>
    </div>

    <div v-if="activeTab === 'sent'" class="friends-grid mt-3">
        <div
            v-for="friendRequest in friendRequests"
            :key="friendRequest.request_id"
            class="friend-card"
        >
            <div class="profile-section">
                <div class="profile-image-container">
                    <img
                        :src="friendRequest.profileImage || defaultProfileImage"
                        :alt="`${friendRequest.nickname} 프로필`"
                        class="profile-image"
                    />
                </div>
        
                <div class="friend-info">
                    <h3 class="nickname">{{ friendRequest.nickname }}</h3>
                    <p class="request-date">{{ formatFriendDate(friendRequest.createdAt) }}</p>
                </div>
                <div class="actions">
                    <button type="button" class="btn btn-danger" 
                        @click="deleteSentFriendRequests(friendRequest.userId)"
                        title="요청 철회">취소</button>

                </div>
            </div>
        </div>
    </div>


    <!-- 받은 요청 목록 -->
    <div v-else class="friends-grid mt-3">
        <div
            v-for="friendRequest in friendRequests"
            :key="friendRequest.request_id"
            class="friend-card"
        >
            <div class="profile-section">
                <div class="profile-image-container">
                    <img
                        :src="friendRequest.profileImage || defaultProfileImage"
                        :alt="`${friendRequest.nickname} 프로필`"
                        class="profile-image"
                    />
                </div>
        
                <div class="friend-info">
                    <h3 class="nickname">{{ friendRequest.nickname }}</h3>
                    <p class="request-date">{{ formatFriendDate(friendRequest.createdAt) }}</p>
                </div>

                <div class="actions">
                    <button type="button" class="btn btn-primary"
                        @click="acceptReceivedFriendRequests(friendRequest.userId)">수락</button>
                    <button type="button" class="btn btn-danger"
                        @click="deleteReceivedFriendRequests(friendRequest.userId)"
                        title="요청 거절">거절</button>

                </div>
            </div>
        </div>
    </div>
</div>
</div>
</template>

<script setup>
    import { ref, computed } from 'vue';
    import defaultProfileImage from "@/assets/default_profile.png";
    import { useFriendRequestsStore } from '../stores/friendRequestsStore';

    const props = defineProps({
        receivedRequests: { type: Array, default: () => [] },
        sentRequests: { type: Array, default: () => [] },
    });

    const activeTab = ref("received");
    const friendRequestsStore = useFriendRequestsStore();

    const friendRequests = computed(() => {
        return activeTab.value === "sent"
            ? props.sentRequests
            : props.receivedRequests;
    });

    const formatFriendDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString("ko-KR", {
            year: "numeric",
            month: "short",
            day: "numeric",
        });
    };

    const deleteSentFriendRequests = async (userId) => {
        if (confirm('정말로 삭제하시겠습니까?')) {
            const result = await friendRequestsStore.deleteSentFriendRequests(userId);

            if (result.code === 200) {
                alert('정상적으로 삭제되었습니다.');

                await friendRequestsStore.getSentFriendRequests();
            };
        };
    };

    const deleteReceivedFriendRequests = async (userId) => {
        if (confirm('정말로 삭제하시겠습니까?')) {
            const result = await friendRequestsStore.deleteReceivedFriendRequests(userId);

            if (result.code === 200) {
                alert('정상적으로 삭제되었습니다.');

                await friendRequestsStore.getReceivedFriendRequests();
            };
        };
    };

    const acceptReceivedFriendRequests = async (userId) => {
        const result = await friendRequestsStore.acceptReceivedFriendRequests(userId);
        console.log(result);
        if (result.code === 200) {
                alert('친구가 추가되었습니다.');
                await friendRequestsStore.getReceivedFriendRequests();
            };
    };

</script>

<style lang="scss" scoped>
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
</style>
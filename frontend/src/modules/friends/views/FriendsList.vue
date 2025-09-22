<template>
    <main>
        <FriendsListForm
            :friend_list="friensStore.friend_list"/>
    </main>
</template>

<script setup>
    import { onMounted } from 'vue';
    import FriendsListForm from '../components/FriendsListForm.vue';
    import { useFriendsStore } from '../stores/friendsStore';

    const friensStore = useFriendsStore();

    onMounted(async () => {
        await friensStore.getFriendsList();
    });
    
</script>

<style lang="scss" scoped>

</style>
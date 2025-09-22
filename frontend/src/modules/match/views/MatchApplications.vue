<template>
    <main>
        <match-table 
            :matchApplications="matchApplicationStore.matchApplications"
            @item-click="itemClick"
            @delete-click="deleteClick"/>
        <page-nation 
            :page-info="matchApplicationStore.pageInfo"
            @change-page="changePage"/>
    </main>
</template>

<script setup>
import { onMounted } from 'vue';
import MatchTable from '../tables/MatchTable.vue';
import PageNation from '../common/PageNation.vue';
import { onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router';
import { useMatchApplicationStore } from '../stores/matchApplicationStore';

    const matchApplicationStore = useMatchApplicationStore();
    const currentRoute = useRoute();
    const router = useRouter();

    const changePage = ({page, totalPages}) => {
        if(page >= 1 && page <= totalPages) {
            router.push({name: 'departments', query: {page}});
        } 
    };

    const itemClick = (no) => {
        router.push({name: 'departments/no', params: {no}});
    };

    const deleteClick = async (no) => {
        try {
            if(confirm('정말로 삭제하시겠습니까?')) {
                const result = await matchApplicationStore.deleteDepartment(no);

                if(result.code === 200) {
                    alert('정상적으로 삭제되었습니다.');

                    await matchApplicationStore.fetchDepartments(matchApplicationStore.pageInfo.currentPage, 10);
                }
            }
        } catch (error) {
            const {status, message} = error.response.data;

            if(status === 'DEPARTMENT_NOT_FOUND') {
                alert(message);

            } else if(status === 'REFRESH_TOKEN_INVALID') {
                router.push({name: 'login'});
            } else if(status === 'INTERNAL_SERVER_ERROR') {
                alert('에러가 발생했습니다.')
            }
        }
    };

    onMounted(async () => {
        try {
            matchApplicationStore.pageInfo.currentPage = parseInt(currentRoute.query.page) || 1;

            console.log(matchApplicationStore.matchApplications);
            
            await matchApplicationStore.fetchMatchApplications(matchApplicationStore.pageInfo.currentPage, 10);
        } catch (error) {
            const {status, message} = error.response.data;

            if(status === 'MATCH_APPLICATION_NOT_FOUND') {
                alert(message);

                router.push({name: 'MatchApplications'});
            } else if(status === 'REFRESH_TOKEN_INVALID') {
                router.push({name: 'login'});
            } else if(status === 'INTERNAL_SERVER_ERROR') {
                alert('에러가 발생했습니다.')
            }
        }
    });

    onBeforeRouteUpdate(async (to) => {
        try {
            matchApplicationStore.pageInfo.currentPage = parseInt(to.query.page) || 1;

            await matchApplicationStore.fetchMatchApplications(matchApplicationStore.pageInfo.currentPage, 10);
        } catch (error) {
            const {status, message} = error.response.data;

            if(status === 'DEPARTMENT_NOT_FOUND') {
                alert(message);

                router.push({name: 'MatchApplications'});
            } else if(status === 'REFRESH_TOKEN_INVALID') {
                router.push({name: 'login'});
            } else if(status === 'INTERNAL_SERVER_ERROR') {
                alert('에러가 발생했습니다.')
            }
        }
    });


</script>

<style lang="scss" scoped>

</style>
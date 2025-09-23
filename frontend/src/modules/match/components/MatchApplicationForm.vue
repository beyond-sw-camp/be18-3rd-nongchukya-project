<template>
    <form @submit.prevent="submitClick">
        <div class="mb-3">
            <label for="sport" class="form-label">종목</label>
            <select class="form-select" v-model="formData.sport">
                <option disabled value="">종목 선택</option>
                <option v-for="sport in sports" :key="sport.id" :value="sport.name">{{ sport.name }}</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="region" class="form-label">지역</label>
            <input type="text" class="form-control" id="region" v-model="formData.region">
        </div>
        <div class="mb-3">
            <label for="date" class="form-label">날짜</label>
            <input type="date" class="form-control" id="date" v-model="formData.matchDate">
        </div>
        <div class="mb-3">
            <label for="startTime" class="form-label">시작 시간</label>
            <input type="time" class="form-control" id="startTime" step="3600" v-model="formData.startTime">
        </div>
        <div class="mb-3">
            <label for="endTime" class="form-label">종료 시간</label>
            <input type="time" class="form-control" id="endTime" step="3600" v-model="formData.endTime">
        </div>
        <label for="genderOption" class="form-label">성별</label>
        <select class="form-select" aria-label="Default select example" v-model="formData.genderOption">
            <option value="A">상관없음</option>
            <option value="F">여자만</option>
            <option value="M">남자만</option>
        </select>
        <div>
            <button type="submit" class="btn btn-primary m-2">신청</button>
        </div>
    </form>
</template>

<script setup>
import { reactive, toRaw } from 'vue';

    const formData = reactive({
        sport: "",       
        region: "",
        matchDate: "",
        startTime: "",
        endTime: "",
        genderOption: "A"
    });

    // eslint-disable-next-line no-unused-vars
    const props = defineProps({
        initFormData: Object,
        formType: {
            type: String,
            required: true
        },
        sports: {
            type: Array,
            required: true
        }
    });

    const emit = defineEmits(['form-submit']);

    const submitClick = () => {
        emit('form-submit', toRaw(formData))
    };
</script>

<style lang="scss" scoped>
</style>
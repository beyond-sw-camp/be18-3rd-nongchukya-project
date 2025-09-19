<template>
    <form>
        <div claoss="mb-3">
            <label for="exampleInputPassword1" class="form-label">종목</label>
            <select class="form-select" aria-label="Default select example">
                <option selected>종목 선택</option>
                <option value="1">테니스</option>
                <option value="2">축구</option>
                <option value="3">배드민턴</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">지역</label>
            <input type="password" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">날짜</label>
            <input type="password" class="form-control" id="exampleInputPassword1">
        </div>
        <div class="mb-3">
            <label for="exampleInputPassword1" class="form-label">시간</label>
            <input type="password" class="form-control" id="exampleInputPassword1">
        </div>
        <label for="exampleInputPassword1" class="form-label">성별</label>
        <select class="form-select" aria-label="Default select example">
            <option selected>상관없음</option>
            <option value="1">여자만</option>
            <option value="2">남자만</option>
        </select>
        <div>
            <button type="submit" class="btn btn-primary m-2">신청</button>
        </div>
    </form>
</template>

<script setup>
</script>

<style lang="scss" scoped>
</style>
<template>
    <div style="width: 80%; margin-top: 3rem;">
        <span style="background-color: #3E3E3E; font-size: 1.5rem; padding: 0.5rem 2rem; border-radius: 50px; color: white;">댓글</span>
        
        <div v-if="comments && comments.data">
            <div v-for="(comment, index) in props.comments.data" :key="index">
                <div class="comment-container" style="margin-top: 3rem; height: auto; padding: 3rem; display: flex; flex-direction: column;">
                    <div class="comment-info" style="display: flex;">
                        <div class="profile-img">
                            <!-- 프로필 이미지는 따로 받아오기 -->
                            <img :src="getProfileImage(comment['user-id']) || defaultProfileImage" alt="profile" width="50" height="50">
                        </div>
                        <div class="user-info">
                            <div class="nickname-and-badge" style="display: flex; align-items: center;">
                                <!-- 닉네임은 따로 받아오기 -->
                                <div style="font-size: 1.5rem; font-weight: bold;">작성자: {{ getNickname(comment['user-id']) }}</div>
                                <!-- 뱃지는 따로 받아오기 -->
                                <div style="margin-left: 10px;">
                                    <img :src="getTierImage(comment['user-id'])" alt="뱃지 이미지" width="30" height="30">
                                </div>
                            </div>
                            <div>
                                {{ comment['comment-created-at'] }}
                            </div>
                        </div>
                    </div>
                    <!-- 댓글 내용 -->
                    <div class="comment-content" style="font-size: 1.8rem;">
                        {{ comment['comment-content'] }}
                    </div>
                    <div style="justify-content: flex-end;">
                        <button style="border-style: none; background: none; margin-top: 6px;">답글</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 에러 처리 -->
        <div v-else>
            <p>댓글 데이터를 가져오는 중 문제가 발생했습니다.</p>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, watchEffect , watch} from 'vue';

// props로 받은 댓글 데이터
const props = defineProps({
    comments: Object, 
    userProfiles: Object
});

// 사용자 프로필 데이터를 저장할 곳
const userProfiles = ref({});

// 기본 프로필 이미지
const defaultProfileImage = 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png';

    const getProfileImage = (userId) => {
    console.log(`Fetching profile image for userId: ${userId}`);
    console.log("Current userProfiles:", userProfiles.value);
    return userProfiles.value[userId]?.profileImage || defaultProfileImage;
    };

    const getNickname = (userId) => {
    console.log(`Fetching nickname for userId: ${userId}`);
    return userProfiles.value[userId]?.nickname || '알 수 없음';
    };

    const getTierImage = (userId) => {
    return userProfiles.value[userId]?.tierImage || '';
    };

watchEffect(() => {
    console.log("WatchEffect: comments", props.comments);  // props.comments로 접근해야 함
    console.log("WatchEffect: userProfiles", props.userProfiles);  // props.userProfiles로 접근
    console.log("Updated userProfiles:", userProfiles.value);
});

onMounted(() => {
    console.log("Mounted: comments", props.comments);
    console.log("Mounted: userProfiles", props.userProfiles);

});
</script>

<style scoped>
.comment-container{
    display: flex;
    background-color: #FFF5F5;
    width: 100%;
    border-radius: 1rem;
}
</style>
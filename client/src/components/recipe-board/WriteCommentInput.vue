<template>
  <div style="width: 80%; margin-top: 3rem">
    <span
      style="
        background-color: #3e3e3e;
        font-size: 1.5rem;
        padding: 0.5rem 2rem;
        border-radius: 50px;
        color: white;
      "
      >댓글</span
    >

    <div v-if="comments">
      <div v-for="(comment, index) in props.comments" :key="index">
        <div
          class="comment-container"
          style="
            margin-top: 3rem;
            height: auto;
            padding: 3rem;
            display: flex;
            flex-direction: column;
          "
        >
          <div class="comment-info" style="display: flex">
            <div class="profile-img">
              <!-- Check if userInfo exists and is valid -->
              <img :src="getProfileImage(index)" alt="profile" width="50" height="50" />
            </div>
            <div class="user-info">
              <div class="nickname-and-badge" style="display: flex; align-items: center">
                <div class="nickname">
                  {{ getNickname(index) }}
                </div>
                <div class="badge" v-if="isAdmin(index)" style="margin-left: 10px"></div>
                <div class="badge" v-else style="margin-left: 10px">
                  <img :src="getTierImage(index)" alt="뱃지 이미지" width="30" height="30" />
                </div>
              </div>
              <div>
                {{ comment['comment_created-at'] }}
              </div>
            </div>
          </div>
          <div class="comment-content" style="font-size: 1.8rem">
            {{ comment['comment_content'] }}
          </div>
          <div class="recomment-btn-wrapper">
            <button>답글</button>
          </div>
        </div>
      </div>
    </div>

    <div v-else>
      <p>댓글 데이터를 가져오는 중 문제가 발생했습니다.</p>
    </div>
  </div>
</template>

<script setup>
// props로 받은 댓글 데이터
const props = defineProps({
  comments: {
    type: Array,
    required: true
  },
  userInfo: {
    type: Array
  }
})

console.log(props.comments)
console.log(props.userInfo)

// 기본 프로필 이미지
const defaultImage = 'https://yoribogobucket.s3.ap-northeast-2.amazonaws.com/default_profile.png'

// Helper functions to safely access user information
function getProfileImage(index) {
  return props.userInfo && props.userInfo[index] && props.userInfo[index].profileImage
    ? props.userInfo[index].profileImage
    : defaultImage
}

function getNickname(index) {
  return props.userInfo && props.userInfo[index] && props.userInfo[index].nickname
    ? props.userInfo[index].nickname
    : 'Unknown User' // Fallback for nickname
}

function getTierImage(index) {
  return props.userInfo && props.userInfo[index] && props.userInfo[index].tierImage
    ? props.userInfo[index].tierImage
    : defaultImage // Fallback for tier image
}

function isAdmin(index) {
  return props.userInfo && props.userInfo[index] && props.userInfo[index].userRole === 'ADMIN'
}
</script>

<style scoped>
.comment-container {
  display: flex;
  background-color: #fff5f5;
  width: 100%;
  border-radius: 1rem;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nickname {
  font-size: 1.8rem;
  font-weight: 700;
}

.badge img {
  width: 3rem;
  height: 3rem;
  margin-top: 0.5rem;
}

.comment-info {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  align-items: center;
}

.comment-content {
  padding-left: 7.5rem;
}

.recomment-btn-wrapper {
  padding-left: 7.5rem;
  margin-top: 1rem;
}

.recomment-btn-wrapper button {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: transparent;
  border: none;
  color: #898989;
}
</style>

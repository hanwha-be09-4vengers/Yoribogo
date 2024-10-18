<template>
  <div class="qna-detail-view">
    <ProfileButton></ProfileButton>
    <HomeButton></HomeButton>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <div class="title">
            <h1 v-bind="inquiry">Q {{inquiry.inquiryTitle}}</h1>
            <img class="privateIcon" v-if="inquiry.inquiryVisibility == 'PUBLIC'" src="/src/assets/images/private_icon.png" >
            <div class="visibility1" v-if="inquiry.answerStatus == 'PENDING'">
              답변대기
            </div>
            <div class="visibility2" v-else>
              답변완료
            </div>
          </div>
          <select class="icons">
            <option value="1">수정</option>
            <option value="0">삭제</option>
          </select>
        </div>
        <div class="L2">
          <div class="inquiry_content" v-bind="inquiry">
            <p>{{ inquiry.inquiryContent }}</p>
          </div>
          <div class="answers" v-for="ans in answers" :key="ans.answerId">
            <div class="admin" v-if="ans.writerType == 'ADMIN'">
              <div class="ans">
                <img class="icon" src="@/assets/images/answer_icon.png">
                <div class="info1">
                  <p>관리자</p>
                  <p>{{ ans.answerCreatedAt }}</p>
                </div>
              </div>
              <div class="content1">
                <p>{{ ans.answerContent }}</p>
              </div>
            </div>
            <div class="enterprise" v-else>
              <div class="ans">
                <img class="icon" src="@/assets/images/inquiry_icon.png">
                <div class="info2">
                  <p>{{ ans.user.nickname }}</p>
                  <p>{{ ans.answerCreatedAt }}</p>
                </div>
              </div>
              <div class="content2">
                <p>{{ ans.answerContent }}</p>
              </div>
            </div>
          </div>
        </div>
        <!-- <div class="L4"> 
          <button class="back" type="button" @click="goBack"><</button>
          <div class="pageNums" v-for="num in nums.length" :key="num.code">
            <button type="button" @click="goPage(num)">{{ num }}</button>
          </div>
          <button class="next" type="button" @click="goNext">></button>
        </div> -->
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
  import HomeButton from '@/components/common/HomeButton.vue'
  import MainBoard from '@/components/common/MainBoard.vue'
  import ProfileButton from '@/components/common/ProfileButton.vue'

  import {ref, onMounted} from 'vue';
  import {useRoute, useRouter} from 'vue-router';
  import {getAnswers, addAnswer, deleteAnswer} from '@/api/qna.js';

  const route = useRoute();
  const router = useRouter();
  const inquiry = ref({});
  const answers = ref([]);
  
  onMounted(async () => {
    const data = await getAnswers(route.params.num);

    setInquiry(data.data);
    setAnswer(data.data.answer);
    
    console.log(inquiry.value);
    console.log(answers.value);
  });

  const setInquiry = (data) => {
    inquiry.value = data;
    inquiry.value.nickname = data.user.nickname; 
  };

  const setAnswer = (data) => {
    for (let i=0; i<data.length; i++) {
      answers.value[i] = data[i];
      answers.value[i].nickname = data[i].user.nickname;
      answers.value[i].answerCreatedAt = data[i].answerCreatedAt.split('T').join(' ');
    }
  };

  

</script>

<style scoped>
.qna-detail-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.profile-btn {
  position: absolute;
  top: 7rem;
  right: 12rem;
}

.home-btn {
  position: absolute;
  top: 7rem;
  right: 20rem;
}

@media screen and (max-width: 960px) {
  .wiki-list {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 480px) {
  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
.wrapper {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 60px 60px 20px 60px;
}
.L1 {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding: 0 2% 2% 2%;
}
.L1 div:nth-child(1) {
  width: 80%;
}
.L1 div:nth-child(2) {
  width: 70px;
}
.title {
  display: flex;
  column-gap: 10px;
  align-items: flex-end;
}
h1 {
  font-size: 30px;
}
.privateIcon {
  height: 15px;
  align-self: center;
}
.visibility1, .visibility2 {
  height: 20px;
  width: 55px;
  margin-left: 20px;
  align-self: flex-end;
}
.visibility1 {
  text-align: center;
  font-size: 6px;
  border-radius: 16px;
  background: #EFEFEF;
  color: #B9B9B9;
}
.visibility2 {
  text-align: center;
  font-size: 6px;
  border-radius: 16px;
  background: #333;
  color: white;
}
.icons {
  
}
.L2 {
  display: flex;
  flex-direction: column;
  border-top: 0.5px solid #bfc1c466;
}
.inquiry_content {
  padding: 5% 0 5% 10px;
  border-bottom: 0.5px solid #bfc1c466;
}
.answers {
  display: flex;
  flex-direction: column;
}
.admin, .enterprise {
  display: grid;
  grid-template-rows: 1fr 2fr;
  padding: 10px;
  row-gap: 10px;
}
.admin {

}
.enterprise {

}
.ans {
  display: flex;
  width: 100%;
  align-items: center;
}
.content1, .content2 {
  padding-left: 40px;
}
.icon {
  width: 30px;
  height: 30px;
  margin: 0 10px 0 0;
}
.L2 p {
  cursor: pointer;
}

.L2 p:first-child {
  font-weight: bold;
  color: black;
}


.L3 {
  display: grid;
  grid-template-rows: repeat(10, 30px);
  padding: 0 5% 0 5%;
}

.list:first-child {
  border-top: 0.5px solid #bfc1c466;
}
.list {
  display: grid;
  grid-template-columns: 1fr 6fr 2fr 2fr 2fr;
  align-content: center;
  justify-items: center;
  border-bottom: 0.5px solid #bfc1c466;
}
.L3 .list a, .L3 .list p {
  font-weight: 500;
  color: black;
}




.L4 {
  display: flex;
  justify-content: center;
  align-items: center;
  column-gap: 8px;
  font-family: "Noto Sans KR";
}

.L4 button {
  background: none;
  border: none;
}

</style>

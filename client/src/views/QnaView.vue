<template>
  <div class="qna-view">
    <header>
      <NotificationButton class="notification-btn"></NotificationButton>
      <ProfileButton class="profile-btn"></ProfileButton>
      <HomeButton class="home-btn"></HomeButton>
    </header>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <p @click="goHome">문의 게시판</p>
          <div class="search">
            <img src="/src/assets/images/search_icon.png">
            <input class="inputValue" type="search" @keyup.enter="searchText" v-model="sTxt" placeholder="문의사항 검색">
          </div>
        </div>
        <div class="L2">
          <div class="p-container">
            <p @click="changeList($event.target.innerText)">전체</p>
            <p @click="changeList($event.target.innerText)">답변대기</p>
            <p @click="changeList($event.target.innerText)">답변완료</p>
          </div>
          <div class="create-btn">
            <button type="button" @click="goCreate">QnA 작성</button>
          </div>
          </div>
        <div class="L3">
          <div class="list" v-for="inquiry in pageInquiries" :key="inquiry.inquiryId">
            <a href="" @click.prevent="toInquiry(inquiry)">{{ inquiry.inquiryId }}</a>
            <div class="title">
              <a href="" @click.prevent="toInquiry(inquiry)">{{ inquiry.inquiryTitle }}</a>
              <img class="privateIcon" v-if="inquiry.inquiryVisibility == 'PRIVATE'" src="/src/assets/images/private_icon.png" >
            </div>
            <p>{{ inquiry.user.nickname }}</p>
            <p>{{ inquiry.inquiryCreatedAt }}</p>
            <div class="visibility1" v-if="inquiry.answerStatus == 'PENDING'">
              답변대기
            </div>
            <div class="visibility2" v-else>
              답변완료
            </div>
          </div>
        </div>
        <div class="L4">
          <button class="back" type="button" @click="goBack"></button>
          <div class="pageNums" v-for="num in nums.length" :key="num.code">
            <button type="button" @click="goPage(num)">{{ num }}</button>
          </div>
          <button class="next" type="button" @click="goNext">></button>
        </div>
      </div>
    </MainBoard>
  </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue';
import MainBoard from '@/components/common/MainBoard.vue';
import ProfileButton from '@/components/common/ProfileButton.vue';
import NotificationButton from '@/components/common/NotificationButton.vue';

  import {ref, onMounted} from 'vue';
  import {useRoute, useRouter} from 'vue-router';
  import { useTokenStore } from '@/stores/tokenStore';
  import {getInquiries, getUserInfo} from '@/api/qna.js';

  const route = useRoute();
  const router = useRouter();
  const tokenStroe = useTokenStore();
  const inquiry = ref([]);
  const list = ref([]);
  const filtd = ref([]);
  const user = ref({});
  const sTxt = ref('');

  onMounted(async () => {
    const response = await getInquiries();

    user.value = await getUserInfo(tokenStroe.token.userId);
    user.value = user.value.data;
    
    setinquiry(response);
    updateList(list.value);
    goPage(1);
  });


  const setNums = (lst) => {
    if (Math.floor(lst.length/10) < 5) next.value = Math.ceil(lst.length/10);
    nums.value = Array.from({ length: next.value - index.value }, (_, i) => i + index.value);
  };

  const updateList = (lst) => {
    setNums(lst);
    list.value = lst;
    filtd.value = lst;
  }

  const setinquiry = (data) => {
    for (let i=0; i<data.data.length; i++)
      data.data[i].inquiryCreatedAt = data.data[i].inquiryCreatedAt.split('T')[0];
    if (data.data.length > 5) next.value = 5;
    else next.value = data.data.length;
    inquiry.value =data.data.reverse();
    list.value = inquiry.value;
    filtd.value = inquiry.value;
  }

  

  const nums = ref([]);
  const index = ref(0);
  const next = ref(0);

  const goBack = () => {
    if (index.value > 0) {
      if (next.value - index.value < 5) next.value -= (next.value-index.value);
      else next.value -= 5;
      index.value -= 5;
    }
    updateNums();
  };

  const goNext = () => {
    const totalNums = ref(Math.ceil(list.value.length/10));
    if (next.value < totalNums.value) {
      if (totalNums.value - next.value < 5) next.value += totalNums.value-next.value+1;
      else next.value += 5;
      index.value += 5;
    }    
    updateNums();
  };

  const updateNums = () => {
    nums.value = Array.from({ length: next.value - index.value }, (_, i) => i + index.value);
  }

  const pageInquiries = ref([]);

  const goPage = (num) => {
    pageInquiries.value = list.value.slice((num-1)*10, (num-1)*10+10);
  };

  const changeList = (elemt) => {
    const tags = document.querySelectorAll('.L2 p');
    for(let i=0; i < tags.length; i++) {
      if (tags[i].innerText === elemt) {
        tags[i].style.fontWeight = 'bold';
        tags[i].style.color = 'black';
      } else {
        tags[i].style.fontWeight = 'normal',
        tags[i].style.color = '#AEB3BB'
      }
    }

    switch (elemt) {
      case '전체':
        list.value = filtd.value;
        break;
      case '답변완료':
        list.value = filtd.value.filter(ans => ans.answerStatus === 'ANSWERED');
        break;
      case '답변대기':
        list.value = filtd.value.filter(ans => ans.answerStatus === 'PENDING');
        break;
      default:
        break;
    }
    setNums(list.value);
    goPage(1);
  };
  

  const searchText = () => {
    filtd.value = inquiry.value.filter(ans => ans.inquiryTitle.includes(sTxt.value));
    updateList(filtd.value);
    changeList('전체');
  };

  const goHome = () => {
    router.go(0);
  };

  /* 문의 상세페이지로 이동 */
  const toInquiry = (inquiry) => {
    if (inquiry.inquiryVisibility === 'PRIVATE' && 
          inquiry.user.userId !== user.value.user_id &&
          user.value.user_role !== 'ADMIN') {
        alert('비밀글은 작성자와 관리자만 볼 수 있습니다.');
    } else {
      router.push(`/qna/${inquiry.inquiryId}`);
    }
  };

  const goCreate = () => {
    router.push('/qna/new');
  };

</script>

<style scoped>
.qna-view {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  min-height: 100vh;
  background-color: var(--yellow-color);
}

.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem;
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

.wrapper {
  display: grid;
  grid-template-rows: 12% 8% 75% 5%;
  width: 100%;
  height: 100vh;
  padding: 60px 60px 50px 60px;
  font-size: 2.2rem;
}
.L1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5% 0 5%;
}
.L1 p {
  cursor: pointer;
  font-size: 4rem;
  font-weight: bold;
}
.search {
  display: grid;
  grid-template-columns: 15% 80%;
  align-items: center;
  margin: 10px 0 0 0;
  width: 230px;
  height: 30px;
  border-radius: 3px;
  border: 1px solid #AEB3BB;
  text-align: center;
  justify-content: center;
}

.search img {
  width: 18px;
  height: 18px;
}

input[type=search] {
  border: none;
  outline: none;
  font-size: 2rem;
}
input[type=search]::placeholder {
  color: #9a9fa7;
  font-weight: 100;
  font-size: 2.7rem;
}
.L2 {
  display: flex;
  justify-content: space-between;
  padding-right: 5%;
  align-items: center;
}
.p-container {
  display: flex;
  column-gap: 20px;
  padding: 0 5% 0 5%;
  justify-items: center;
  color: #AEB3BB;
}
.L2 div p {
  cursor: pointer;
  font-size: 2.5rem;
}

.L2 div p:first-child {
  font-weight: bold;
  color: black;
}
.create-btn {
  display: flex;
  width: 1fr;
  margin-right: 10px;
  text-align: center;
  justify-content: flex-end;
  margin: 0;
}
.create-btn button {
  height: 30px;
  width: 80px;
  font-size: 6px;
  border: none;
  color: white;
  background-color: black;
  cursor: pointer;
  border-radius: 3px;
  font-size: 2rem;
}

.L3 {
  display: grid;
  grid-template-rows: repeat(10, 10%);
  padding: 0 5% 0 5%;
}

.list:first-child {
  border-top: 0.5px solid #bfc1c466;
}
.list {
  display: grid;
  grid-template-columns: 0.5fr 5fr 2fr 2fr 1fr;
  align-content: center;
  align-items: center;
  justify-items: center;
  border-bottom: 0.5px solid #bfc1c466;
}
.L3 .list a, .L3 .list p {
  font-weight: 500;
  color: black;
}

.title {
  display: flex;
  justify-self: start;
  align-items: center;
  padding-left: 10px;
  gap: 5px;
}
.privateIcon {
  width: 7px;
  height: 10px;
}
.visibility1, .visibility2 {
  height: 24px;
  width: 55px;
  margin-left: 10px;
  margin-right: 10px;
  align-content: center;
  text-align: center;
}
.visibility1 {
  font-size: 6px;
  border-radius: 16px;
  background: #EFEFEF;
  color: #B9B9B9;
}
.visibility2 {
  font-size: 6px;
  border-radius: 16px;
  background: #333;
  color: white;
}

.L4 {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  column-gap: 8px;
  font-family: "Noto Sans KR";
  height: 100%;
}

.L4 button, .L4 div, .L4 div button {
  display: flex;
  flex-direction: column;
  background: none;
  border: none;
  color: black;
  text-align: center;
  font-size: 3rem;
  cursor: pointer;
  height: 100%;
  justify-content: flex-end;
  align-items: flex-end;
  text-align: center;
}


.notification-btn {
  position: absolute;
  top: 7rem;
  left: 12rem
}
@media screen and (max-width: 480px) {
  .notification-btn {
    left: 10rem;
  }

  .profile-btn {
    right: 10rem;
  }

  .home-btn {
    right: 18rem;
  }
}

@media screen and (max-width: 425px) {
  .notification-btn {
    left: 9rem;
  }

  .profile-btn {
    right: 9rem;
  }

  .home-btn {
    right: 17rem;
  }
}

@media screen and (max-width: 375px) {
  .notification-btn {
    left: 8rem;
  }

  .profile-btn {
    right: 8rem;
  }

  .home-btn {
    right: 16rem;
  }
}
</style>

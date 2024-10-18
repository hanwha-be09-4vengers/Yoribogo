<template>
  <div class="qna-view">
    <ProfileButton></ProfileButton>
    <HomeButton></HomeButton>
    <MainBoard :cur="'qna'">
      <div class="wrapper">
        <div class="L1">
          <h1 @click="goHome">문의 게시판</h1>
          <div class="search">
            <img src="/src/assets/images/search_icon.png">
            <input class="inputValue" type="search" @keyup.enter="searchText($event)" placeholder="문의사항 검색">
          </div>
        </div>
        <div class="L2">
          <p @click="changeList($event.target.innerText)">전체</p>
          <p @click="changeList($event.target.innerText)">답변대기</p>
          <p @click="changeList($event.target.innerText)">답변완료</p>
        </div>
        <div class="L3">
          <div class="list" v-for="inquiry in pageInquiries" :key="inquiry.inquiryId">
            <a href="" @click.prevent="toInquiry(inquiry.inquiryId)">{{ inquiry.inquiryId }}</a>
            <div class="title">
              <a href="" @click.prevent="toInquiry(inquiry.inquiryId)">{{ inquiry.inquiryTitle }}</a>
              <img class="privateIcon" v-if="inquiry.inquiryVisibility == 'PUBLIC'" src="/src/assets/images/private_icon.png" >
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
          <button class="back" type="button" @click="goBack"><</button>
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
  import HomeButton from '@/components/common/HomeButton.vue'
  import MainBoard from '@/components/common/MainBoard.vue'
  import ProfileButton from '@/components/common/ProfileButton.vue'

  import {ref, onMounted} from 'vue';
  import {useRoute, useRouter} from 'vue-router';
  import axios from 'axios';
  import {getInquiries, addInquiry, modifyInquiry} from '@/api/qna.js';

  const route = useRoute();
  const router = useRouter();
  const inquiry = ref([]);
  const list = ref([]);
  const filtd = ref([]);

  onMounted(async () => {
    const response = await getInquiries();
    
    setinquiry(response);
    updateList(list.value);
    console.log(list.value);
    goPage(1);
  });

  const updateList = (lst) => {
    if (Math.floor(lst.length/10) < 5) next.value = Math.ceil(lst.length/10);
    nums.value = Array.from({ length: next.value - index.value }, (_, i) => i + index.value);
    list.value = lst;
    filtd.value = lst;
  }

  const setinquiry = (data) => {
    for (let i=0; i<data.data.length; i++)
      data.data[i].inquiryCreatedAt = data.data[i].inquiryCreatedAt.split('T')[0];
    inquiry.value =data.data.reverse();
    list.value = inquiry.value;
    filtd.value = inquiry.value;
  }

  

  const nums = ref([]);
  const index = ref(0);
  const next = ref(5);

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
      default:
        break;
    }
    goPage(1);
  };
  

  const searchText = (event) => {
    filtd.value = inquiry.value.filter(ans => ans.inquiryTitle.includes(event.target.value));
    updateList(filtd.value);
    changeList('전체');
  };

  const goHome = () => {
    const tag = document.querySelector(".inputValue");
    tag.value = '';
    updateList(inquiry.value);
    changeList('전체');
  };

  /* 문의 상세페이지로 이동 */
  const toInquiry = (num) => {
    router.push(`/qna/${num}`);
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
  display: grid;
  grid-template-rows: 3fr 1fr 10fr 1fr;
  width: 100%;
  height: 100%;
  padding: 60px 60px 50px 60px;
}
.L1 {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 5% 0 5%;

}
h1 {
  cursor: pointer;
}
.search {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  column-gap: 10px;
  margin: 10px 0 0 0;
  width: 230px;
  height: 25px;
  border-radius: 3px;
  border: 1px solid #AEB3BB;

}

.search img {
  width: 18px;
  height: 18px;
}

input[type=search] {
  border: none;
  outline: none;
}
input[type=search]::placeholder {
  color: #AEB3BB;
  font-weight: 100;
  font-size: smaller;
}

.L2 {
  display: flex;
  column-gap: 20px;
  padding: 0 5% 0 5%;
  justify-items: center;
  font-size: 15px;
  color: #AEB3BB;
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
.visibility1 {
  width: 70%;
  text-align: center;
  font-size: 6px;
  border-radius: 16px;
  background: #EFEFEF;
  color: #B9B9B9;
}
.visibility2 {
  width: 70%;
  text-align: center;
  font-size: 6px;
  border-radius: 16px;
  background: #333;
  color: white;
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

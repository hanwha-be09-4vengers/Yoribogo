<template>
  <div class="result-board">
    <img :src="props.img" alt="Image" />
    <span>{{ props.text }}</span>
  </div>
</template>

<script setup>
const props = defineProps({
  img: {
    type: String,
    required: true
  },
  text: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.result-board {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 60rem;
  height: 68rem;
  gap: 4rem;
  background-color: var(--white-color);
  box-shadow: 0.5rem 0.5rem 0.3rem 0rem rgba(60, 60, 60, 0.5);
  border-radius: 1.8rem;
  cursor: pointer;
  transition: 0.3s ease-out;
}

.result-board:hover {
  transform: translateY(-0.3rem);
  box-shadow: 0.5rem 0.5rem 0.3rem 0.2rem rgba(60, 60, 60, 0.5);
}

.result-board img {
  height: 22.6rem;
}

.result-board span {
  font-size: 4rem;
}

/* 노트북 */
@media screen and (max-width: 1683px) {
  .result-board {
    width: 47rem;
    height: 53rem;
  }
}

/* 모바일 */
@media screen and (max-width: 767px) {
  .result-board img {
    height: 20rem;
  }
}

@media screen and (max-width: 560px) {
  .result-board {
    width: 42rem;
    height: 47rem;
  }

  .result-board img {
    height: 18rem;
  }

  .result-board span {
    font-size: 3.3rem;
  }
}

@media screen and (max-width: 374px) {
  .result-board {
    width: 40rem;
    height: 45rem;
  }

  .result-board img {
    height: 16rem;
  }

  .result-board span {
    font-size: 3.2rem;
  }
}
@media screen and (max-width: 320px) {
  .result-board {
    width: 36rem;
    height: 40rem;
  }

  .result-board img {
    height: 14rem;
  }

  .result-board span {
    font-size: 3rem;
  }
}
</style>

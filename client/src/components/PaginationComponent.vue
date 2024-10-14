<template>
  <div class="pagination">
    <button
      class="prev"
      @click="prevPage"
      :disabled="props.data.number + 1 === 1 || typeof props.data.number + 1 === 'undefined'"
    >
      <i class="fa-solid fa-caret-left"></i>
    </button>
    <span>{{ props.data.number + 1 }} / {{ props.data.totalPages }}</span>
    <button
      class="next"
      @click="nextPage"
      :disabled="
        props.data.number + 1 === props.data.totalPages ||
        typeof props.data.number + 1 === 'undefined'
      "
    >
      <i class="fa-solid fa-caret-right"></i>
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const props = defineProps({
  data: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['changePage'])

// 페이지 이동 함수
const nextPage = () => {
  if (props.data.number + 1 < props.data.totalPages) {
    emit('changePage', props.data.number + 1 + 1) // 페이지를 변경하라는 이벤트를 emit
  }
}

const prevPage = () => {
  if (props.data.number + 1 > 1) {
    emit('changePage', props.data.number + 1 - 1) // 페이지를 변경하라는 이벤트를 emit
  }
}
</script>

<style>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  flex-wrap: nowrap;
  width: 100%;
  margin-bottom: 5rem;
  color: var(--red-color);
}

.prev,
.next {
  border: none;
  background-color: transparent;
  color: var(--red-color);
  cursor: pointer;
}

.pagination i {
  font-size: 3.5rem;
}

.pagination span {
  font-size: 3rem;
  margin-left: 1rem;
  margin-right: 1rem;
}
</style>

<template>
  <div class="attribute-input-container">
    <div class="attribute-name-wrapper">
      <span>{{ props.name }}</span>
    </div>
    <div class="attribute-input-wrapper">
      <input type="text" :placeholder="props.placeholder" />
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  name: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    required: true
  }
})
</script>

<style scoped>
.attribute-input-container {
  display: flex;
  flex-direction: column;
  width: 90%;
  height: fit-content;
}

.attribute-name-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 9rem;
  height: 3rem;
  align-items: center;
  background-color: var(--navy-color);
  color: var(--white-color);
  border-radius: 0.8rem 0.8rem 0rem 0;
  font-size: 1.5rem;
  font-weight: 500;
}

.attribute-input-wrapper {
  display: flex;
  width: 100%;
  min-height: 9rem;
  background-color: var(--light-gray-color);
  border-radius: 0 0.8rem 0.8rem 0.8rem;
}

.attribute-input-wrapper input {
  width: 100%;
  background-color: transparent;
  border: none;
  outline: none;
  padding-left: 3rem;
  font-size: 1.5rem;
}
</style>

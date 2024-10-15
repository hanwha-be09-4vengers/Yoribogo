<template>
  <button class="profile-btn">
    <img
      src="https://cdxarchivephoto.s3.ap-northeast-2.amazonaws.com/1720072494119_21792adc-7e3e-4d3e-8e69-3b55fa58615c_IMG_2316.jpeg"
      alt="Logo"
    />
  </button>
</template>

<script setup></script>

<style scoped>
.profile-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 6.3rem;
  height: 6.3rem;
  border: none;
  border-radius: 50%;
  overflow: hidden;
  background-color: var(--light-yellow-color);
  box-shadow: 0rem 0.35rem 0.35rem 0rem rgba(60, 60, 60, 0.5);
  cursor: pointer;
  z-index: 9999;
}

.profile-btn img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}

.profile-btn:hover {
  transition: 0.3s ease;
  box-shadow: 0rem 0.4rem 0.4rem 0.1rem rgba(60, 60, 60, 0.5);
}
</style>

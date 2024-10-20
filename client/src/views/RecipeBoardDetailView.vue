<template>
    <div class="recipe-board-detail-view">
        <header>
            <NotificationButton></NotificationButton>
            <ProfileButton></ProfileButton>
            <HomeButton></HomeButton>
        </header>
        <MainBoard :cur="recipe-board-detail">
            <BackButton></BackButton>
            <div class="menu-name-wrapper">
                <span class="menu-name">{{  }}</span>
            </div>
            <div class="menu-img-wrapper">
                <img v-show="isImageLoading" :src="defaultImage" alt="MenuImage" />
                <img
                    v-show="!isImageLoading"
                    :src="menuImageSrc"
                    alt="MenuImage"
                    @load="handleImageLoad"
                    @error="handleImageError"
                />
            </div>
            <div class="menu-ingredient-container">
                <div class="info">재료</div>
                <span class="menu-ingredient">{{  }}</span>
            </div>
            <div class="menu-manual-container">
                <div class="info">조리 순서</div>
                <RecipeBoardManual
                    v-for="manual in manualList"
                    :key="manual.manual_id"
                    :manualContent="manual.manual_content"
                    :manualImage="manual.manual_image"
                ></RecipeBoardManual>
            </div>
        </MainBoard>
        <aside>
            <GoTopButton></GoTopButton>
        </aside>
    </div>
</template>

<script setup>
import HomeButton from '@/components/common/HomeButton.vue';
import MainBoard from '@/components/common/MainBoard.vue';
import BackButton from '@/components/common/BackButton.vue';
import NotificationButton from '@/components/common/NotificationButton.vue';
import ProfileButton from '@/components/common/ProfileButton.vue';
import GoTopButton from '@/components/common/GoTopButton.vue';


</script>

<style scoped>
.recipe-board-detail-view {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    min-height: 100vh;
    background-color: var(--yellow-color);
}

.go-top-btn {
    position: fixed;
    top: 84%;
    right: 8rem;
}

.menu-name-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 18rem;
}

.menu-name {
    font-size: 4rem;
    font-weight: 500;
    color: var(--black-color);
}

.menu-img-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 72rem;
    height: 54rem;
    overflow: hidden;
    margin-top: 4rem;
    border-radius: 1.4rem;
}

.menu-img-wrapper img {
    height: 100%;
    width: 100%;
    object-fit: cover;
}

.menu-ingredient-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 4rem;
    margin-top: 6rem;
}

.menu-ingredient {
    text-align: center;
    width: 68%;
    font-size: 2.4rem;
    font-weight: 500;
    line-height: 4rem;
    word-wrap: break-word;
}

.menu-manual-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    gap: 4rem;
    margin-top: 4rem;
    margin-bottom: 12rem;
}

.info {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 1rem 2rem;
    font-size: 2.4rem;
    font-weight: 350;
    color: var(--white-color);
    background-color: var(--black-color);
    border-radius: 3rem;
}

.back-btn {
    position: absolute;
    top: 14rem;
    left: 12rem;
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

@media screen and (max-width:1024px) {
    .go-top-btn {
        right: 5rem;
    }
}
</style>
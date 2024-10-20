<template>
    <div class="recipe-list-view-container" >
        <SearchBar></SearchBar>
        <RecipeViewSwitchButton></RecipeViewSwitchButton>
        <router-link to="/recipe-board/write">
            <WriteRecipeButton></WriteRecipeButton>
        </router-link>
    </div>
</template>

<script setup>
import SearchBar from '@/components/common/SearchBar.vue';
import RecipeViewSwitchButton from '@/components/recipe-board/RecipeViewSwitchButton.vue';
import WriteRecipeButton from '@/components/recipe-board/WriteRecipeButton.vue';

</script>

<style scoped>

.recipe-list-view-container{
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.write-recipe-container{
    position: absolute;
    bottom: 7rem;
    right: 8rem;
}

.search-bar{
    margin-top: 8rem;

}


</style>
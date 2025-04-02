<template>
    <div class="app-container">
        <Navbar />
        <RouterView />
        <Toaster />
    </div>
</template>

<style scoped>
.app-container {
    display: flex;
    flex-direction: column;
    min-height: 100dvh;
    height: 100dvh;
}
</style>
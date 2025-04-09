<template>
  <div class="app-container">
    <Navbar />
    <div class="center-view">
      <RouterView />
    </div>
    <Toaster />
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/Navbar.vue'
import { RouterView } from 'vue-router'
import { Toaster, toast as showToast, type ExternalToast } from 'vue-sonner'
import { useAuthStore } from '@/stores/auth';
import { useCookies } from "@vueuse/integrations/useCookies";
import { onMounted } from 'vue';
import Cookies from 'universal-cookie';

const cookie = useCookies(["toast"]);

onMounted(() => {
  const cookieStr = new Cookies(document.cookie).get("toast");
  const toastData = cookieStr ? new URLSearchParams(cookieStr) : undefined;
  if (toastData) {
    const type = toastData.get("type") as "success" | "error" | "info" | "warning";
    const message = toastData.get("message") as string;
    const description = toastData.get("description") as string | undefined;
    const duration = toastData.get("duration") as string | undefined;

    const options: ExternalToast = {};
    if (description) options.description = description;
    if (!Number.isNaN(Number(duration))) options.duration = Number(duration);

    showToast[type](message, options);

    document.cookie = "toast=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    cookie.remove("toast");
  } 

})


const authStore = useAuthStore();
authStore.initialize();

</script>

<style scoped>
.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100dvh;
}

.center-view {
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: center;  
}
</style>

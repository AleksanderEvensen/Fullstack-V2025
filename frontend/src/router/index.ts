import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/home/HomeView.vue'
import AuthLayout from '@/views/layouts/AuthLayout.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      component: AuthLayout,
      children: [
        {
          path: '/',
          name: 'home',
          component: HomeView,
        },
        {
          path: '/search',
          name: 'search',
          component: () => import('../views/search/SearchView.vue'),
        },
        {
          path: '/about',
          name: 'about',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/AboutView.vue'),
        },
        {
          path: '/components',
          name: 'components',
          component: () => import('../views/ComponentsView.vue'),
        },

        {
          path: '/marketplace/messages',
          name: 'messages',
          meta: {
            requiresAuth: true,
          },
          component: () => import('../views/MessagesView.vue'),
        },
        {
          path: '/marketplace/product/:id',
          name: 'product',
          component: () => import('../views/ProductView.vue'),
        },
        {
          path: '/marketplace/product/create',
          name: 'product-create',
          meta: {
            requiresAuth: true,
          },
          component: () => import('../views/create-listing/CreateListingView.vue'),
        },
        {
          path: '/register',
          name: 'register',
          component: () => import('../views/auth/register/RegisterView.vue'),
        },
        {
          path: '/login',
          name: 'login',
          component: () => import('../views/auth/login/LoginView.vue'),
        },
        {
          path: '/profile',
          name: 'profile',
          meta: {
            requiresAuth: true,
          },
          component: () => import('../views/profile/ProfileView.vue'),
        },
        {
          path: '/profile/settings',
          name: 'profile-settings',
          meta: {
            requiresAuth: true,
          },
          component: () => import('../views/profile/settings/SettingsView.vue'),
        },
        {
          path: '/chat',
          name: 'chat',
          component: () => import('../views/chat/ChatView.vue'),
          meta: { requiresAuth: true },
        },
      ],
    },
  ],
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (authStore.token && !authStore.user) {
    authStore.initialize()
  }

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router

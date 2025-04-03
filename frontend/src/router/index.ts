import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/home/HomeView.vue'
import AuthLayout from '@/views/layouts/AuthLayout.vue'

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
          component: () => import('../views/CreateListingView.vue'),
        },
        {
          path: '/auth/register',
          name: 'register',
          component: () => import('../views/RegisterView.vue'),
        },
        {
          path: '/auth/login',
          name: 'login',
          component: () => import('../views/LoginView.vue'),
        },
        {
          path: '/profile',
          name: 'profile',
          component: () => import('../views/profile/ProfileView.vue'),
        },
      ],
    },
  ],
})

export default router

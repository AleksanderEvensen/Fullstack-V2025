import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'layout',
      component: () => import('../views/layouts/AuthLayout.vue'),
      children: [
        {
          path: '/',
          name: 'home',
          component: HomeView,
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
      ],
    },
  ],
})

export default router

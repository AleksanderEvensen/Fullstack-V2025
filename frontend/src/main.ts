import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { VueQueryPlugin } from '@tanstack/vue-query'
import { i18n } from './i18n'
import 'mapbox-gl/dist/mapbox-gl.css'

const app = createApp(App)

app.use(VueQueryPlugin)
app.use(createPinia())
app.use(router)
app.use(i18n)

app.mount('#app')

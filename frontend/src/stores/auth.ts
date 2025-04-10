import type { components, paths } from '@/lib/api/schema'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import createFetchClient from 'openapi-fetch'
import ky from 'ky'
import Cookies from 'universal-cookie'

type LoginRequest = paths['/api/auth/login']['post']['requestBody']['content']['application/json']
type RegisterRequest =
  paths['/api/auth/register']['post']['requestBody']['content']['application/json']
type User = components['schemas']['FullUserDto']

export const TOKEN = 'amazoom-token'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem(TOKEN))
  const user = ref<User | null>(null)
  const isAuthenticated = computed(() => !!token.value)

  const api = createFetchClient<paths>({
    fetch: (...args) =>
      ky.extend({
        headers: {
          'Content-Type': 'application/json',
        },
        hooks: {
          beforeRequest: [
            (req) => {
              if (token.value) {
                req.headers.set('Authorization', `Bearer ${token.value}`)
              }
            },
          ],
          afterResponse: [
            async (req, _, res) => {
              if (res.status === 401 && token.value) {
                logout()
              }

              return res
            },
          ],
        },
      })(...args),
  })

  async function login(credentials: LoginRequest) {
    try {
      const response = await api.POST('/api/auth/login', {
        body: credentials,
      })

      if (!response.data) {
        return { success: false, error: 'Invalid response' }
      }

      token.value = response.data.accessToken
      localStorage.setItem(TOKEN, token.value)

      await fetchUser()

      return { success: true, data: response.data }
    } catch (error) {
      console.error('Login error:', error)
      return { success: false, error }
    }
  }

  async function register(credentials: RegisterRequest) {
    try {
      const response = await api.POST('/api/auth/register', {
        body: credentials,
      })

      if (!response.data) {
        return { success: false, error: 'Invalid response' }
      }

      token.value = response.data.accessToken
      localStorage.setItem(TOKEN, token.value)

      await fetchUser()

      return { success: true, data: response.data }
    } catch (error) {
      console.error('Register error:', error)
      return { success: false, error }
    }
  }

  function logout() {
    token.value = null
    user.value = null
    localStorage.removeItem(TOKEN)
  }

  async function fetchUser() {
    try {
      const response = await api.GET('/api/auth/me')

      if (!response.data) {
        return { success: false, error: 'Invalid response' }
      }

      user.value = response.data
      return { success: true, data: response.data }
    } catch (error) {
      console.error('Fetch user error:', error)
      logout()
      throw error
    }
  }

  function initialize() {
    const cookies = new Cookies(document.cookie)
    const tokenFromCookie = cookies.get('am_session') as string | undefined

    if (tokenFromCookie) {
      token.value = tokenFromCookie
      localStorage.setItem(TOKEN, token.value)
    }

    if (token.value) {
      fetchUser().catch(() => logout())
    }

    document.cookie = 'am_session=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
  }

  return {
    token,
    user,
    isAuthenticated,
    login,
    register,
    logout,
    fetchUser,
    initialize,
  }
})

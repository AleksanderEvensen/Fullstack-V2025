import createFetchClient from 'openapi-fetch'
import type { paths } from '@/lib/api/schema'
import ky from 'ky'
import { useAuthStore } from '@/stores/auth'

// // Flag to prevent multiple simultaneous refresh attempts
// const isRefreshing = false
// // Queue of requests to retry after token refresh
// const failedRequestsQueue: Array<() => void> = []

function createApiClient() {
  const kyClient = ky.extend({
    headers: {
      'Content-Type': 'application/json',
    },
    hooks: {
      beforeRequest: [
        (req) => {
          const authStore = useAuthStore()

          if (authStore.token) {
            req.headers.set('Authorization', `Bearer ${authStore.token}`)
          }
        },
      ],
      afterResponse: [
        async (req, _, res) => {
          const authStore = useAuthStore()

          if (res.status === 401 && authStore.token) {
            authStore.logout()
          }

          return res
        },
      ],
    },
  })

  return createFetchClient<paths>({
    fetch: (...args) => kyClient(...args),
  })
}

export const fetchClient = createApiClient()

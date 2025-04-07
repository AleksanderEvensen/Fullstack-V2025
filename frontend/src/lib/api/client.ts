import createFetchClient from 'openapi-fetch'
import type { paths } from '@/lib/api/schema'
import ky from 'ky'

const kyClient = ky.extend({
  headers: {
    'Content-Type': 'application/json',
  },
})
export const fetchClient = createFetchClient<paths>({
  fetch: (...args) => kyClient(...args),
})

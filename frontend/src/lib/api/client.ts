import createFetchClient from 'openapi-fetch';
import type { paths } from '@/lib/api/schema';

const fetchClient = createFetchClient<paths>({});
export const $api = fetchClient;

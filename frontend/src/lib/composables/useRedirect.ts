import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

/**
 * Composable for handling redirect after authentication
 * @param defaultPath Path to redirect to if no redirect query param exists
 * @returns Function to perform the redirect
 */
export function useRedirect(defaultPath = '/') {
  const route = useRoute()
  const router = useRouter()

  /**
   * Redirects user to the original requested path or default path
   */
  const redirectToOriginal = () => {
    const redirectPath = route.query.redirect as string | undefined

    if (redirectPath) {
      router.push(redirectPath)
    } else {
      router.push(defaultPath)
    }
  }

  return {
    redirectToOriginal,
    hasRedirect: computed(() => !!route.query.redirect),
  }
}

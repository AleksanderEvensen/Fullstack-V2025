import { useMutation } from '@tanstack/vue-query'
import { uploadClient } from '@/lib/api/client'

export function useUploadImage() {
  return useMutation({
    mutationFn: async (image: File) => {
      const formData = new FormData()
      formData.append('file', image)

      const response = await uploadClient.post('/api/images/upload', {
        body: formData,
        headers: {
          'Content-Type': undefined,
        },
      })
      return response.text()
    },
  })
}

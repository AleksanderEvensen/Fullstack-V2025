import { useMutation } from '@tanstack/vue-query'
import { fetchClient, uploadClient } from '@/lib/api/client'
import type { paths } from '../schema'

export function useMutateUserProfilePicture() {
  return useMutation({
    mutationFn: async (image: File) => {
      const formData = new FormData()
      formData.append('file', image)

      const response = await uploadClient.post('/api/user/profile-image', {
        body: formData,
        headers: {
          'Content-Type': undefined,
        },
      })
      return response.text()
    },
  })
}

export function useRemoveUserProfilePicture() {
  return useMutation({
    mutationFn: async () => {
      const response = await fetchClient.POST('/api/user/delete-profile-image')
      return response.data
    },
  })
}

type UpdateUserPasswordRequest =
  paths['/api/auth/update-password']['post']['requestBody']['content']['application/json']
export function useUpdateUserPassword() {
  return useMutation({
    mutationFn: async ({ currentPassword, newPassword }: UpdateUserPasswordRequest) => {
      const response = await fetchClient.POST('/api/auth/update-password', {
        body: {
          currentPassword,
          newPassword,
        },
      })
      return response.data
    },
  })
}

type UpdateUserAddressRequest =
  paths['/api/user/address']['put']['requestBody']['content']['application/json']
export function useUpdateUserAddress() {
  return useMutation({
    mutationFn: async (address: UpdateUserAddressRequest) => {
      const response = await fetchClient.PUT('/api/user/address', {
        body: address,
      })
      return response.data
    },
  })
}

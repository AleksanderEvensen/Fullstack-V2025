import { type ClassValue, clsx } from 'clsx'
import type { components } from './api/schema'

export function cn(...inputs: ClassValue[]) {
  return clsx(inputs)
}

type Address = components['schemas']['AddressDto']
export function formatAddress(address: Address) {
  const addressParts = [
    address.streetName,
    address.streetNumber,
    address.postalCode,
    address.city,
    address.country,
  ].filter(Boolean)
  return addressParts.join(', ')
}

export function formatNameInitials(name: string) {
  const nameParts = name.toUpperCase().split(/\s+/)
  return nameParts
    .map((part) => part.charAt(0))
    .filter(Boolean)
    .join('')
}

export const MAPBOX_API_TOKEN = import.meta.env.VITE_MAPBOX_GL_TOKEN as string

// TODO: This is a temporary function to format the picture url.
// The main purpose of this is to make sure that seeder and uploaded images work
// seeder uses image urls, while uploaded images are saved as filename.generateduuid.extension
export function formatPictureUrl(filename: string | null) {
  if (window === undefined || !filename) {
    return ''
  }
  if (filename.startsWith('http')) {
    return filename
  }
  return `${window.location.origin}/api/images/${filename}`
}

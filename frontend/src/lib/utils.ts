import { type ClassValue, clsx } from 'clsx'
import type { components } from './api/schema'

export function cn(...inputs: ClassValue[]) {
  return clsx(inputs)
}

type Address = components['schemas']['Address']
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

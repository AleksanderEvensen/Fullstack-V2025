import { describe, it, expect, beforeEach, afterEach } from 'vitest'
import { cn, formatAddress, formatNameInitials, formatPictureUrl } from '../utils'
import type { components } from '../api/schema'

describe('cn', () => {
  it('should merge class values correctly', () => {
    expect(cn('class1', 'class2')).toBe('class1 class2')
    expect(cn('class1', { class2: true, class3: false })).toBe('class1 class2')
    expect(cn('class1', ['class2', 'class3'])).toBe('class1 class2 class3')
    expect(cn(null, undefined, 'class1', false, 'class2')).toBe('class1 class2')
  })
})

describe('formatAddress', () => {
  it('should format address correctly with all fields', () => {
    const address: components['schemas']['AddressDto'] = {
      streetName: 'Main Street',
      streetNumber: '123',
      postalCode: '12345',
      city: 'New York',
      country: 'USA',
    }

    expect(formatAddress(address)).toBe('Main Street 123, 12345, New York, USA')
  })

  it('should handle missing fields', () => {
    const address: components['schemas']['AddressDto'] = {
      streetName: 'Tollbugata',
      streetNumber: '2',
      postalCode: '0125',
      city: 'Oslo',
      // @ts-expect-error This line should give a type error
      country: undefined,
    }

    expect(formatAddress(address)).toBe('Tollbugata 2, 0125, Oslo')
  })
})

describe('formatNameInitials', () => {
  it('should format single name correctly', () => {
    expect(formatNameInitials('John')).toBe('J')
  })

  it('should format multiple names correctly', () => {
    expect(formatNameInitials('John Doe')).toBe('JD')
  })

  it('should format multiple names with extra spaces correctly', () => {
    expect(formatNameInitials('John  Doe  Smith')).toBe('JDS')
  })

  it('should handle empty string', () => {
    expect(formatNameInitials('')).toBe('')
  })

  it('should handle lowercase names', () => {
    expect(formatNameInitials('john doe')).toBe('JD')
  })
})

describe('formatPictureUrl', () => {
  const originalLocation = window.location

  beforeEach(() => {
    // Mock the window.location.origin
    Object.defineProperty(window, 'location', {
      value: {
        origin: 'https://amazoom.example.com',
      },
      writable: true,
    })
  })

  afterEach(() => {
    // Restore original location
    Object.defineProperty(window, 'location', {
      value: originalLocation,
      writable: true,
    })
  })

  it('should return empty string for null filename', () => {
    expect(formatPictureUrl(null)).toBe('')
  })

  it('should return the URL as is if it starts with http', () => {
    expect(formatPictureUrl('https://example.com/image.jpg')).toBe('https://example.com/image.jpg')
    expect(formatPictureUrl('http://example.com/image.jpg')).toBe('http://example.com/image.jpg')
  })

  it('should format the URL with the origin for non-http filenames', () => {
    expect(formatPictureUrl('image.jpg')).toBe('https://amazoom.example.com/api/images/image.jpg')
    expect(formatPictureUrl('folder/image.123abc.jpg')).toBe(
      'https://amazoom.example.com/api/images/folder/image.123abc.jpg',
    )
  })
})

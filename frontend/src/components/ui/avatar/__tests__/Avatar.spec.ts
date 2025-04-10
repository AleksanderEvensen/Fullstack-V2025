import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Avatar from '../Avatar.vue'
import AvatarFallback from '../AvatarFallback.vue'
import AvatarImage from '../AvatarImage.vue'
import { h } from 'vue'
import type { SetupContext } from 'vue'

type AvatarSize = 'sm' | 'base' | 'lg'
type AvatarShape = 'circle' | 'square'

vi.mock('reka-ui', () => ({
  AvatarRoot: {
    name: 'AvatarRoot',
    setup:
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      (_: any, { slots }: SetupContext) =>
        () =>
          h('div', { class: 'avatar-root' }, slots.default?.()),
  },
  AvatarFallback: {
    name: 'AvatarFallback',
    setup:
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      (_: any, { slots }: SetupContext) =>
        () =>
          h('div', { class: 'avatar-fallback' }, slots.default?.()),
  },
  AvatarImage: {
    name: 'AvatarImage',
    props: ['src', 'alt'],
    template: '<img :src="src" :alt="alt" class="avatar-image" />',
  },
}))

describe('Avatar', () => {
  it('renders correctly with default props', () => {
    const wrapper = mount(Avatar)

    expect(wrapper.classes()).toContain('avatar')
    expect(wrapper.classes()).toContain('avatar-sm')
    expect(wrapper.classes()).toContain('avatar-circle')
  })

  it('applies different sizes correctly', () => {
    const sizes: AvatarSize[] = ['sm', 'base', 'lg']

    sizes.forEach((size) => {
      const wrapper = mount(Avatar, {
        props: { size },
      })

      expect(wrapper.classes()).toContain(`avatar-${size}`)
    })
  })

  it('applies different shapes correctly', () => {
    const shapes: AvatarShape[] = ['circle', 'square']

    shapes.forEach((shape) => {
      const wrapper = mount(Avatar, {
        props: { shape },
      })

      expect(wrapper.classes()).toContain(`avatar-${shape}`)
    })
  })

  it('renders with slot content', () => {
    const wrapper = mount(Avatar, {
      slots: {
        default: 'Avatar Content',
      },
    })

    expect(wrapper.text()).toBe('Avatar Content')
  })

  it('works as a composite component', () => {
    const wrapper = mount({
      components: { Avatar, AvatarImage, AvatarFallback },
      template: `
        <Avatar>
          <AvatarImage src="/avatar.jpg" alt="User" />
          <AvatarFallback>JD</AvatarFallback>
        </Avatar>
      `,
    })

    expect(wrapper.findComponent(AvatarImage).exists()).toBe(true)
    expect(wrapper.findComponent(AvatarFallback).exists()).toBe(true)
    expect(wrapper.find('.avatar-fallback').text()).toBe('JD')
  })
})

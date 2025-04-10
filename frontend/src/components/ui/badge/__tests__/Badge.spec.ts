import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Badge from '../Badge.vue'

describe('Badge', () => {
  it('renders correctly with default variant', () => {
    const wrapper = mount(Badge, {
      slots: {
        default: 'New',
      },
    })

    expect(wrapper.text()).toBe('New')
    expect(wrapper.classes()).toContain('badge')
    expect(wrapper.classes()).toContain('badge-default')
  })

  it('applies secondary variant correctly', () => {
    const wrapper = mount(Badge, {
      props: {
        variant: 'secondary',
      },
      slots: {
        default: 'Updated',
      },
    })

    expect(wrapper.text()).toBe('Updated')
    expect(wrapper.classes()).toContain('badge')
    expect(wrapper.classes()).toContain('badge-secondary')
  })

  it('applies destructive variant correctly', () => {
    const wrapper = mount(Badge, {
      props: {
        variant: 'destructive',
      },
      slots: {
        default: 'Deleted',
      },
    })

    expect(wrapper.text()).toBe('Deleted')
    expect(wrapper.classes()).toContain('badge')
    expect(wrapper.classes()).toContain('badge-destructive')
  })

  it('applies outline variant correctly', () => {
    const wrapper = mount(Badge, {
      props: {
        variant: 'outline',
      },
      slots: {
        default: 'Draft',
      },
    })

    expect(wrapper.text()).toBe('Draft')
    expect(wrapper.classes()).toContain('badge')
    expect(wrapper.classes()).toContain('badge-outline')
  })

  it('applies additional classes from props', () => {
    const wrapper = mount(Badge, {
      props: {
        class: 'custom-badge large',
      },
      slots: {
        default: 'Custom',
      },
    })

    expect(wrapper.text()).toBe('Custom')
    expect(wrapper.classes()).toContain('badge')
    expect(wrapper.classes()).toContain('custom-badge')
    expect(wrapper.classes()).toContain('large')
  })
})

import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Button from '../Button.vue'

describe('Button', () => {
  it('renders properly with default props', () => {
    const wrapper = mount(Button, {
      slots: {
        default: 'Click Me',
      },
    })
    expect(wrapper.text()).toContain('Click Me')
    expect(wrapper.classes()).toContain('button')
    expect(wrapper.classes()).toContain('button-default')
    expect(wrapper.classes()).toContain('button-size-default')
  })

  it('applies different variants', () => {
    const wrapper = mount(Button, {
      props: { variant: 'destructive' },
      slots: {
        default: 'Delete',
      },
    })
    expect(wrapper.classes()).toContain('button-destructive')
  })

  it('applies different sizes', () => {
    const wrapper = mount(Button, {
      props: { size: 'sm' },
      slots: {
        default: 'Small Button',
      },
    })
    expect(wrapper.classes()).toContain('button-size-sm')
  })

  it('renders as a button by default', () => {
    const wrapper = mount(Button)
    expect(wrapper.element.tagName).toBe('BUTTON')
  })

  it('renders as a custom element when asChild prop is used', () => {
    const wrapper = mount(Button, {
      props: { as: 'a' },
      slots: {
        default: 'Link Button',
      },
    })
    expect(wrapper.element.tagName).toBe('A')
  })

  it('disables the button when disabled prop is true', () => {
    const wrapper = mount(Button, {
      props: { disabled: true },
      slots: {
        default: 'Disabled Button',
      },
    })
    expect(wrapper.attributes('disabled')).toBeDefined()
  })
})

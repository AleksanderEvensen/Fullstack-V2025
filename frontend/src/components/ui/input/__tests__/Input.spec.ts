import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Input from '../Input.vue'

describe('Input', () => {
  it('renders correctly with default props', () => {
    const wrapper = mount(Input)
    const input = wrapper.find('input')

    expect(input.exists()).toBe(true)
    expect(input.classes()).toContain('input')
    expect(input.element.value).toBe('')
  })

  it('renders with initial model value', async () => {
    const wrapper = mount(Input, {
      props: {
        modelValue: 'test value',
      },
    })
    const input = wrapper.find('input')

    expect(input.element.value).toBe('test value')
  })

  it('renders with a default value when provided', async () => {
    const wrapper = mount(Input, {
      props: {
        defaultValue: 'default value',
      },
    })
    const input = wrapper.find('input')

    expect(input.element.value).toBe('default value')
  })

  it('emits update:modelValue event when input value changes', async () => {
    const wrapper = mount(Input, {
      props: {
        modelValue: '',
      },
    })
    const input = wrapper.find('input')

    await input.setValue('new input value')

    expect(wrapper.emitted('update:modelValue')).toBeTruthy()
    expect(wrapper.emitted('update:modelValue')?.[0]).toEqual(['new input value'])
  })

  it('applies additional classes from props', () => {
    const wrapper = mount(Input, {
      props: {
        class: 'extra-class test-class',
      },
    })

    expect(wrapper.classes()).toContain('input')
    expect(wrapper.classes()).toContain('extra-class')
    expect(wrapper.classes()).toContain('test-class')
  })
})

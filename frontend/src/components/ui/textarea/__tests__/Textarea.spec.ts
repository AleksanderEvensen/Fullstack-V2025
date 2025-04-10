import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Textarea from '../Textarea.vue'

describe('Textarea', () => {
  it('renders correctly with default props', () => {
    const wrapper = mount(Textarea)
    const textarea = wrapper.find('textarea')

    expect(textarea.exists()).toBe(true)
    expect(textarea.classes()).toContain('textarea')
    expect(textarea.element.value).toBe('')
  })

  it('renders with initial model value', async () => {
    const wrapper = mount(Textarea, {
      props: {
        modelValue: 'test content',
      },
    })
    const textarea = wrapper.find('textarea')

    expect(textarea.element.value).toBe('test content')
  })

  it('emits update:modelValue event when textarea content changes', async () => {
    const wrapper = mount(Textarea, {
      props: {
        modelValue: '',
      },
    })
    const textarea = wrapper.find('textarea')

    await textarea.setValue('new textarea content')

    expect(wrapper.emitted('update:modelValue')).toBeTruthy()
    expect(wrapper.emitted('update:modelValue')?.[0]).toEqual(['new textarea content'])
  })

  it('applies additional classes from props', () => {
    const wrapper = mount(Textarea, {
      props: {
        class: 'custom-textarea large',
      },
    })

    const textarea = wrapper.find('textarea')
    expect(textarea.classes()).toContain('textarea')
    expect(textarea.classes()).toContain('custom-textarea')
    expect(textarea.classes()).toContain('large')
  })

  it('handles numeric values properly', async () => {
    const wrapper = mount(Textarea, {
      props: {
        modelValue: 123,
      },
    })
    const textarea = wrapper.find('textarea')

    expect(textarea.element.value).toBe('123')

    await textarea.setValue('456')
    expect(wrapper.emitted('update:modelValue')?.[0]).toEqual(['456'])
  })
})

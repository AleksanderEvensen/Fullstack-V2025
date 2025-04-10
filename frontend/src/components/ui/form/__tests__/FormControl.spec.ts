import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import FormControl from '../FormControl.vue'
import { computed } from 'vue'

const mockUseFormField = {
  error: computed(() => null as string | null),
  formItemId: 'test-form-item-id',
  formDescriptionId: 'test-form-description-id',
  formMessageId: 'test-form-message-id',
  valid: computed(() => true),
  isDirty: computed(() => false),
  isTouched: computed(() => false),
  id: 'form-item',
  name: 'form-item',
}

vi.mock('../useFormField', () => ({
  useFormField: vi.fn(() => mockUseFormField),
}))

describe('FormControl', () => {
  it('renders correctly with default props', () => {
    const wrapper = mount(FormControl, {
      slots: {
        default: '<input type="text" placeholder="Test input" />',
      },
    })

    expect(wrapper.classes()).toContain('form-control')
    expect(wrapper.attributes('data-error')).toBe('false')
    expect(wrapper.find('input').exists()).toBe(true)
  })

  it('applies additional classes from props', () => {
    const wrapper = mount(FormControl, {
      props: {
        class: 'custom-form-control wide',
      },
      slots: {
        default: '<input type="text" placeholder="Test input" />',
      },
    })

    expect(wrapper.classes()).toContain('form-control')
    expect(wrapper.classes()).toContain('custom-form-control')
    expect(wrapper.classes()).toContain('wide')
  })

  it('passes correct accessibility attributes to slotted element', async () => {
    const wrapper = mount({
      components: { FormControl },
      template: `
        <FormControl>
          <template #default="slotProps">
            <input v-bind="slotProps" />
          </template>
        </FormControl>
      `,
    })

    await wrapper.vm.$nextTick()
    const input = wrapper.find('input')
    expect(input.attributes('id')).toBe('test-form-item-id')
  })

  it('applies error state correctly', async () => {
    // Create a properly typed error mock with error as string
    const errorMock = {
      error: computed(() => 'This field is required'),
      formItemId: 'test-form-item-id',
      formDescriptionId: 'test-form-description-id',
      formMessageId: 'test-form-message-id',
      valid: computed(() => false),
      isDirty: computed(() => true),
      isTouched: computed(() => true),
      id: 'form-item',
      name: 'form-item',
    }

    const { useFormField } = await import('../useFormField')
    vi.mocked(useFormField).mockReturnValueOnce(errorMock)

    const wrapper = mount({
      components: { FormControl },
      template: `
        <FormControl>
          <template #default="slotProps">
            <input v-bind="slotProps" />
          </template>
        </FormControl>
      `,
    })

    expect(wrapper.attributes('data-error')).toBe('true')
  })
})

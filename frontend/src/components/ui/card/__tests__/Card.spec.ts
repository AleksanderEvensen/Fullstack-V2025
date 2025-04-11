import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import Card from '../Card.vue'
import CardHeader from '../CardHeader.vue'
import CardTitle from '../CardTitle.vue'
import CardDescription from '../CardDescription.vue'
import CardContent from '../CardContent.vue'
import CardFooter from '../CardFooter.vue'

describe('Card Components', () => {
  describe('Card', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(Card, {
        slots: {
          default: 'Card Content',
        },
      })

      expect(wrapper.text()).toBe('Card Content')
      expect(wrapper.classes()).toContain('_card-component')
      expect(wrapper.classes()).toContain('rounded')
    })

    it('applies additional classes from props', () => {
      const wrapper = mount(Card, {
        props: {
          class: 'custom-card theme-dark',
        },
        slots: {
          default: 'Card Content',
        },
      })

      expect(wrapper.classes()).toContain('_card-component')
      expect(wrapper.classes()).toContain('custom-card')
      expect(wrapper.classes()).toContain('theme-dark')
    })
  })

  describe('CardHeader', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(CardHeader, {
        slots: {
          default: 'Header Content',
        },
      })

      expect(wrapper.text()).toBe('Header Content')
      expect(wrapper.classes()).toContain('_card-header-component')
    })
  })

  describe('CardTitle', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(CardTitle, {
        slots: {
          default: 'Card Title',
        },
      })

      expect(wrapper.text()).toBe('Card Title')
      expect(wrapper.element.tagName).toBe('H3')
      expect(wrapper.classes()).toContain('_card-title-component')
      expect(wrapper.classes()).toContain('text-2xl')
    })
  })

  describe('CardDescription', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(CardDescription, {
        slots: {
          default: 'Card Description',
        },
      })

      expect(wrapper.text()).toBe('Card Description')
      expect(wrapper.element.tagName).toBe('P')
      expect(wrapper.classes()).toContain('_card-description-component')
      expect(wrapper.classes()).toContain('text-sm')
    })
  })

  describe('CardContent', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(CardContent, {
        slots: {
          default: 'Content Area',
        },
      })

      expect(wrapper.text()).toBe('Content Area')
      expect(wrapper.classes()).toContain('_card-content-component')
    })
  })

  describe('CardFooter', () => {
    it('renders correctly with default props', () => {
      const wrapper = mount(CardFooter, {
        slots: {
          default: 'Footer Content',
        },
      })

      expect(wrapper.text()).toBe('Footer Content')
      expect(wrapper.classes()).toContain('_card-content-component')
    })
  })

  describe('Card System Integration', () => {
    it('works as a composite component system', () => {
      const wrapper = mount({
        components: {
          Card,
          CardHeader,
          CardTitle,
          CardDescription,
          CardContent,
          CardFooter,
        },
        template: `
          <Card>
            <CardHeader>
              <CardTitle>Example Card</CardTitle>
              <CardDescription>This is a description of the card</CardDescription>
            </CardHeader>
            <CardContent>
              <p>Here is the main content of the card</p>
            </CardContent>
            <CardFooter>
              <span>Footer information</span>
            </CardFooter>
          </Card>
        `,
      })

      expect(wrapper.find('h3').text()).toBe('Example Card')
      expect(wrapper.findAll('p')[0].text()).toBe('This is a description of the card')
      expect(wrapper.findAll('p')[1].text()).toBe('Here is the main content of the card')
      expect(wrapper.find('span').text()).toBe('Footer information')
    })
  })
})

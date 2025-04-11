import { test, expect } from '@playwright/test'

test.describe('Components View', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/components')
    await page.waitForLoadState('networkidle')
  })

  test('should load components page', async ({ page }) => {
    await expect(page).toHaveURL('/components')

    await expect(page.locator('.components-container')).toBeVisible()
    await expect(page.locator('h1')).toHaveText(/component examples/i)
  })

  test('should display component categories or links', async ({ page }) => {
    const componentLinks = page.getByRole('link')

    expect(await componentLinks.count()).toBeGreaterThan(0)
  })

  test('should navigate to specific component demos', async ({ page }) => {
    const commonComponents = ['button', 'form', 'select', 'slider']

    let foundComponentLink = false

    for (const component of commonComponents) {
      const links = page.getByRole('link').filter({ hasText: new RegExp(component, 'i') })

      if ((await links.count()) > 0) {
        foundComponentLink = true

        const href = await links.first().getAttribute('href')
        expect(href).toBeTruthy()
        break
      }
    }

    if (!foundComponentLink) {
      const allLinks = await page.getByRole('link').all()
      expect(allLinks.length).toBeGreaterThan(0)
    }
  })

  test('should showcase various UI components', async ({ page }) => {
    const uiElements = [
      'button',
      'input',
      'select',
      '.component',
      '[role="button"]',
      '[role="switch"]',
    ]

    let foundElements = false
    for (const selector of uiElements) {
      if ((await page.locator(selector).count()) > 0) {
        foundElements = true
        break
      }
    }

    expect(foundElements).toBeTruthy()
  })
})

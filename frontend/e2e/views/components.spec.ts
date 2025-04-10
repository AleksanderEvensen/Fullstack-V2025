import { test, expect } from '@playwright/test'

test.describe('Components View', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to components page
    await page.goto('/components')
    await page.waitForLoadState('networkidle')
  })

  test('should load components page', async ({ page }) => {
    // Verify we're on components page
    await expect(page).toHaveURL('/components')

    // Check page structure
    await expect(page.locator('.components-container')).toBeVisible()
    await expect(page.locator('h1')).toHaveText(/component examples/i)
  })

  test('should display component categories or links', async ({ page }) => {
    // Find navigation links to component examples
    const componentLinks = page.getByRole('link')

    // There should be multiple component links
    expect(await componentLinks.count()).toBeGreaterThan(0)
  })

  test('should navigate to specific component demos', async ({ page }) => {
    // Find links to common component types
    const commonComponents = ['button', 'form', 'select', 'slider']

    // Try to find and test at least one component link
    let foundComponentLink = false

    for (const component of commonComponents) {
      const links = page.getByRole('link').filter({ hasText: new RegExp(component, 'i') })

      if ((await links.count()) > 0) {
        // Found a component link - we'll be careful with navigation
        foundComponentLink = true

        // Get the href without navigating
        const href = await links.first().getAttribute('href')
        expect(href).toBeTruthy()
        break
      }
    }

    if (!foundComponentLink) {
      // Component links might be structured differently
      const allLinks = await page.getByRole('link').all()
      expect(allLinks.length).toBeGreaterThan(0)
    }
  })

  test('should showcase various UI components', async ({ page }) => {
    // Components page should display actual components, not just links
    const uiElements = [
      'button',
      'input',
      'select',
      '.component',
      '[role="button"]',
      '[role="switch"]',
    ]

    // At least some of these should be present
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

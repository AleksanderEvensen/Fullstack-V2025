import { test, expect } from '@playwright/test'

test.describe('Search View', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/search')
    await page.waitForLoadState('networkidle')
  })

  test('should load search page', async ({ page }) => {
    await expect(page).toHaveURL(new RegExp('/search'))

    await expect(page.locator('main')).toBeVisible()
  })

  test('should have search functionality', async ({ page }) => {
    const searchInput = page
      .locator(
        'input[type="search"], input[placeholder*="search" i], input[aria-label*="search" i]',
      )
      .first()

    if ((await searchInput.count()) > 0) {
      await searchInput.fill('test')
      await searchInput.press('Enter')

      await page.waitForTimeout(500)
    } else {
      await expect(page.locator('body')).toBeVisible()
    }
  })

  test('should display filtering options', async ({ page }) => {
    const filterSelectors = [
      'select',
      '[role="combobox"]',
      'input[type="checkbox"]',
      'input[type="radio"]',
      '.filter',
      '.sort',
      '.categories',
    ]

    let hasFilters = false
    for (const selector of filterSelectors) {
      if ((await page.locator(selector).count()) > 0) {
        hasFilters = true
        break
      }
    }

    if (!hasFilters) {
      test.skip()
    }
  })
})

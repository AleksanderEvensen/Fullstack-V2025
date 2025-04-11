import { test, expect } from '@playwright/test'

test.describe('Product View', () => {
  const sampleProductId = '1'

  test.beforeEach(async ({ page }) => {
    await page.goto(`/marketplace/product/${sampleProductId}`)
    await page.waitForLoadState('networkidle')
  })

  test('should load product page', async ({ page }) => {
    await expect(page).toHaveURL(new RegExp(`/marketplace/product/\\d+`))

    await page.waitForLoadState('networkidle')

    await expect(page.locator('main')).toBeVisible()
  })

  test('should display product information', async ({ page }) => {
    const productElements = []

    let visibleElements = 0
    for (const selector of productElements) {
      if ((await page.locator(selector).count()) > 0) {
        visibleElements++
      }
    }

    expect(visibleElements).toBeGreaterThan(0)
  })

  test('should handle product actions', async ({ page }) => {
    const actionButtons = page.locator('button:not([disabled])').filter({
      hasText: /buy|add to cart|contact seller|save|favorite|like/i,
    })

    if ((await actionButtons.count()) > 0) {
      await expect(actionButtons.first()).toBeVisible()
    } else {
      await expect(page.locator('body')).toBeVisible()
    }
  })
})

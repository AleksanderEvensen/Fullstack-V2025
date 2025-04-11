import { test, expect } from '@playwright/test'

test.describe('Component Demo Views', () => {
  test('should display slider demo properly', async ({ page }) => {
    await page.goto('/components')
    await page.waitForLoadState('networkidle')

    const sliderLinks = page.getByRole('link').filter({ hasText: /slider/i })

    if ((await sliderLinks.count()) > 0) {
      await sliderLinks.first().click()
      await page.waitForLoadState('networkidle')

      await expect(page.locator('.slider-demo-page, [role="slider"]').first()).toBeVisible()

      await expect(page.getByText(/value:/i).first()).toBeVisible()

      const slider = page.locator('[role="slider"]').first()
      if (await slider.isVisible()) {
        const initialBoundingBox = await slider.boundingBox()

        if (initialBoundingBox) {
          await page.mouse.click(
            initialBoundingBox.x + initialBoundingBox.width / 2,
            initialBoundingBox.y + initialBoundingBox.height / 2,
          )

          await expect(page.locator('body')).toBeVisible()
        }
      }
    }
  })

  test('should display select component demo properly', async ({ page }) => {
    await page.goto('/components')
    await page.waitForLoadState('networkidle')

    const selectLinks = page.getByRole('link').filter({ hasText: /select/i })

    if ((await selectLinks.count()) > 0) {
      await selectLinks.first().click()
      await page.waitForLoadState('networkidle')

      await expect(page.locator('.select-test, select, [role="combobox"]').first()).toBeVisible()

      await expect(page.getByText(/vue|react|angular|svelte/i).first()).toBeVisible()

      await expect(page.getByText(/selected value/i).first()).toBeVisible()

      const selectTrigger = page.locator('[role="combobox"], select, .select-trigger').first()
      if (await selectTrigger.isVisible()) {
        await selectTrigger.click()

        await page.waitForTimeout(100)

        const option = page.getByRole('option').first()
        if (await option.isVisible()) {
          await option.click()

          await expect(page.locator('body')).toBeVisible()
        } else {
          await page.mouse.click(0, 0)
        }
      }
    }
  })
})

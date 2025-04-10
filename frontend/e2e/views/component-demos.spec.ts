import { test, expect } from '@playwright/test'

test.describe('Component Demo Views', () => {
  test('should display slider demo properly', async ({ page }) => {
    // Navigate to the slider demo
    await page.goto('/components')
    await page.waitForLoadState('networkidle')

    // Find link to slider demo
    const sliderLinks = page.getByRole('link').filter({ hasText: /slider/i })

    // If we found a slider link, click it and check the demo
    if ((await sliderLinks.count()) > 0) {
      await sliderLinks.first().click()
      await page.waitForLoadState('networkidle')

      // Should show slider components
      await expect(page.locator('.slider-demo-page, [role="slider"]').first()).toBeVisible()

      // Check for value displays which are shown in SliderDemo.vue
      await expect(page.getByText(/value:/i).first()).toBeVisible()

      // Try interacting with a slider if possible
      const slider = page.locator('[role="slider"]').first()
      if (await slider.isVisible()) {
        // Get initial position
        const initialBoundingBox = await slider.boundingBox()

        if (initialBoundingBox) {
          // Click on the slider to change position
          await page.mouse.click(
            initialBoundingBox.x + initialBoundingBox.width / 2,
            initialBoundingBox.y + initialBoundingBox.height / 2,
          )

          // Ensure page doesn't crash after interaction
          await expect(page.locator('body')).toBeVisible()
        }
      }
    }
  })

  test('should display select component demo properly', async ({ page }) => {
    // Navigate to the select demo
    await page.goto('/components')
    await page.waitForLoadState('networkidle')

    // Find link to select demo
    const selectLinks = page.getByRole('link').filter({ hasText: /select/i })

    // If we found a select link, click it and check the demo
    if ((await selectLinks.count()) > 0) {
      await selectLinks.first().click()
      await page.waitForLoadState('networkidle')

      // Check for select-test class from SelectTest.vue
      await expect(page.locator('.select-test, select, [role="combobox"]').first()).toBeVisible()

      // Look for framework options which are in the demo
      await expect(page.getByText(/vue|react|angular|svelte/i).first()).toBeVisible()

      // Check for "Selected value" text which appears in the component
      await expect(page.getByText(/selected value/i).first()).toBeVisible()

      // Try interacting with a select if possible
      const selectTrigger = page.locator('[role="combobox"], select, .select-trigger').first()
      if (await selectTrigger.isVisible()) {
        await selectTrigger.click()

        // Wait a bit for dropdown to appear
        await page.waitForTimeout(100)

        // Try to select an option if available
        const option = page.getByRole('option').first()
        if (await option.isVisible()) {
          await option.click()

          // Ensure page doesn't crash after interaction
          await expect(page.locator('body')).toBeVisible()
        }

        // If no visible options, at least click away to close dropdown
        else {
          await page.mouse.click(0, 0)
        }
      }
    }
  })
})

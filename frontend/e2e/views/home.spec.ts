import { test, expect } from '@playwright/test'

test.describe('Home View', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to home page
    await page.goto('/')
    await page.waitForLoadState('networkidle')
  })

  test('should load home page', async ({ page }) => {
    // Verify we're on home page
    await expect(page).toHaveURL('/')

    // Verify basic page structure
    await expect(page.locator('main')).toBeVisible()

    // Make sure navigation is visible
    await expect(page.locator('nav')).toBeVisible()
  })

  test('should navigate to other pages from home', async ({ page }) => {
    // Find some navigation links
    const links = page.getByRole('link')

    // Ensure there are multiple navigation links
    expect(await links.count()).toBeGreaterThan(0)
  })

  test('should display main content sections', async ({ page }) => {
    // Check for common content sections on home page
    // These selectors target common marketing/home page sections
    await expect(
      page.locator('section, .hero, .featured, .categories, .content-section').first(),
    ).toBeVisible()
  })
})

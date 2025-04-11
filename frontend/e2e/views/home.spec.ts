import { test, expect } from '@playwright/test'

test.describe('Home View', () => {
  test.beforeEach(async ({ page }) => {
    await page.goto('/')
    await page.waitForLoadState('networkidle')
  })

  test('should load home page', async ({ page }) => {
    await expect(page).toHaveURL('/')

    await expect(page.locator('main')).toBeVisible()

    await expect(page.locator('nav')).toBeVisible()
  })

  test('should navigate to other pages from home', async ({ page }) => {
    const links = page.getByRole('link')

    expect(await links.count()).toBeGreaterThan(0)
  })

  test('should display main content sections', async ({ page }) => {
    await expect(
      page.locator('section, .hero, .featured, .categories, .content-section').first(),
    ).toBeVisible()
  })
})

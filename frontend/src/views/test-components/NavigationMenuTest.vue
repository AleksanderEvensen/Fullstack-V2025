<script setup lang="ts">
import {
    NavigationMenu,
    NavigationMenuContent,
    NavigationMenuItem,
    NavigationMenuLink,
    NavigationMenuList,
    NavigationMenuTrigger,
} from '@/components/ui/navigation-menu'

const components = [
    {
        title: 'Alert Dialog',
        href: '/docs/primitives/alert-dialog',
        description: 'A modal dialog that interrupts the user with important content and expects a response.',
    },
    {
        title: 'Hover Card',
        href: '/docs/primitives/hover-card',
        description: 'For sighted users to preview content available behind a link.',
    },
    {
        title: 'Progress',
        href: '/docs/primitives/progress',
        description: 'Displays an indicator showing the completion progress of a task.',
    },
]

const frameworks = [
    {
        title: 'Vue',
        href: '/docs/frameworks/vue',
        description: 'The progressive JavaScript framework for building user interfaces.',
    },
    {
        title: 'React',
        href: '/docs/frameworks/react',
        description: 'A JavaScript library for building user interfaces.',
    },
    {
        title: 'Svelte',
        href: '/docs/frameworks/svelte',
        description: 'Cybernetically enhanced web apps.',
    },
]
</script>

<template>
    <div class="nav-menu-test">
        <h2>Navigation Menu Component</h2>

        <div class="nav-menu-examples">
            <NavigationMenu>
                <NavigationMenuList>
                    <!-- Basic Link -->
                    <NavigationMenuItem>
                        <NavigationMenuLink href="/docs">
                            Documentation
                        </NavigationMenuLink>
                    </NavigationMenuItem>

                    <!-- Components Dropdown -->
                    <NavigationMenuItem>
                        <NavigationMenuTrigger>Components</NavigationMenuTrigger>
                        <NavigationMenuContent>
                            <ul class="nav-menu-grid">
                                <li v-for="component in components" :key="component.title">
                                    <NavigationMenuLink :href="component.href">
                                        <div class="nav-menu-item">
                                            <h3>{{ component.title }}</h3>
                                            <p>{{ component.description }}</p>
                                        </div>
                                    </NavigationMenuLink>
                                </li>
                            </ul>
                        </NavigationMenuContent>
                    </NavigationMenuItem>

                    <!-- Frameworks Dropdown -->
                    <NavigationMenuItem>
                        <NavigationMenuTrigger>Frameworks</NavigationMenuTrigger>
                        <NavigationMenuContent>
                            <ul class="nav-menu-list">
                                <li v-for="framework in frameworks" :key="framework.title">
                                    <NavigationMenuLink :href="framework.href">
                                        <div class="nav-menu-item">
                                            <h3>{{ framework.title }}</h3>
                                            <p>{{ framework.description }}</p>
                                        </div>
                                    </NavigationMenuLink>
                                </li>
                            </ul>
                        </NavigationMenuContent>
                    </NavigationMenuItem>
                </NavigationMenuList>
            </NavigationMenu>
        </div>
    </div>
</template>

<style>
.nav-menu-test {
    padding: calc(var(--spacing) * 4);
}

h2 {
    font-size: var(--font-size-2xl);
    font-weight: var(--font-weight-bold);
    margin-bottom: calc(var(--spacing) * 4);
    color: var(--foreground);
}

.nav-menu-examples {
    display: flex;
    justify-content: center;
    width: 100%;
    padding: calc(var(--spacing) * 4);
    background-color: var(--background);
    border-radius: var(--radius-lg);
    border: 1px solid var(--border);
}

.nav-menu-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: calc(var(--spacing) * 2);
    padding: calc(var(--spacing) * 2);
    width: calc(var(--spacing) * 80);
    list-style: none;
}

.nav-menu-list {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 2);
    padding: calc(var(--spacing) * 2);
    width: calc(var(--spacing) * 60);
    list-style: none;
}

.nav-menu-item {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 1);
    padding: calc(var(--spacing) * 2);
    border-radius: var(--radius-md);
    transition: background-color 0.2s;
}

.nav-menu-item:hover {
    background-color: var(--accent);
}

.nav-menu-item h3 {
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-medium);
    color: var(--foreground);
}

.nav-menu-item p {
    font-size: var(--font-size-sm);
    color: var(--muted-foreground);
    line-height: 1.4;
}

@media (max-width: 768px) {
    .nav-menu-grid {
        grid-template-columns: 1fr;
        width: 100%;
    }

    .nav-menu-list {
        width: 100%;
    }
}
</style>
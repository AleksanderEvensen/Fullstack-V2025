<script setup lang="ts">
import Button from "@/components/ui/button/Button.vue";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuPortal,
    DropdownMenuSeparator,
    DropdownMenuShortcut,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
</script>

<template>
    <section class="component-section">
        <h2>Dropdowns</h2>
        <div class="component-grid">
            <div class="component-example">
                <h3>Basic Dropdown</h3>
                <div class="example-content">
                    <DropdownMenu>
                        <DropdownMenuTrigger as-child>
                            <Button variant="outline">Open Menu</Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent>
                            <DropdownMenuLabel>My Account</DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuGroup>
                                <DropdownMenuItem>
                                    <span>Profile</span>
                                    <DropdownMenuShortcut>⇧⌘P</DropdownMenuShortcut>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    <span>Settings</span>
                                    <DropdownMenuShortcut>⌘S</DropdownMenuShortcut>
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem disabled>
                                <span>API</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                                <span>Log out</span>
                                <DropdownMenuShortcut>⇧⌘Q</DropdownMenuShortcut>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>

            <div class="component-example">
                <h3>Nested Dropdown</h3>
                <div class="example-content">
                    <DropdownMenu>
                        <DropdownMenuTrigger as-child>
                            <Button variant="outline">Nested Menu</Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent>
                            <DropdownMenuSub>
                                <DropdownMenuSubTrigger>
                                    <span>More options</span>
                                </DropdownMenuSubTrigger>
                                <DropdownMenuPortal>
                                    <DropdownMenuSubContent>
                                        <DropdownMenuItem>
                                            <span>Sub Item 1</span>
                                        </DropdownMenuItem>
                                        <DropdownMenuItem>
                                            <span>Sub Item 2</span>
                                        </DropdownMenuItem>
                                    </DropdownMenuSubContent>
                                </DropdownMenuPortal>
                            </DropdownMenuSub>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>
        </div>
    </section>
</template>

<style scoped>
.component-section {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 6);
}

.component-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: calc(var(--spacing) * 6);
}

.component-example {
    display: flex;
    flex-direction: column;
    gap: calc(var(--spacing) * 4);
    padding: calc(var(--spacing) * 4);
    background-color: var(--background);
    border: 1px solid var(--border);
    border-radius: var(--radius);
}

.example-content {
    display: flex;
    flex-wrap: wrap;
    gap: calc(var(--spacing) * 2);
    align-items: flex-start;
}
</style>
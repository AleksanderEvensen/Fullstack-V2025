<script setup lang="ts">
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
</script>

<template>
    <section class="component-section">
        <h2>Avatars</h2>
        <div class="component-grid">
            <div class="component-example">
                <h3>Sizes</h3>
                <div class="example-content">
                    <Avatar size="sm">
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <Avatar size="base">
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <Avatar size="lg">
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                </div>
            </div>

            <div class="component-example">
                <h3>Shapes & Fallback</h3>
                <div class="example-content">
                    <Avatar shape="circle">
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <Avatar shape="square">
                        <AvatarImage src="https://github.com/shadcn.png" alt="@shadcn" />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <Avatar>
                        <AvatarImage src="broken-image.jpg" alt="@broken" />
                        <AvatarFallback>JD</AvatarFallback>
                    </Avatar>
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
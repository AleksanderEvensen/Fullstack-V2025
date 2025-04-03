<script setup lang="ts">
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from '@/components/ui/sheet'
import { Button } from '@/components/ui/button'
import { Menu } from 'lucide-vue-next'
</script>

<template>
  <div class="sheet-test">
    <h2>Sheet Components</h2>

    <div class="sheet-examples">
      <div class="sheet-example">
        <h3>Basic Sheet</h3>
        <Sheet>
          <SheetTrigger as-child>
            <Button variant="outline">
              <Menu class="sheet-trigger-icon" />
              Open Sheet
            </Button>
          </SheetTrigger>
          <SheetContent>
            <SheetHeader>
              <SheetTitle>Edit profile</SheetTitle>
              <SheetDescription>
                Make changes to your profile here. Click save when you're done.
              </SheetDescription>
            </SheetHeader>
            <div class="sheet-content">
              <p>Sheet content goes here...</p>
            </div>
            <SheetFooter>
              <Button>Save changes</Button>
            </SheetFooter>
          </SheetContent>
        </Sheet>
      </div>

      <div class="sheet-example">
        <h3>Sheet with Different Sides</h3>
        <div class="sheet-sides">
          <Sheet>
            <SheetTrigger as-child>
              <Button variant="outline">Open Top Sheet</Button>
            </SheetTrigger>
            <SheetContent side="top">
              <SheetHeader>
                <SheetTitle>Top Sheet</SheetTitle>
                <SheetDescription>This sheet slides in from the top.</SheetDescription>
              </SheetHeader>
            </SheetContent>
          </Sheet>

          <Sheet>
            <SheetTrigger as-child>
              <Button variant="outline">Open Right Sheet</Button>
            </SheetTrigger>
            <SheetContent side="right">
              <SheetHeader>
                <SheetTitle>Right Sheet</SheetTitle>
                <SheetDescription>This sheet slides in from the right.</SheetDescription>
              </SheetHeader>
            </SheetContent>
          </Sheet>

          <Sheet>
            <SheetTrigger as-child>
              <Button variant="outline">Open Bottom Sheet</Button>
            </SheetTrigger>
            <SheetContent side="bottom">
              <SheetHeader>
                <SheetTitle>Bottom Sheet</SheetTitle>
                <SheetDescription>This sheet slides in from the bottom.</SheetDescription>
              </SheetHeader>
            </SheetContent>
          </Sheet>

          <Sheet>
            <SheetTrigger as-child>
              <Button variant="outline">Open Left Sheet</Button>
            </SheetTrigger>
            <SheetContent side="left">
              <SheetHeader>
                <SheetTitle>Left Sheet</SheetTitle>
                <SheetDescription>This sheet slides in from the left.</SheetDescription>
              </SheetHeader>
            </SheetContent>
          </Sheet>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.sheet-test {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 4);
  padding: calc(var(--spacing) * 4);
}

.sheet-examples {
  display: grid;
  gap: calc(var(--spacing) * 4);
}

.sheet-example {
  display: flex;
  flex-direction: column;
  gap: calc(var(--spacing) * 2);
}

.sheet-sides {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: calc(var(--spacing) * 2);
}

.sheet-trigger-icon {
  width: var(--icon-size);
  height: var(--icon-size);
  margin-right: calc(var(--spacing) * 1);
}

.sheet-content {
  margin-top: calc(var(--spacing) * 4);
}
</style>

<script setup lang="ts">
import { ref } from 'vue'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs'

const activeTab = ref('account')
</script>

<template>
  <section class="component-section">
    <h2>Tabs</h2>
    <div class="component-grid">
      <div class="component-example">
        <h3>Basic Tabs</h3>
        <div class="example-content">
          <Tabs v-model="activeTab" class="w-full">
            <TabsList>
              <TabsTrigger value="account">Account</TabsTrigger>
              <TabsTrigger value="password">Password</TabsTrigger>
              <TabsTrigger value="settings">Settings</TabsTrigger>
            </TabsList>
            <TabsContent value="account">
              <div class="tabs-panel">
                <h4>Account Settings</h4>
                <p>Configure your account preferences and personal information.</p>
              </div>
            </TabsContent>
            <TabsContent value="password">
              <div class="tabs-panel">
                <h4>Password Settings</h4>
                <p>Change your password and security settings.</p>
              </div>
            </TabsContent>
            <TabsContent value="settings">
              <div class="tabs-panel">
                <h4>General Settings</h4>
                <p>Manage your application settings and preferences.</p>
              </div>
            </TabsContent>
          </Tabs>
        </div>
      </div>

      <div class="component-example">
        <h3>Disabled State</h3>
        <div class="example-content">
          <Tabs defaultValue="active">
            <TabsList>
              <TabsTrigger value="active">Active</TabsTrigger>
              <TabsTrigger value="disabled" disabled>Disabled</TabsTrigger>
              <TabsTrigger value="pending">Pending</TabsTrigger>
            </TabsList>
            <TabsContent value="active">
              <div class="tabs-panel">
                <h4>Active Tab</h4>
                <p>This tab is active and can be interacted with.</p>
              </div>
            </TabsContent>
            <TabsContent value="disabled">
              <div class="tabs-panel">
                <p>This content is not accessible.</p>
              </div>
            </TabsContent>
            <TabsContent value="pending">
              <div class="tabs-panel">
                <h4>Pending Tab</h4>
                <p>This tab shows pending content.</p>
              </div>
            </TabsContent>
          </Tabs>
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

.tabs-panel {
  padding: calc(var(--spacing) * 4);
  background-color: var(--background);
  border: 1px solid var(--border);
  border-radius: var(--radius);
}

.tabs-panel h4 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-medium);
  color: var(--foreground);
  margin-bottom: calc(var(--spacing) * 2);
}

.tabs-panel p {
  color: var(--muted-foreground);
  font-size: var(--font-size-sm);
  line-height: 1.6;
}
</style>

import { Component } from 'vue'
import { RouteRecordRaw } from 'vue-router'

export const createNameComponent = (component: () => Promise<Component>): RouteRecordRaw['component'] => {
  return component
} 
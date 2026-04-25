import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/HomeView.vue'),
  },
  {
    path: '/spots/:id',
    name: 'SpotDetail',
    component: () => import('@/views/SpotDetailView.vue'),
  },
  {
    path: '/folk/:id',
    name: 'FolkDetail',
    component: () => import('@/views/FolkDetailView.vue'),
  },
  {
    path: '/music',
    name: 'Music',
    component: () => import('@/views/MusicView.vue'),
  },
  {
    path: '/scripture',
    name: 'Scripture',
    component: () => import('@/views/ScriptureView.vue'),
  },
  {
    path: '/translate',
    name: 'Translate',
    component: () => import('@/views/TranslateView.vue'),
  },
  {
    path: '/calendar',
    name: 'Calendar',
    component: () => import('@/views/CalendarView.vue'),
  },
  {
    path: '/notes',
    name: 'Notes',
    component: () => import('@/views/NoteView.vue'),
  },
  {
    path: '/phrases',
    name: 'Phrases',
    component: () => import('@/views/PhraseView.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 }),
})

export default router

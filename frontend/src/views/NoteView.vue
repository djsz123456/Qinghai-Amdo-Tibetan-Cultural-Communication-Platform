<template>
  <div class="note-page-wrapper">
    <div class="page-body">
      <div class="note-page">
        <div class="note-sidebar">
          <div class="sidebar-header">
            <h3><i class="fa fa-book"></i> 笔记列表</h3>
            <button class="btn-new" @click="newNote"><i class="fa fa-plus"></i> 新建</button>
          </div>
          <div class="note-list">
            <div v-if="notes.length === 0" class="sidebar-empty">
              <i class="fa fa-file-text-o"></i>
              <p>暂无笔记<br /><small>点击"新建"开始记录</small></p>
            </div>
            <div v-for="note in notes" :key="note.id"
                 class="note-item"
                 :class="{ active: currentNoteId === note.id }"
                 @click="loadNote(note)">
              <div class="note-item-title">{{ note.title || '无标题笔记' }}</div>
              <div class="note-item-preview">{{ note.content ? note.content.substring(0, 50) : '空白笔记...' }}</div>
              <div class="note-item-time">{{ formatTime(note.updatedAt) }}</div>
              <button class="note-item-delete" @click.stop="deleteNote(note.id)" title="删除笔记">
                <i class="fa fa-trash-o"></i>
              </button>
            </div>
          </div>
          <div v-if="notes.length > 0" class="sidebar-count">共 {{ notes.length }} 条笔记</div>
        </div>

        <div class="note-main">
          <div class="note-header">
            藏文笔记 ཡིག་གཟུགས།
            <span v-if="hasUnsaved" class="unsaved-dot" title="有未保存的更改"></span>
          </div>
          <input v-model="noteTitle" type="text" class="note-title" placeholder="请输入笔记标题" @input="markUnsaved" />
          <textarea v-model="noteContent" class="note-editor" placeholder="请在这里书写藏文或汉文笔记..." @input="markUnsaved"></textarea>
          <div class="note-buttons">
            <button class="btn-save" @click="saveNote"><i class="fa fa-floppy-o"></i> 保存笔记</button>
            <button class="btn-clear" @click="clearNote"><i class="fa fa-eraser"></i> 清空内容</button>
          </div>
        </div>

        <transition name="fade">
          <div v-if="toastShow" class="toast">{{ toastText }}</div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'

interface Note {
  id: number
  title: string
  content: string
  createdAt: number
  updatedAt: number
}

const userStore = useUserStore()
const noteTitle = ref('')
const noteContent = ref('')
const notes = ref<Note[]>([])
const currentNoteId = ref<number | null>(null)
const hasUnsaved = ref(false)
const toastShow = ref(false)
const toastText = ref('')

function getStorageKey() {
  return `tibetan_notes_${userStore.userName}`
}

function loadNotes() {
  if (!userStore.isLoggedIn) return
  try {
    notes.value = JSON.parse(localStorage.getItem(getStorageKey()) || '[]')
  } catch { notes.value = [] }
  if (notes.value.length > 0) loadNote(notes.value[0])
}

function saveNote() {
  if (!userStore.isLoggedIn) return
  const now = Date.now()
  if (currentNoteId.value) {
    const idx = notes.value.findIndex(n => n.id === currentNoteId.value)
    if (idx !== -1) {
      notes.value[idx].title = noteTitle.value
      notes.value[idx].content = noteContent.value
      notes.value[idx].updatedAt = now
    }
  } else {
    const newNote: Note = { id: now, title: noteTitle.value, content: noteContent.value, createdAt: now, updatedAt: now }
    notes.value.unshift(newNote)
    currentNoteId.value = newNote.id
  }
  localStorage.setItem(getStorageKey(), JSON.stringify(notes.value))
  hasUnsaved.value = false
  showToast('笔记保存成功')
}

function newNote() {
  if (hasUnsaved.value && (noteTitle.value || noteContent.value)) saveNote()
  noteTitle.value = ''
  noteContent.value = ''
  currentNoteId.value = null
  hasUnsaved.value = false
}

function loadNote(note: Note) {
  if (hasUnsaved.value && currentNoteId.value && (noteTitle.value || noteContent.value)) saveNote()
  noteTitle.value = note.title
  noteContent.value = note.content
  currentNoteId.value = note.id
  hasUnsaved.value = false
}

function deleteNote(id: number) {
  if (!userStore.isLoggedIn) return
  const idx = notes.value.findIndex(n => n.id === id)
  if (idx === -1) return
  notes.value.splice(idx, 1)
  localStorage.setItem(getStorageKey(), JSON.stringify(notes.value))
  if (currentNoteId.value === id) {
    if (notes.value.length > 0) { loadNote(notes.value[0]) }
    else { noteTitle.value = ''; noteContent.value = ''; currentNoteId.value = null }
  }
  showToast('笔记已删除')
}

function clearNote() {
  noteTitle.value = ''
  noteContent.value = ''
  hasUnsaved.value = true
  showToast('内容已清空')
}

function markUnsaved() { hasUnsaved.value = true }

function formatTime(ts: number) {
  if (!ts) return ''
  const diff = Date.now() - ts
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 604800000) return Math.floor(diff / 86400000) + '天前'
  const d = new Date(ts)
  return (d.getMonth() + 1) + '月' + d.getDate() + '日'
}

function showToast(text: string) {
  toastText.value = text
  toastShow.value = true
  setTimeout(() => { toastShow.value = false }, 2000)
}

onMounted(() => { loadNotes() })
</script>

<style lang="scss" scoped>
.page-body { padding: 20px; }

.note-page { display: flex; max-width: 1200px; margin: 0 auto; gap: 20px; min-height: calc(100vh - 250px); }

.note-sidebar { width: 280px; flex-shrink: 0; display: flex; flex-direction: column; background: #fff; border-radius: 12px; box-shadow: var(--shadow-sm); overflow: hidden; }
.sidebar-header { padding: 16px 18px; background: linear-gradient(135deg, var(--primary-red), #B83C2E); color: #fff; display: flex; align-items: center; justify-content: space-between; h3 { font-size: 15px; font-weight: 600; i { margin-right: 6px; } } }
.btn-new { padding: 6px 14px; background: rgba(255,255,255,0.2); color: #fff; border: 1px solid rgba(255,255,255,0.3); border-radius: 6px; font-size: 13px; cursor: pointer; transition: all 0.3s; &:hover { background: rgba(255,255,255,0.3); } }
.note-list { flex: 1; overflow-y: auto; padding: 8px; }
.note-item { padding: 12px 14px; border-radius: 8px; cursor: pointer; transition: all 0.2s; margin-bottom: 4px; position: relative; border: 1px solid transparent; &:hover { background: #faf5f0; border-color: rgba(155,43,37,0.1); } &.active { background: rgba(155,43,37,0.06); border-color: rgba(155,43,37,0.2); .note-item-title { color: var(--primary-red); } } }
.note-item-title { font-size: 14px; font-weight: 500; color: #333; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 4px; }
.note-item-preview { font-size: 12px; color: #999; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 4px; }
.note-item-time { font-size: 11px; color: #bbb; }
.note-item-delete { position: absolute; top: 50%; right: 10px; transform: translateY(-50%); width: 32px; height: 32px; border-radius: 50%; border: none; background: rgba(220,53,69,0.08); color: #e74c3c; cursor: pointer; display: flex; align-items: center; justify-content: center; font-size: 16px; transition: all 0.2s; &:hover { color: #fff; background: #DC3545; } }
.sidebar-empty { text-align: center; padding: 40px 20px; color: #ccc; i { font-size: 36px; display: block; margin-bottom: 10px; } p { font-size: 13px; } }
.sidebar-count { padding: 10px 18px; font-size: 12px; color: #999; border-top: 1px solid #f0f0f0; text-align: center; }

.note-main { flex: 1; display: flex; flex-direction: column; min-width: 0; }
.note-header { text-align: center; margin-bottom: 16px; color: var(--primary-red); font-size: 22px; font-weight: bold; }
.unsaved-dot { display: inline-block; width: 8px; height: 8px; background: var(--primary-gold); border-radius: 50%; margin-left: 6px; vertical-align: middle; }
.note-title { width: 100%; padding: 14px 18px; font-size: 18px; border: none; border-radius: 10px; margin-bottom: 12px; box-shadow: 0 2px 6px rgba(0,0,0,0.05); background: #fff; font-family: inherit; &:focus { outline: 2px solid rgba(155,43,37,0.3); } }
.note-editor { width: 100%; flex: 1; min-height: 500px; padding: 20px; font-size: 17px; line-height: 1.8; border: none; border-radius: 12px; resize: none; box-shadow: 0 3px 10px rgba(0,0,0,0.07); background: #fff; font-family: inherit; &:focus { outline: 2px solid rgba(155,43,37,0.3); } }
.note-buttons { display: flex; gap: 12px; margin-top: 16px; justify-content: center; }
.btn-save { padding: 12px 30px; background: var(--primary-red); color: white; border: none; border-radius: 8px; font-size: 15px; cursor: pointer; transition: all 0.3s; &:hover { background: #8B2020; } }
.btn-clear { padding: 12px 30px; background: #666; color: white; border: none; border-radius: 8px; font-size: 15px; cursor: pointer; transition: all 0.3s; &:hover { background: #555; } }

.toast { position: fixed; top: 30px; left: 50%; transform: translateX(-50%); background: #28a745; color: white; padding: 10px 25px; border-radius: 6px; font-size: 15px; z-index: 9999; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

@media (max-width: 768px) {
  .note-page { flex-direction: column; min-height: auto; }
  .note-sidebar { width: 100%; max-height: 200px; }
  .note-list { max-height: 120px; }
  .note-editor { min-height: 300px; }
}
</style>

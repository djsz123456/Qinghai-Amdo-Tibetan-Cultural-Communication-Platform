<template>
  <div class="keyboard-page">
    <div class="page-body">
      <h1>喜马拉雅藏文输入法 网页虚拟键盘</h1>
      <textarea ref="inputBoxRef" class="input-box" placeholder="点击下方按键输入藏文" v-model="inputText"></textarea>
      <div class="shift-status">当前状态：{{ isShiftActive ? 'Shift激活' : '正常（非Shift）' }}</div>

      <div class="keyboard">
        <div class="key-row">
          <button class="key" v-for="k in funcKeys" :key="k" @click="insertKey(k)">{{ k }}</button>
        </div>
        <div class="key-row">
          <button class="key" v-for="k in row1" :key="k" @click="insert(k)">{{ k }}</button>
          <button class="key wide" @click="backspace()">Backspace</button>
        </div>
        <div class="key-row">
          <button class="key wide" @click="insertKey('Tab')">Tab</button>
          <button class="key" v-for="k in row2" :key="k" @click="insert(k)">{{ k }}</button>
          <button class="key wide" @click="insertKey('Enter')">Enter</button>
        </div>
        <div class="key-row">
          <button class="key extra-wide" @click="toggleShift()">Caps Lock</button>
          <button class="key" v-for="k in row3" :key="k" @click="insert(k)">{{ k }}</button>
          <button class="key extra-wide" @click="insertKey('Enter')">Enter</button>
        </div>
        <div class="key-row">
          <button class="key extra-wide" @click="toggleShift()">Shift</button>
          <button class="key" v-for="k in row4" :key="k" @click="insert(k)">{{ k }}</button>
          <button class="key" @click="insertKey('↑')">↑</button>
          <button class="key" @click="insertKey('Fn')">Fn</button>
        </div>
        <div class="key-row">
          <button class="key" @click="insertKey('Fn')">Fn</button>
          <button class="key" @click="insertKey('Ctrl')">Ctrl</button>
          <button class="key" @click="insertKey('Win')">Win</button>
          <button class="key" @click="insertKey('Alt')">Alt</button>
          <button class="key space" @click="insert(' ')">空格</button>
          <button class="key" @click="insertKey('Alt')">Alt</button>
          <button class="key" @click="insertKey('Ctrl')">Ctrl</button>
          <button class="key" @click="insertKey('←')">←</button>
          <button class="key" @click="insertKey('↓')">↓</button>
          <button class="key" @click="insertKey('→')">→</button>
        </div>
      </div>

      <div class="copy-bar">
        <button class="btn-copy" @click="copyText"><i class="fa fa-copy"></i> 复制文本</button>
        <button class="btn-clear" @click="inputText = ''"><i class="fa fa-eraser"></i> 清空</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const inputBoxRef = ref<HTMLTextAreaElement | null>(null)
const inputText = ref('')
const isShiftActive = ref(false)

const funcKeys = ['Esc','F1','F2','F3','F4','F5','F6','F7','F8','F9','F10','F11','F12']
const row1 = ['ཨ','༡','༢','༣','༤','༥','༦','༧','༨','༩','༠','ས','ག']
const row2 = ['ཀ','ཤ','ད','ར','ཏ','ཡ','ུ','ི','ོ','པ','ཙ','ཚ']
const row3 = ['ཨ','ས','ད','ཕ','ག','ཧ','་','ཀ','བ','འ','ི']
const row4 = ['ཟ','ཞ','ཙ','བ','ང','ན','མ','ལ','ས','ཤ','ཚ']

function insert(char: string) {
  inputText.value += char
  inputBoxRef.value?.focus()
}

function insertKey(key: string) {
  switch (key) {
    case 'Enter': inputText.value += '\n'; break
    case 'Tab': inputText.value += '\t'; break
    default: break
  }
  inputBoxRef.value?.focus()
}

function backspace() {
  inputText.value = inputText.value.slice(0, -1)
  inputBoxRef.value?.focus()
}

function toggleShift() {
  isShiftActive.value = !isShiftActive.value
}

function copyText() {
  if (!inputText.value.trim()) return
  navigator.clipboard.writeText(inputText.value).then(() => {
    alert('已复制到剪贴板')
  })
}

function onKeydown(e: KeyboardEvent) { if (e.key === 'Shift') isShiftActive.value = true }
function onKeyup(e: KeyboardEvent) { if (e.key === 'Shift') isShiftActive.value = false }

onMounted(() => {
  document.addEventListener('keydown', onKeydown)
  document.addEventListener('keyup', onKeyup)
})
onUnmounted(() => {
  document.removeEventListener('keydown', onKeydown)
  document.removeEventListener('keyup', onKeyup)
})
</script>

<style lang="scss" scoped>
.page-body { padding: 30px; display: flex; flex-direction: column; align-items: center; }

h1 { color: var(--primary-red); margin-bottom: 20px; text-align: center; font-size: 1.5rem; }

.input-box { width: 90%; max-width: 1200px; height: 120px; font-size: 24px; padding: 15px; border: 2px solid #b97a46; border-radius: 8px; margin-bottom: 30px; resize: none; font-family: 'Noto Sans Tibetan', serif; &:focus { outline: 2px solid var(--primary-red); } }

.shift-status { margin-bottom: 10px; font-size: 16px; color: #666; }

.keyboard { background: #e0e0e0; padding: 20px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.2); }

.key-row { display: flex; gap: 8px; margin-bottom: 8px; justify-content: center; }

.key { background: #1a1a1a; color: #fff; border: none; border-radius: 6px; min-width: 50px; height: 50px; font-size: 20px; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: background 0.2s; &:hover { background: #333; } &:active { background: #555; } }
.key.wide { min-width: 80px; }
.key.extra-wide { min-width: 120px; }
.key.space { min-width: min(400px, 60vw); }

.copy-bar { display: flex; gap: 12px; margin-top: 20px; }
.btn-copy { padding: 10px 24px; background: var(--primary-red); color: #fff; border: none; border-radius: 8px; font-size: 15px; cursor: pointer; display: flex; align-items: center; gap: 6px; transition: all 0.2s; &:hover { background: #8B2020; } }
.btn-clear { padding: 10px 24px; background: #666; color: #fff; border: none; border-radius: 8px; font-size: 15px; cursor: pointer; display: flex; align-items: center; gap: 6px; transition: all 0.2s; &:hover { background: #555; } }

@media (max-width: 768px) {
  .keyboard { padding: 10px; }
  .key { min-width: 36px; height: 40px; font-size: 16px; }
  .key.wide { min-width: 60px; }
  .key.extra-wide { min-width: 80px; }
  .key.space { min-width: min(200px, 50vw); }
  .key-row { gap: 4px; margin-bottom: 4px; }
  .input-box { font-size: 18px; height: 80px; }
}
</style>

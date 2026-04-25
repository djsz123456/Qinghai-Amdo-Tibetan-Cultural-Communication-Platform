<template>
  <div class="phrase-page">
    <div class="page-header">
      <div class="main-title">安多藏语常用短语</div>
      <div class="subtitle-tibetan">ཨ་མདོའི་བོད་སྐད་ཀྱི་ཚིག་གྲུབ་སྤྱི་ཆེར་མཁོ་བ།</div>
    </div>

    <div class="container">
      <div class="search-bar">
        <input v-model="searchKeyword" type="text" class="search-input" placeholder="输入中文/拼音搜索常用短语..." />
      </div>

      <div class="category-bar">
        <div v-for="cat in categories" :key="cat.id" class="category-item" :class="{ active: currentCategory === cat.id }" @click="currentCategory = cat.id">
          {{ cat.name }}
        </div>
      </div>

      <div class="stats-bar">共 <span>{{ filteredPhrases.length }}</span> 条短语</div>

      <div class="phrase-list">
        <div v-for="(item, index) in filteredPhrases" :key="index" class="phrase-card">
          <div class="phrase-tag">{{ item.categoryLabel }}</div>
          <div class="tibetan-text">{{ item.tibetan }}</div>
          <div class="pinyin-text">{{ item.pinyin }}</div>
          <div class="chinese-text">{{ item.chinese }}</div>
          <div class="card-actions">
            <button class="btn-audio" @click="playAudio(item)">&#128266; 发音</button>
            <button class="btn-collect" :class="{ collected: item.collected }" @click="toggleCollect(item)">
              {{ item.collected ? '已收藏' : '收藏' }}
            </button>
          </div>
        </div>
        <div v-if="filteredPhrases.length === 0" class="empty-state">暂无匹配短语，请换个关键词试试</div>
      </div>

      <transition name="fade">
        <div v-if="toastShow" class="toast">{{ toastText }}</div>
      </transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue'

interface Phrase {
  tibetan: string
  pinyin: string
  chinese: string
  category: string
  categoryLabel: string
  collected: boolean
}

const categories = [
  { id: 'all', name: '全部' },
  { id: 'greeting', name: '问候礼仪' },
  { id: 'daily', name: '日常用语' },
  { id: 'travel', name: '旅游出行' },
  { id: 'food', name: '餐饮美食' },
  { id: 'shopping', name: '购物砍价' },
  { id: 'number', name: '数字时间' },
]

const currentCategory = ref('all')
const searchKeyword = ref('')
const toastShow = ref(false)
const toastText = ref('')

const phrases = reactive<Phrase[]>([
  { tibetan: 'བཀྲ་ཤིས་བདེ་ལེགས།', pinyin: 'Tashi Delek', chinese: '你好 / 吉祥如意', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'ཁྱེད་རང་བདེ་པོ་ཡིན་ནམ།', pinyin: 'Khyerang depoi yinnam?', chinese: '你好吗？', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'ང་བདེ་པོ་ཡིན།', pinyin: 'Nga depoi yin', chinese: '我很好', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'ཐུགས་རྗེ་ཆེ།', pinyin: 'Thugje che', chinese: '谢谢', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'གལ་ཆེ་མི་འདུག', pinyin: 'Galche midug', chinese: '不客气', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'གཞི་མི་འདུག', pinyin: 'Zhimi adug', chinese: '对不起 / 抱歉', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'ལགས་སོ།', pinyin: 'Lakso', chinese: '是的 / 好的', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'མིན།', pinyin: 'Min', chinese: '不是 / 不', category: 'greeting', categoryLabel: '问候礼仪', collected: false },
  { tibetan: 'ཁྱེད་རང་གི་མིང་ལ་ཅི་ཟེར།', pinyin: 'Khyerang gi ming la chi zer?', chinese: '你叫什么名字？', category: 'daily', categoryLabel: '日常用语', collected: false },
  { tibetan: 'ང་གི་མིང་ལ་... ཟེར།', pinyin: 'Nga gi ming la ... zer', chinese: '我叫……', category: 'daily', categoryLabel: '日常用语', collected: false },
  { tibetan: 'ཁྱེད་རང་གང་ནས་ཡིན།', pinyin: 'Khyerang gang ne yin?', chinese: '你是哪里人？', category: 'daily', categoryLabel: '日常用语', collected: false },
  { tibetan: 'ང་རྒྱ་གར་མི་ཡིན།', pinyin: 'Nga gyami yin', chinese: '我是中国人', category: 'daily', categoryLabel: '日常用语', collected: false },
  { tibetan: '... གང་དུ་འདུག', pinyin: '... gang du adug?', chinese: '……在哪里？', category: 'travel', categoryLabel: '旅游出行', collected: false },
  { tibetan: '... ལ་འགྲོ་ན་ཇི་ལྟར་འགྲོ།', pinyin: '... la dro na jitar dro?', chinese: '去……怎么走？', category: 'travel', categoryLabel: '旅游出行', collected: false },
  { tibetan: 'སྙིང་མཐའ་ལ་འགྲོ་གི་ཡིན།', pinyin: 'Nyingtha la drogi yin', chinese: '我要去湖边', category: 'travel', categoryLabel: '旅游出行', collected: false },
  { tibetan: 'འདིར་མཛེས་སྡུག་ཆེ།', pinyin: 'Adir dze dug che', chinese: '这里很美', category: 'travel', categoryLabel: '旅游出行', collected: false },
  { tibetan: 'བཏང་བ་ཡག་པོ་འདུག', pinyin: 'Tamba yakpo adug', chinese: '食物很好吃', category: 'food', categoryLabel: '餐饮美食', collected: false },
  { tibetan: 'བཞེས་རོགས་གནང།', pinyin: 'Zhe rok nang', chinese: '请吃饭', category: 'food', categoryLabel: '餐饮美食', collected: false },
  { tibetan: 'ཆང་གཅིག་ཁ་འཐུང།', pinyin: 'Chang chig kha thung', chinese: '喝一杯青稞酒', category: 'food', categoryLabel: '餐饮美食', collected: false },
  { tibetan: 'བོད་ཇ་ཞིམ་པོ་འདུག', pinyin: 'Böja zhimpo adug', chinese: '酥油茶真香', category: 'food', categoryLabel: '餐饮美食', collected: false },
  { tibetan: 'འདི་ག་ཚད་རེད།', pinyin: 'Adi gatshe re?', chinese: '这个多少钱？', category: 'shopping', categoryLabel: '购物砍价', collected: false },
  { tibetan: 'གོང་ཆུང་ཆུང་གནང་།', pinyin: 'Gong chung chung nang', chinese: '便宜一点', category: 'shopping', categoryLabel: '购物砍价', collected: false },
  { tibetan: 'ལེན་གི་ཡིན།', pinyin: 'Len gi yin', chinese: '我要买', category: 'shopping', categoryLabel: '购物砍价', collected: false },
  { tibetan: 'གཅིག', pinyin: 'Chig', chinese: '一（1）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'གཉིས', pinyin: 'Nyi', chinese: '二（2）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'གསུམ', pinyin: 'Sum', chinese: '三（3）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'བཞི', pinyin: 'Zhi', chinese: '四（4）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'ལྔ', pinyin: 'Nga', chinese: '五（5）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'དྲུག', pinyin: 'Drug', chinese: '六（6）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'བདུན', pinyin: 'Dün', chinese: '七（7）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'བརྒྱད', pinyin: 'Gye', chinese: '八（8）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'དགུ', pinyin: 'Gu', chinese: '九（9）', category: 'number', categoryLabel: '数字时间', collected: false },
  { tibetan: 'བཅུ', pinyin: 'Chu', chinese: '十（10）', category: 'number', categoryLabel: '数字时间', collected: false },
])

const filteredPhrases = computed(() => {
  return phrases.filter(item => {
    const matchCategory = currentCategory.value === 'all' || item.category === currentCategory.value
    const keyword = searchKeyword.value.trim()
    const matchSearch = !keyword ||
      item.chinese.includes(keyword) ||
      item.pinyin.toLowerCase().includes(keyword.toLowerCase()) ||
      item.tibetan.includes(keyword)
    return matchCategory && matchSearch
  })
})

function playAudio(item: Phrase) {
  if ('speechSynthesis' in window) {
    const utter = new SpeechSynthesisUtterance(item.chinese)
    utter.lang = 'zh-CN'
    utter.rate = 0.85
    window.speechSynthesis.speak(utter)
    showToast('正在播放中文发音...')
  } else {
    showToast('当前浏览器不支持语音播放')
  }
}

function toggleCollect(item: Phrase) {
  item.collected = !item.collected
  showToast(item.collected ? '已添加到收藏' : '已取消收藏')
}

function showToast(text: string) {
  toastText.value = text
  toastShow.value = true
  setTimeout(() => { toastShow.value = false }, 2000)
}
</script>

<style lang="scss" scoped>
.page-header {
  text-align: center; margin-bottom: 30px; padding: 30px 0 20px;
  border-bottom: 2px solid var(--primary-gold);
}
.main-title { font-size: 28px; color: var(--primary-red); font-weight: bold; }
.subtitle-tibetan { font-size: 20px; color: var(--primary-red); margin-top: 10px; letter-spacing: 2px; }

.container { max-width: 1000px; margin: 0 auto; padding: 20px; }

.search-bar { margin-bottom: 25px; text-align: center; }
.search-input { width: 80%; max-width: 500px; padding: 12px 20px; border: 2px solid #ddd; border-radius: 25px; font-size: 15px; outline: none; transition: border-color 0.3s; &:focus { border-color: var(--primary-red); } }

.category-bar { display: flex; gap: 12px; flex-wrap: wrap; justify-content: center; margin-bottom: 25px; }
.category-item { padding: 8px 20px; border: 1px solid #d0c8c0; border-radius: 20px; background: #fff; cursor: pointer; font-size: 15px; transition: all 0.3s; &.active { background: var(--primary-red); color: #fff; border-color: var(--primary-red); } }

.stats-bar { text-align: center; margin-bottom: 20px; color: #888; font-size: 14px; span { color: var(--primary-red); font-weight: bold; } }

.phrase-list { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
.phrase-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 3px 8px rgba(0,0,0,0.06); border-left: 4px solid var(--primary-red); transition: transform 0.2s, box-shadow 0.2s; &:hover { transform: translateY(-3px); box-shadow: 0 6px 16px rgba(155,43,37,0.12); } }
.phrase-tag { display: inline-block; font-size: 12px; padding: 2px 10px; border-radius: 10px; background: rgba(155,43,37,0.1); color: var(--primary-red); margin-bottom: 10px; font-weight: 600; }
.tibetan-text { font-size: 22px; line-height: 1.8; color: var(--primary-red); margin-bottom: 4px; font-family: "Noto Sans Tibetan", serif; }
.pinyin-text { font-size: 13px; color: #8B6914; margin-bottom: 6px; font-style: italic; }
.chinese-text { font-size: 16px; color: #444; margin-bottom: 14px; font-weight: 600; line-height: 1.6; }
.card-actions { display: flex; gap: 10px; justify-content: flex-end; }
.btn-audio { padding: 6px 14px; background: #f0e8e0; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; display: flex; align-items: center; gap: 4px; transition: background 0.2s; &:hover { background: #e0d0c8; } }
.btn-collect { padding: 6px 14px; background: #fff; border: 1px solid var(--primary-red); color: var(--primary-red); border-radius: 6px; cursor: pointer; font-size: 14px; transition: all 0.2s; &.collected { background: var(--primary-red); color: #fff; } }
.empty-state { text-align: center; padding: 60px 20px; color: #999; font-size: 16px; grid-column: 1 / -1; }

.toast { position: fixed; top: 30px; left: 50%; transform: translateX(-50%); background: #28a745; color: white; padding: 10px 25px; border-radius: 6px; font-size: 15px; z-index: 999; }
.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

@media (max-width: 768px) {
  .phrase-list { grid-template-columns: 1fr; }
  .main-title { font-size: 22px; }
}
</style>

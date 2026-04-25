<template>
  <div class="calendar-page">
    <div class="page-header">
      <h1>百年藏历</h1>
      <p>公历藏历对照查询 · 藏历节日 · 佛教殊胜日</p>
    </div>

    <div class="container">
      <div class="info-card">
        <div class="card-icon-large"><i class="fa fa-calendar"></i></div>
        <h2>藏历查询工具</h2>
        <p>提供1951年-2051年公历藏历对照查询，包含藏历节日、佛教殊胜日、日月食信息等详细信息。</p>
        <a href="/zangli-master/calendar.html" class="launch-btn">
          <i class="fa fa-external-link"></i> 打开完整藏历查询
        </a>
      </div>

      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon"><i class="fa fa-search"></i></div>
          <h3>公历藏历对照</h3>
          <p>查询任意日期的藏历信息，包括藏历年月日、天干地支等。</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon"><i class="fa fa-star"></i></div>
          <h3>藏历节日</h3>
          <p>萨嘎达瓦节、雪顿节、藏历新年等重要节日日期查询。</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon"><i class="fa fa-moon-o"></i></div>
          <h3>佛教殊胜日</h3>
          <p>莲师荟供日、神变节、释迦牟尼佛节日等殊胜日查询。</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon"><i class="fa fa-sun-o"></i></div>
          <h3>日月食信息</h3>
          <p>日食、月食时间查询，藏传佛教中日月食具有特殊宗教意义。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
.page-header {
  background: linear-gradient(135deg, #8B4513 0%, #CD853F 100%);
  color: #fff; text-align: center; padding: 48px 20px 40px;
  h1 { font-size: 2.5rem; font-weight: 700; margin-bottom: 10px; }
  p { font-size: 15px; opacity: 0.85; }
}

.container { max-width: 900px; margin: 0 auto; padding: 40px 20px; }

.info-card {
  text-align: center; background: var(--bg-secondary); border-radius: var(--radius-md);
  padding: 50px 30px; box-shadow: var(--shadow-md); margin-bottom: 40px;
  h2 { font-size: 1.5rem; color: #8B4513; margin: 20px 0 12px; }
  p { color: var(--text-secondary); margin-bottom: 24px; line-height: 1.8; }
}

.card-icon-large {
  width: 80px; height: 80px; border-radius: 50%; margin: 0 auto;
  background: linear-gradient(135deg, #8B4513, #CD853F);
  display: flex; align-items: center; justify-content: center;
  font-size: 32px; color: #fff;
}

.launch-btn {
  display: inline-flex; align-items: center; gap: 10px;
  padding: 14px 40px; background: linear-gradient(135deg, #8B4513, #A0522D);
  color: white; text-decoration: none; border-radius: 40px;
  font-size: 16px; font-weight: 600; box-shadow: 0 6px 20px rgba(139,69,19,0.35);
  transition: var(--transition);
  &:hover { transform: translateY(-3px); box-shadow: 0 8px 28px rgba(139,69,19,0.45); }
}

.features-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }

.feature-card {
  background: var(--bg-secondary); border-radius: var(--radius-md); padding: 28px;
  box-shadow: var(--shadow-sm); transition: var(--transition);
  &:hover { transform: translateY(-3px); box-shadow: var(--shadow-md); }
  h3 { font-size: 1rem; color: #8B4513; margin: 14px 0 8px; }
  p { font-size: 0.9rem; color: var(--text-secondary); line-height: 1.6; }
}

.feature-icon {
  width: 48px; height: 48px; border-radius: 50%;
  background: linear-gradient(135deg, #8B4513, #CD853F);
  display: flex; align-items: center; justify-content: center;
  font-size: 20px; color: #fff;
}

@media (max-width: 768px) {
  .features-grid { grid-template-columns: 1fr; }
  .page-header h1 { font-size: 2rem; }
}
</style>

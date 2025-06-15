// 提示信息仅在开发环境生效
import { createI18n } from 'vue-i18n'
import store from '@/store'
import zhCn from './json/zh-cn.json'
import en from './json/en.json'
import elementPlusZhCn from 'element-plus/dist/locale/zh-cn.mjs'
import elementPlusEn from 'element-plus/dist/locale/en.mjs'

const messages = {
  'zh-cn': {
    ...zhCn,
    el: elementPlusZhCn.el
  },
  'en': {
    ...en,
    el: elementPlusEn.el
  }
}

const lang = store.state.app.lang || navigator.language // 初次进入，采用浏览器当前设置的语言，默认采用中文
const locale = lang.indexOf('en') !== -1 ? 'en' : 'zh-cn'

/** 国际化主函数，调用vue-i18n插件生成 */
const i18n = createI18n({
  legacy: false,
  locale: locale,
  fallbackLocale: 'zh-cn',
  messages,
  silentTranslationWarn: true, // 关闭翻译警告
  silentFallbackWarn: true, // 关闭回退警告
  missingWarn: false, // 关闭缺失警告
  fallbackWarn: false // 关闭回退警告
})

// 设置 Element Plus 的语言
document.getElementsByTagName('html')[0].setAttribute('lang', locale)

export default i18n
import Vue from 'vue';
import App from './App';
import store from './store';

// 引入全局样式
import './static/styles/global.scss';

Vue.config.productionTip = false;

// 挂载Vuex
Vue.prototype.$store = store;

App.mpType = 'app';

const app = new Vue({
  ...App,
  store
});
app.$mount(); 
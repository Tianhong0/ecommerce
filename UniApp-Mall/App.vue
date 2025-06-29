<script>
export default {
  onLaunch: function() {
    console.log('App Launch');
    // 检查登录状态
    this.checkLogin();
  },
  onShow: function() {
    console.log('App Show');
  },
  onHide: function() {
    console.log('App Hide');
  },
  methods: {
    // 检查登录状态
    checkLogin() {
      const token = uni.getStorageSync('token');
      if (token) {
        // 已登录，获取用户信息
        this.$store.dispatch('getUserInfo');
      }
    }
  }
};
</script>

<style lang="scss">
/* 引入全局样式 */
@import './static/styles/global.scss';
</style> 
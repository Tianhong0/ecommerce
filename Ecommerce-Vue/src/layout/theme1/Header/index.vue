<template>
  <header>
    <div class="left-box">
      <!-- 收缩按钮 -->
      <div class="menu-icon" @click="opendStateChange">
        <i class="sfont head-fold" :class="isCollapse ? 'system-s-unfold' : 'system-s-fold'"></i>
      </div>
      <Breadcrumb />
    </div>
    <div class="right-box">
      <!-- 快捷功能按钮 -->
      <div class="function-list">
        <div class="function-list-item hidden-sm-and-down"><Full-screen /></div>
        <div class="function-list-item"><Word /></div>
        <div class="function-list-item"><SizeChange /></div>
        <div class="function-list-item hidden-sm-and-down"><Theme /></div>
      </div>
      <!-- 用户信息 -->
      <div class="user-info">
        <el-dropdown>
          <span class="el-dropdown-link">
            <el-avatar 
              size="small" 
              :src="$store.state.user.avatar" 
              v-if="$store.state.user.avatar"
            >
              <img src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"/>
            </el-avatar>
            {{ $store.state.user.nickname || $store.state.user.name || t('message.system.user') }}
            <i class="sfont system-xiala"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToUserProfile">{{ t('message.system.userProfile') || '个人信息' }}</el-dropdown-item>
              <el-dropdown-item @click="showPasswordLayer">{{ t('message.system.changePassword') }}</el-dropdown-item>
              <el-dropdown-item @click="loginOut">{{ t('message.system.loginOut') }}</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <password-layer :layer="layer" v-if="layer.show" />
    </div>
  </header>
</template>

<script lang="ts">
import { defineComponent, computed, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import FullScreen from '../../components/functionList/fullscreen.vue'
import Word from '../../components/functionList/word.vue'
import SizeChange from '../../components/functionList/sizeChange.vue'
import Theme from '../../components/functionList/theme.vue'
import Breadcrumb from './Breadcrumb.vue'
import PasswordLayer from '../../components/passwordLayer.vue'
export default defineComponent({
  components: {
    FullScreen,
    Breadcrumb,
    Word,
    SizeChange,
    Theme,
    PasswordLayer
  },
  setup() {
    const store = useStore()
    const router = useRouter()
    const route = useRoute()
    const { t } = useI18n()
    const layer = reactive({
      show: false,
      showButton: true
    })
    const isCollapse = computed(() => store.state.app.isCollapse)
    // isCollapse change to hide/show the sidebar
    const opendStateChange = () => {
      store.commit('app/isCollapseChange', !isCollapse.value)
    }

    // login out the system
    const loginOut = () => {
      store.dispatch('user/loginOut')
    }
    
    const showPasswordLayer = () => {
      layer.show = true
    }
    
    const goToUserProfile = () => {
      router.push('/user/profile')
    }
    
    return {
      isCollapse,
      layer,
      opendStateChange,
      loginOut,
      showPasswordLayer,
      goToUserProfile,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
  header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 60px;
    background-color: var(--system-header-background);
    padding-right: 22px;
  }
  .left-box {
    height: 100%;
    display: flex;
    align-items: center;
    .menu-icon {
      width: 60px;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 25px;
      font-weight: 100;
      cursor: pointer;
      margin-right: 10px;
      &:hover {
        background-color: var(--system-header-item-hover-color);
      }
      i {
        color: var(--system-header-text-color);
      }
    }
  }
  .right-box {
    display: flex;
    justify-content: center;
    align-items: center;
    .function-list{
      display: flex;
      .function-list-item {
        width: 30px;
        display: flex;
        justify-content: center;
        align-items: center;
        :deep(i) {
          color: var(--system-header-text-color);
        }
      }
    }
    .user-info {
      margin-left: 20px;
      .el-dropdown-link {
        color: var(--system-header-breadcrumb-text-color);
        display: flex;
        align-items: center;
        cursor: pointer;
        
        .el-avatar {
          margin-right: 8px;
        }
      }
    }
  }
  .head-fold {
    font-size: 20px;
  }
</style>
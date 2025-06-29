<template>
  <div class="container">
    <div class="box">
      <div class="login-content-left">
        <img :src="loginLeftPng"/>
        <div class="login-content-left-mask">
          <div>{{ t(systemTitle) }}</div>
          <div>{{ t(systemSubTitle) }}</div>
        </div>
      </div>

      <div class="box-inner">
        <h1>{{ t(isLogin ? 'message.system.welcome' : 'message.system.register') }}</h1>
        
        <el-form v-if="isLogin" ref="loginFormRef" :model="loginForm" :rules="loginRules" class="form">
          <el-form-item prop="name">
            <el-input
                size="large"
                v-model="loginForm.name"
                :placeholder="t('message.system.userName')"
                type="text"
                maxlength="50"
            >
              <template #prepend>
                <i class="sfont system-xingmingyonghumingnicheng"></i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                size="large"
                v-model="loginForm.password"
                :type="passwordType"
                :placeholder="t('message.system.password')"
                name="password"
                maxlength="50"
            >
              <template #prepend>
                <i class="sfont system-mima"></i>
              </template>
              <template #append>
                <i class="sfont password-icon" :class="passwordType ? 'system-yanjing-guan': 'system-yanjing'"
                   @click="passwordTypeChange"></i>
              </template>
            </el-input>
          </el-form-item>

          <el-button type="primary" :loading="loading" @click="handleLogin" style="width: 100%;" size="default">
            {{ t('message.system.login') }}
          </el-button>
          
          <div class="form-footer">
            <el-button type="text" @click="switchForm(false)">
              {{ t('message.system.goToRegister') || '没有账号？去注册' }}
            </el-button>
          </div>
        </el-form>
        
        <el-form v-else ref="registerFormRef" :model="registerForm" :rules="registerRules" class="form">
          <el-form-item prop="username">
            <el-input
                size="large"
                v-model="registerForm.username"
                :placeholder="t('message.system.userName')"
                type="text"
                maxlength="50"
            >
              <template #prepend>
                <i class="sfont system-xingmingyonghumingnicheng"></i>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input
                size="large"
                v-model="registerForm.password"
                :type="passwordType"
                :placeholder="t('message.system.password')"
                maxlength="50"
            >
              <template #prepend>
                <i class="sfont system-mima"></i>
              </template>
              <template #append>
                <i class="sfont password-icon" :class="passwordType ? 'system-yanjing-guan': 'system-yanjing'"
                   @click="passwordTypeChange"></i>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item prop="nickname">
            <el-input
                size="large"
                v-model="registerForm.nickname"
                :placeholder="t('message.system.nickname') || '昵称'"
                type="text"
                maxlength="50"
            >
             
            </el-input>
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input
                size="large"
                v-model="registerForm.email"
                :placeholder="t('message.system.email') || '邮箱'"
                type="email"
                maxlength="50"
            >
          
            </el-input>
          </el-form-item>
          
          <el-form-item prop="phone">
            <el-input
                size="large"
                v-model="registerForm.phone"
                :placeholder="t('message.system.phone') || '手机号'"
                type="text"
                maxlength="11"
            >
          
            </el-input>
          </el-form-item>

          <el-button type="primary" :loading="loading" @click="handleRegister" style="width: 100%;" size="default">
            {{ t('message.system.register') || '注册' }}
          </el-button>
          
          <div class="form-footer">
            <el-button type="text" @click="switchForm(true)">
              {{ t('message.system.goToLogin') || '已有账号？去登录' }}
            </el-button>
          </div>
        </el-form>
        
        <div class="fixed-top-right">
          <select-lang/>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { systemTitle, systemSubTitle } from '@/config'
import { ref, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'
import selectLang from '@/layout/components/functionList/word.vue'
import loginLeftPng from '@/assets/login/left.jpg'
import type { FormInstance } from 'element-plus'
import { registerApi } from '@/api/user'

const store = useStore()
const router = useRouter()
const { t } = useI18n()
const loginFormRef = ref<FormInstance>()
const registerFormRef = ref<FormInstance>()
const loading = ref(false)
const passwordType = ref('password')
const isLogin = ref(true)

const loginForm = reactive({
  name: 'admin',
  password: '123456'
})

const registerForm = reactive({
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: ''
})

const loginRules = {
  name: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ]
}

const passwordTypeChange = () => {
  passwordType.value = passwordType.value === '' ? 'password' : ''
}

const switchForm = (login: boolean) => {
  isLogin.value = login
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const res = await store.dispatch('user/login', {
      username: loginForm.name,
      password: loginForm.password,
      client_id: 'web',
      client_secret: '123456'
    })
    
    if (res.success) {
      try {
        const { getAuthRoutes } = await import('@/router/permission')
        await getAuthRoutes()
        setTimeout(() => {
          router.push('/')
        }, 300)
      } catch (error) {
        console.error('路由加载失败:', error)
        router.push('/')
      }
    }
  } catch (error: any) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    // @ts-ignore 忽略类型检查
    const res = await registerApi(registerForm)
    
    if (res && res.success) {
      ElMessage.success(res.message || '注册成功')
      // 注册成功后切换到登录页
      switchForm(true)
      // 清空表单
      registerForm.username = ''
      registerForm.password = ''
      registerForm.nickname = ''
      registerForm.email = ''
      registerForm.phone = ''
    }
  } catch (error: any) {
    console.error('注册失败:', error)
    ElMessage.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.container {
  position: relative;
  width: 100vw;
  height: 100vh;
  background: #fff url('@/assets/login/bg.png') no-repeat center;
  overflow: hidden;
  background-size: cover;
  cursor: pointer;
  user-select: none;

  .box {
    width: 1160px;
    display: flex;
    position: absolute;
    left: 50%;
    top: 50%;
    background: white;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    height: 440px;
    overflow: hidden;
    box-shadow: 0 6px 20px 5px rgba(152, 152, 152, 0.1),
    0 16px 24px 2px rgba(117, 117, 117, 0.14);

    .login-content-left {
      position: relative;

      img {
        height: 440px;
      }

      .login-content-left-mask {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        background-image: linear-gradient(rgba(0,204,222,0.8), rgba(51,132,224,0.8));
        text-align: center;
        color: #fff;
        font-size: 1.8rem;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        letter-spacing: 2px;

        div:nth-child(1) {
          font-size: 3.5rem;
          margin-bottom: 1em;
        }
      }
    }

    .box-inner {
      width: 500px;
      overflow-y: auto;
      max-height: 440px;

      h1 {
        margin-top: 40px;
        text-align: center;
      }

      .form {
        width: 80%;
        margin: 30px auto 15px;

        .el-input {
          margin-bottom: 20px;
        }

        .password-icon {
          cursor: pointer;
          color: #409eff;
        }
        
        .form-footer {
          text-align: center;
          margin-top: 10px;
        }
      }

      .fixed-top-right {
        position: absolute;
        top: 10px;
        right: 10px;
      }
    }
  }
}

@media screen and (max-width: 1260px) {
  .login-content-left {
    display: none;
  }
  .box {
    width: 500px !important;
  }
}

@media screen and (max-width: 750px) {
  .container .box, .container .box-inner {
    width: 100vw !important;
    height: 100vh;
    box-shadow: none;
    left: 0;
    top: 0;
    transform: none;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    h1 {
      margin-top: 0;
    }
  }
}
</style>

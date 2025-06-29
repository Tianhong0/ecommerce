<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      
      <div class="avatar-container">
        <el-upload
          class="avatar-uploader"
          action="#"
          :show-file-list="false"
          :auto-upload="false"
          :on-change="handleAvatarChange"
          :before-upload="beforeAvatarUpload"
        >
          <img v-if="formData.avatar" :src="formData.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon" ><Plus /></el-icon>
        </el-upload>
        <div class="avatar-text">点击上传头像</div>
      </div>
      
      <el-form :model="formData" label-width="100px" class="profile-form">
        <el-form-item label="用户名">
          <el-input v-model="formData.username" disabled />
        </el-form-item>
        
        <el-form-item label="昵称">
          <el-input v-model="formData.nickname" :disabled="!isEditing" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" :disabled="!isEditing" />
        </el-form-item>
        
        <el-form-item label="手机号">
          <el-input v-model="formData.phone" :disabled="!isEditing" />
        </el-form-item>
        
        <el-form-item v-if="isEditing">
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </el-form-item>
        <el-form-item v-else>
          <el-button type="primary" @click="startEdit">编辑</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { updateCurrentUser } from '@/api/user'
import { uploadFile } from '@/api/upload'
import type { UploadFile } from 'element-plus'

const store = useStore()
const isEditing = ref(false)

// 表单数据
const formData = reactive({
  id: 0,
  username: '',
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

// 编辑时的临时数据
const tempFormData = reactive({
  nickname: '',
  email: '',
  phone: '',
  avatar: ''
})

// 获取最新的用户信息
onMounted(() => {
  store.dispatch('user/getCurrentUserInfo').then(() => {
    updateFormFromStore()
  })
})

// 监听store中的用户信息变化
watch(() => store.state.user, () => {
  updateFormFromStore()
}, { deep: true })

// 从store更新表单数据
const updateFormFromStore = () => {
  formData.id = store.state.user.id || 0
  formData.username = store.state.user.name || ''
  formData.nickname = store.state.user.nickname || ''
  formData.email = store.state.user.email || ''
  formData.phone = store.state.user.phone || ''
  formData.avatar = store.state.user.avatar || ''
}

// 开始编辑
const startEdit = () => {
  tempFormData.nickname = formData.nickname
  tempFormData.email = formData.email
  tempFormData.phone = formData.phone
  tempFormData.avatar = formData.avatar
  isEditing.value = true
}

// 取消编辑
const cancelEdit = () => {
  formData.nickname = tempFormData.nickname
  formData.email = tempFormData.email
  formData.phone = tempFormData.phone
  formData.avatar = tempFormData.avatar
  isEditing.value = false
}

// 头像上传前的验证
const beforeAvatarUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('头像必须是图片格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

// 头像变更处理
const handleAvatarChange = async (file: UploadFile) => {
  if (!file.raw) return
  try {
    const res = await uploadFile(file.raw)
    if (res && res.success) {
      formData.avatar = res.data
      ElMessage.success('头像上传成功')
      
      // 如果不在编辑状态，直接保存头像
      if (!isEditing.value) {
        await handleSave()
      }
    }
  } catch (error: any) {
    ElMessage.error(error.message || '头像上传失败')
  }
}

// 保存编辑
const handleSave = async () => {
  try {
    console.log('提交的数据:', {
      nickname: formData.nickname,
      email: formData.email,
      phone: formData.phone,
      avatar: formData.avatar
    })
    
    // 调用更新用户信息API
    const res = await updateCurrentUser({
      nickname: formData.nickname,
      email: formData.email,
      phone: formData.phone,
      avatar: formData.avatar
    })
    
    // 更新成功后重新获取用户信息
    await store.dispatch('user/getCurrentUserInfo')
    
    ElMessage.success('个人信息更新成功')
    isEditing.value = false
  } catch (error: any) {
    ElMessage.error(error.message || '更新失败')
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 20px;
  
  .profile-card {
    max-width: 600px;
    margin: 0 auto;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 18px;
      font-weight: bold;
    }
    
    .avatar-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-bottom: 20px;
      
      .avatar-uploader {
        width: 100px;
        height: 100px;
        border: 1px dashed #d9d9d9;
        border-radius: 50%;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: border-color 0.3s;
        
        &:hover {
          border-color: #409EFF;
        }
        
        .avatar {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        .avatar-uploader-icon {
        
          
          font-size: 55px;
          color: #8c939d;
          width: 100%;
          height: 100px;
       
        }
      }
      
      .avatar-text {
        margin-top: 10px;
        font-size: 14px;
        color: #606266;
      }
    }
    
    .profile-form {
      padding: 20px 0;
    }
  }
}
</style> 
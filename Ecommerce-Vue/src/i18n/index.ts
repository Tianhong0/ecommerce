import user from '@/store/modules/user'
import { createI18n } from 'vue-i18n'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import en from 'element-plus/dist/locale/en.mjs'

const messages = {
  'zh-cn': {
    message: {
      common: {
        search: '搜索',
        searchTip: '请输入关键词进行检索',
        add: '新增',
        update: '编辑',
        del: '删除',
        delBat: '批量删除',
        delTip: '确定删除选中的数据吗？',
        handle: '操作',
        exportExcel: '导出Excel',
        exportExcelTip: '请输入导出文件名',
        auth: '权限'
      },
      menu: {
        dashboard: {
          name: '首页',
          index: '首页'
        },
        menu: {
          name: '菜单',
          menu_1: '菜单1',
          menu_1_1: '菜单1-1',
          menu_1_1_1: '菜单1-1-1',
          menu_1_1_2: '菜单1-1-2',
          menu_1_2: '菜单1-2',
          menu_2: '菜单2',
          menu_3: '菜单3'
        },
        systemManage: {
          name: '系统管理',
          menu: '菜单管理',
          role: '角色管理',
          user: '用户管理'
        },
        product: {
          name: '商品管理',
          category: '分类管理',
          product: '商品列表'
        },
        community: {
          name: '社区',
          qq: 'QQ群',
          site: '网站'
        },
        system: {
          name: '系统',
          '404': '404',
          '401': '401',
          redirect: '重定向'
        }
      },
      system: {
        title: '电商后台管理系统',
        subTitle: '时间不在于你拥有多少,而在于你怎样使用。',
        welcome: '欢迎登录',
        login: '登录',
        userName: '用户名',
        password: '密码',
        contentScreen: '内容全屏',
        fullScreen: '全屏',
        fullScreenBack: '退出全屏',
        github: '访问github地址',
        changePassword: '修改密码',
        loginOut: '退出登录',
        user: '管理员',
        size: {
          default: '默认',
          large: '大',
          small: '小'
        },
        setting: {
          name: '系统设置',
          style: {
            name: '整体风格设置',
            default: '默认菜单风格',
            light: '亮色菜单风格',
            chinese: '中国水墨风',
            dark: '暗色菜单风格'
          },
          primaryColor: {
            name: '主题色',
            blue: '默认蓝',
            red: '玫瑰红',
            violet: '优雅紫',
            green: '故事绿',
            cyan: '明青',
            black: '极客黑'
          },
          other: {
            name: '其他设置',
            showLogo: '显示logo',
            showBreadcrumb: '显示面包屑导航',
            keepOnlyOneMenu: '保持一个菜单展开'
          }
        },
        tab: {
          reload: '重新加载',
          closeAll: '关闭所有标签',
          closeOther: '关闭其他标签',
          closeCurrent: '关闭当前标签'
        },
        goToRegister: '没有账号？去注册'
      },
      el: {
        pagination: {
          total: '共 {total} 条',
          goto: '前往',
          pageClassifier: '页'
        }
      }
    }
  },
  'en': {
    message: {
      common: {
        search: 'search',
        searchTip: 'please input keyword',
        add: 'add',
        update: 'update',
        del: 'delete',
        delBat: 'delete choose',
        delTip: 'Are you sure delete the selection data ?',
        handle: 'handle',
        exportExcel: 'export excel',
        exportExcelTip: 'please input file name'
      },
      menu: {
        dashboard: {
          name: 'Dashboard',
          index: 'Dashboard'
        },
        menu: {
          name: 'Menu',
          menu_1: 'Menu 1',
          menu_1_1: 'Menu 1-1',
          menu_1_1_1: 'Menu 1-1-1',
          menu_1_1_2: 'Menu 1-1-2',
          menu_1_2: 'Menu 1-2',
          menu_2: 'Menu 2',
          menu_3: 'Menu 3'
        },
        systemManage: {
          name: 'System',
          menu: 'Menu',
          role: 'Role',
          user: 'User'
        },
        product: {
          name: 'Product Management',
          category: 'Category Management',
          product: 'Product List'
        },
        community: {
          name: 'Community',
          qq: 'QQ Group',
          site: 'Website'
        },
        system: {
          name: 'System',
          '404': '404',
          '401': '401',
          redirect: 'Redirect'
        }
      },
      system: {
        title: 'Backend System',
        subTitle: 'Time is not about how much you have, but how you use it.',
        welcome: 'Welcome',
        login: 'Login',
        userName: 'Username',
        password: 'Password',
        contentScreen: 'Content Full Screen',
        fullScreen: 'Full Screen',
        fullScreenBack: 'Exit Full Screen',
        github: 'Visit Github',
        changePassword: 'Change Password',
        loginOut: 'Logout',
        user: 'Admin',
        size: {
          default: 'Default',
          large: 'Large',
          small: 'Small'
        },
        setting: {
          name: 'Settings',
          style: {
            name: 'Style Settings',
            default: 'Default Menu Style',
            light: 'Light Menu Style',
            chinese: 'Chinese Style',
            dark: 'Dark Menu Style'
          },
          primaryColor: {
            name: 'Primary Color',
            blue: 'Default Blue',
            red: 'Rose Red',
            violet: 'Grace Violet',
            green: 'Story Green',
            cyan: 'Cyan',
            black: 'Geek Black'
          },
          other: {
            name: 'Other Settings',
            showLogo: 'Show Logo',
            showBreadcrumb: 'Show Breadcrumb',
            keepOnlyOneMenu: 'Keep Only One Menu Open'
          }
        },
        tab: {
          reload: 'Reload',
          closeAll: 'Close All',
          closeOther: 'Close Others',
          closeCurrent: 'Close Current'
        }
      },
      el: {
        pagination: {
          total: 'Total {total} items',
          goto: 'Go to',
          pageClassifier: 'page'
        }
      }
    }
  }
}

const i18n = createI18n({
  legacy: false,
  locale: 'zh-cn',
  fallbackLocale: 'en',
  messages,
  silentTranslationWarn: true,
  silentFallbackWarn: true,
  missingWarn: false,
  fallbackWarn: false
})

export default i18n 
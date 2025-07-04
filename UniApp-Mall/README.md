# UniApp-Mall 电商平台移动端

基于 UniApp 开发的电商平台移动端应用，支持商品展示、分类浏览、购物车、下单支付等功能。

## 项目结构

```
UniApp-Mall/
├── api/                  # API接口
├── components/           # 公共组件
├── pages/                # 页面
│   ├── index/            # 首页
│   ├── category/         # 分类页
│   ├── cart/             # 购物车
│   ├── user/             # 个人中心
│   ├── login/            # 登录页
│   ├── product-detail/   # 商品详情
│   ├── order-confirm/    # 订单确认
│   └── order-list/       # 订单列表
├── static/               # 静态资源
│   ├── styles/           # 全局样式
│   ├── banner/           # 轮播图
│   ├── category/         # 分类图标
│   └── tabbar/           # 底部导航图标
├── store/                # Vuex状态管理
├── utils/                # 工具函数
├── App.vue               # 应用入口组件
├── main.js               # 应用入口JS
├── pages.json            # 页面配置
├── manifest.json         # 应用配置
└── package.json          # 项目依赖
```

## 功能模块

1. **首页**
   - 轮播图展示
   - 商品分类导航
   - 热门商品推荐
   - 新品上市

2. **商品分类**
   - 左侧分类菜单
   - 右侧商品列表
   - 分页加载

3. **商品详情**
   - 商品图片轮播
   - 商品信息展示
   - 商品规格选择
   - 加入购物车
   - 立即购买

4. **购物车**
   - 商品列表
   - 商品选择
   - 数量修改
   - 商品删除
   - 结算功能

5. **订单确认**
   - 收货地址选择
   - 商品清单
   - 订单备注
   - 订单提交

6. **订单列表**
   - 订单状态分类
   - 订单详情
   - 订单操作（取消、支付、确认收货）

7. **个人中心**
   - 用户信息展示
   - 订单状态快捷入口
   - 常用功能入口
   - 退出登录

## 技术栈

- UniApp
- Vue.js
- Vuex
- SCSS

## 后端接口

本项目使用已有的后端API接口，主要包括：

- 用户认证：登录、注册、获取用户信息
- 商品管理：商品列表、商品详情
- 分类管理：分类列表
- 订单管理：创建订单、订单列表、订单状态更新

## 运行项目

1. 安装依赖
```bash
npm install
```

2. 开发模式运行
```bash
npm run dev:h5         # H5版本
npm run dev:mp-weixin  # 微信小程序版本
```

3. 发布构建
```bash
npm run build:h5         # H5版本
npm run build:mp-weixin  # 微信小程序版本
```

## 注意事项

- 本项目使用的是模拟数据，实际使用时需要连接真实的后端API
- 支付功能为模拟实现，实际使用时需要对接真实的支付接口
- 用户登录使用管理后台的普通用户账号 
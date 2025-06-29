/**
 * 分类模拟数据
 */

export const categoryList = [
  { id: 1, name: "手机数码", icon: "/static/category/digital.png", parentId: 0 },
  { id: 2, name: "服装鞋包", icon: "/static/category/clothing.png", parentId: 0 },
  { id: 3, name: "家用电器", icon: "/static/category/appliance.png", parentId: 0 },
  { id: 4, name: "美妆护肤", icon: "/static/category/beauty.png", parentId: 0 },
  { id: 5, name: "生鲜果蔬", icon: "/static/category/food.png", parentId: 0 },
  
  // 手机数码子分类
  { id: 101, name: "手机", icon: "/static/category/phone.png", parentId: 1 },
  { id: 102, name: "平板", icon: "/static/category/tablet.png", parentId: 1 },
  { id: 103, name: "笔记本", icon: "/static/category/laptop.png", parentId: 1 },
  { id: 104, name: "耳机", icon: "/static/category/headphone.png", parentId: 1 },
  { id: 105, name: "智能手表", icon: "/static/category/watch.png", parentId: 1 },
  
  // 服装鞋包子分类
  { id: 201, name: "女装", icon: "/static/category/women.png", parentId: 2 },
  { id: 202, name: "男装", icon: "/static/category/men.png", parentId: 2 },
  { id: 203, name: "鞋靴", icon: "/static/category/shoes.png", parentId: 2 },
  { id: 204, name: "箱包", icon: "/static/category/bag.png", parentId: 2 },
  { id: 205, name: "配饰", icon: "/static/category/accessory.png", parentId: 2 }
];

export const getCategoryById = (id) => {
  return categoryList.find(item => item.id === parseInt(id));
};

export const getSubCategories = (parentId) => {
  return categoryList.filter(item => item.parentId === parseInt(parentId));
}; 
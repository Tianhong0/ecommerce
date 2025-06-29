/**
 * 商品模拟数据
 */

export const productList = [
  { 
    id: 1, 
    name: "iPhone 13", 
    price: 5999, 
    originalPrice: 6999,
    imageUrl: "/static/product/phone1.png", 
    description: "苹果最新款手机，A15仿生芯片，超强性能",
    sales: 1000,
    stock: 500,
    categoryId: 1,
    images: [
      "/static/product/phone1.png",
      "/static/product/phone1-1.png",
      "/static/product/phone1-2.png"
    ],
    specs: [
      { name: "颜色", options: ["黑色", "白色", "蓝色"] },
      { name: "内存", options: ["128GB", "256GB", "512GB"] }
    ]
  },
  { 
    id: 2, 
    name: "华为 Mate 40", 
    price: 6999, 
    originalPrice: 7999,
    imageUrl: "/static/product/phone2.png", 
    description: "华为旗舰手机，麒麟9000芯片，强大性能",
    sales: 800,
    stock: 300,
    categoryId: 1,
    images: [
      "/static/product/phone2.png",
      "/static/product/phone2-1.png",
      "/static/product/phone2-2.png"
    ],
    specs: [
      { name: "颜色", options: ["黑色", "白色", "绿色"] },
      { name: "内存", options: ["128GB", "256GB", "512GB"] }
    ]
  },
  { 
    id: 3, 
    name: "小米 12", 
    price: 4999, 
    originalPrice: 5499,
    imageUrl: "/static/product/phone3.png", 
    description: "小米最新款手机，骁龙8 Gen 1处理器",
    sales: 1200,
    stock: 600,
    categoryId: 1,
    images: [
      "/static/product/phone3.png",
      "/static/product/phone3-1.png",
      "/static/product/phone3-2.png"
    ],
    specs: [
      { name: "颜色", options: ["黑色", "白色", "紫色"] },
      { name: "内存", options: ["128GB", "256GB"] }
    ]
  },
  { 
    id: 4, 
    name: "OPPO Find X5", 
    price: 5499, 
    originalPrice: 5999,
    imageUrl: "/static/product/phone4.png", 
    description: "OPPO旗舰手机，骁龙8 Gen 1处理器，哈苏影像",
    sales: 900,
    stock: 400,
    categoryId: 1,
    images: [
      "/static/product/phone4.png",
      "/static/product/phone4-1.png",
      "/static/product/phone4-2.png"
    ],
    specs: [
      { name: "颜色", options: ["黑色", "白色", "蓝色"] },
      { name: "内存", options: ["128GB", "256GB"] }
    ]
  }
];

export const getProductById = (id) => {
  return productList.find(item => item.id === parseInt(id));
}; 
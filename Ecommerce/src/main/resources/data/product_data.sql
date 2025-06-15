-- 插入商品数据
INSERT INTO product (name, description, category_id, price, stock, status, main_image, create_time, update_time) VALUES
('iPhone 15 Pro', 'Apple最新旗舰手机，搭载A17 Pro芯片', 1, 7999.00, 100, 1, 'https://example.com/images/iphone15pro.jpg', NOW(), NOW()),
('华为 Mate 60 Pro', '华为旗舰手机，搭载麒麟9000S芯片', 1, 6999.00, 100, 1, 'https://example.com/images/mate60pro.jpg', NOW(), NOW()),
('MacBook Pro 14', 'Apple专业级笔记本电脑，M3 Pro芯片', 2, 14999.00, 50, 1, 'https://example.com/images/macbookpro14.jpg', NOW(), NOW()),
('联想 ThinkPad X1', '商务办公笔记本电脑', 2, 9999.00, 50, 1, 'https://example.com/images/thinkpadx1.jpg', NOW(), NOW()),
('索尼 65英寸 OLED电视', '4K HDR OLED智能电视', 3, 12999.00, 30, 1, 'https://example.com/images/sony65tv.jpg', NOW(), NOW()),
('海尔 变频冰箱', '智能变频节能冰箱', 3, 4999.00, 40, 1, 'https://example.com/images/haierfridge.jpg', NOW(), NOW()),
('Nike Air Force 1', '经典运动鞋', 4, 899.00, 200, 1, 'https://example.com/images/nikeaf1.jpg', NOW(), NOW()),
('Adidas 运动套装', '休闲运动套装', 4, 599.00, 150, 1, 'https://example.com/images/adidassuit.jpg', NOW(), NOW()),
('SK-II 护肤套装', '高端护肤套装', 5, 2999.00, 80, 1, 'https://example.com/images/sk2set.jpg', NOW(), NOW()),
('雅诗兰黛 小棕瓶', '明星精华液', 5, 999.00, 100, 1, 'https://example.com/images/estee.jpg', NOW(), NOW()),
('三只松鼠 坚果礼盒', '混合坚果零食礼盒', 6, 199.00, 300, 1, 'https://example.com/images/sanzhisongshu.jpg', NOW(), NOW()),
('五粮液 52度', '浓香型白酒', 6, 1099.00, 100, 1, 'https://example.com/images/wuliangye.jpg', NOW(), NOW()),
('Kindle Paperwhite', '电子书阅读器', 7, 999.00, 50, 1, 'https://example.com/images/kindle.jpg', NOW(), NOW()),
('索尼 WH-1000XM5', '无线降噪耳机', 7, 2999.00, 50, 1, 'https://example.com/images/sonyxm5.jpg', NOW(), NOW()),
('Nike 跑步鞋', '专业跑步鞋', 8, 799.00, 100, 1, 'https://example.com/images/nikerun.jpg', NOW(), NOW()),
('李宁 运动套装', '专业运动套装', 8, 399.00, 150, 1, 'https://example.com/images/lining.jpg', NOW(), NOW()),
('宜家 沙发', '北欧风格三人沙发', 9, 2999.00, 20, 1, 'https://example.com/images/ikeasofa.jpg', NOW(), NOW()),
('小米 智能台灯', '护眼智能台灯', 9, 199.00, 100, 1, 'https://example.com/images/milight.jpg', NOW(), NOW()),
('飞利浦 电动牙刷', '声波电动牙刷', 10, 399.00, 200, 1, 'https://example.com/images/philips.jpg', NOW(), NOW()),
('乐高 积木玩具', '创意积木玩具', 10, 599.00, 100, 1, 'https://example.com/images/lego.jpg', NOW(), NOW());

-- 插入商品SKU数据
INSERT INTO product_sku (product_id, sku_code, name, attributes, price, stock, status, create_time, update_time) VALUES
(1, 'IP15P-001', 'iPhone 15 Pro 256GB 黑色', '{"color":"黑色","storage":"256GB"}', 7999.00, 50, 1, NOW(), NOW()),
(1, 'IP15P-002', 'iPhone 15 Pro 512GB 白色', '{"color":"白色","storage":"512GB"}', 8999.00, 50, 1, NOW(), NOW()),
(2, 'M60P-001', 'Mate 60 Pro 256GB 黑色', '{"color":"黑色","storage":"256GB"}', 6999.00, 50, 1, NOW(), NOW()),
(2, 'M60P-002', 'Mate 60 Pro 512GB 白色', '{"color":"白色","storage":"512GB"}', 7999.00, 50, 1, NOW(), NOW()),
(3, 'MBP14-001', 'MacBook Pro 14 M3 Pro 16GB 512GB', '{"processor":"M3 Pro","memory":"16GB","storage":"512GB"}', 14999.00, 25, 1, NOW(), NOW()),
(3, 'MBP14-002', 'MacBook Pro 14 M3 Pro 32GB 1TB', '{"processor":"M3 Pro","memory":"32GB","storage":"1TB"}', 17999.00, 25, 1, NOW(), NOW()),
(4, 'TPX1-001', 'ThinkPad X1 i7 16GB 512GB', '{"processor":"i7","memory":"16GB","storage":"512GB"}', 9999.00, 25, 1, NOW(), NOW()),
(4, 'TPX1-002', 'ThinkPad X1 i7 32GB 1TB', '{"processor":"i7","memory":"32GB","storage":"1TB"}', 11999.00, 25, 1, NOW(), NOW()); 
-- 初始数据

-- 默认用户（密码: 123456，BCrypt加密）
INSERT INTO users (username, password, nickname, email, gender, daily_calorie_goal) VALUES
('admin', '$2a$10$L1IpQYXgox70Nas3dMQMeOEcnnHq249ZKy.VMRu6FWqjMw2RpX7My', '管理员', 'admin@smartrecipe.com', 1, 2000);

-- 默认分类
INSERT INTO categories (name, description, sort_order) VALUES
('家常菜', '日常家庭烹饪菜品', 1),
('川菜', '四川风味菜品', 2),
('粤菜', '广东风味菜品', 3),
('早餐', '营养早餐', 4),
('汤品', '各类汤品', 5),
('甜品', '甜点饮品', 6);

-- 默认食材（每单位营养成分）
INSERT INTO ingredients (name, unit, calories_per_unit, protein_per_unit, fat_per_unit, carb_per_unit) VALUES
('鸡蛋', '个', 72, 6.3, 4.8, 0.8),
('猪肉', '克', 143, 20.3, 6.2, 0),
('牛肉', '克', 125, 20.2, 4.2, 0),
('鸡胸肉', '克', 118, 24.0, 1.9, 0),
('番茄', '个', 20, 0.9, 0.2, 4.0),
('土豆', '个', 77, 2.0, 0.2, 17.0),
('白菜', '克', 17, 1.5, 0.2, 3.0),
('豆腐', '块', 81, 8.1, 3.7, 4.2),
('大米', '克', 346, 7.4, 0.8, 77.9),
('面条', '克', 280, 8.3, 0.7, 61.0),
('胡萝卜', '根', 37, 1.0, 0.2, 8.8),
('洋葱', '个', 40, 1.1, 0.2, 9.0),
('蒜', '瓣', 4, 0.2, 0, 0.9),
('姜', '克', 41, 1.3, 0.6, 10.3),
('酱油', '勺', 10, 0.5, 0, 1.0),
('盐', '勺', 0, 0, 0, 0),
('糖', '勺', 16, 0, 0, 4.0),
('食用油', '勺', 90, 0, 10.0, 0),
('葱', '根', 3, 0.1, 0, 0.5),
('黄瓜', '根', 15, 0.7, 0.1, 2.9);

-- 默认菜谱
INSERT INTO recipes (user_id, category_id, title, description, cover_image, difficulty, cook_time, servings, calories, protein, fat, carbohydrate) VALUES
(1, 1, '番茄炒鸡蛋', '经典家常菜，酸甜可口，简单易做，几分钟就能上桌', '/images/recipes/tomato-egg.jpg', 1, 15, 2, 250, 12.5, 15.0, 18.0),
(1, 1, '红烧肉', '色泽红亮，肥而不腻，入口即化的经典家常菜', '/images/recipes/braised-pork.jpg', 3, 60, 4, 580, 28.0, 42.0, 15.0),
(1, 2, '麻婆豆腐', '麻辣鲜香的经典川菜，豆腐嫩滑，肉末酥香', '/images/recipes/mapo-tofu.jpg', 2, 20, 2, 220, 15.0, 12.0, 10.0),
(1, 3, '白切鸡', '原汁原味的粤菜经典，皮爽肉滑，蘸料食用', '/images/recipes/white-cut-chicken.jpg', 2, 30, 3, 320, 35.0, 18.0, 2.0),
(1, 4, '蛋炒饭', '简单快手的经典早餐，粒粒分明，蛋香四溢', '/images/recipes/egg-fried-rice.jpg', 1, 15, 1, 380, 10.0, 12.0, 55.0),
(1, 5, '番茄蛋花汤', '酸甜开胃的快手汤品，五分钟搞定，老少皆宜', '/images/recipes/tomato-egg-soup.jpg', 1, 10, 2, 120, 8.0, 5.0, 10.0),
(1, 1, '凉拌黄瓜', '清脆爽口的开胃凉菜，蒜香浓郁，夏日必备', '/images/recipes/cold-cucumber.jpg', 1, 5, 2, 60, 2.0, 5.0, 6.0),
(1, 1, '土豆炖牛肉', '软烂入味的炖菜，牛肉酥烂，土豆绵密，汤汁浓郁', '/images/recipes/potato-beef-stew.jpg', 2, 45, 3, 420, 30.0, 15.0, 25.0);

-- 菜谱食材关联
-- 番茄炒鸡蛋 (recipe_id=1)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(1, 1, 3, '个'),
(1, 5, 2, '个'),
(1, 18, 2, '勺'),
(1, 16, 1, '勺'),
(1, 17, 1, '勺'),
(1, 19, 2, '根');

-- 红烧肉 (recipe_id=2)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(2, 2, 500, '克'),
(2, 15, 3, '勺'),
(2, 17, 2, '勺'),
(2, 18, 1, '勺'),
(2, 14, 10, '克'),
(2, 13, 3, '瓣'),
(2, 19, 2, '根'),
(2, 16, 1, '勺');

-- 麻婆豆腐 (recipe_id=3)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(3, 8, 2, '块'),
(3, 3, 100, '克'),
(3, 18, 2, '勺'),
(3, 13, 3, '瓣'),
(3, 15, 1, '勺'),
(3, 16, 1, '勺'),
(3, 19, 2, '根');

-- 白切鸡 (recipe_id=4)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(4, 4, 500, '克'),
(4, 14, 20, '克'),
(4, 19, 3, '根'),
(4, 16, 1, '勺');

-- 蛋炒饭 (recipe_id=5)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(5, 9, 200, '克'),
(5, 1, 2, '个'),
(5, 18, 2, '勺'),
(5, 16, 1, '勺'),
(5, 19, 1, '根');

-- 番茄蛋花汤 (recipe_id=6)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(6, 5, 2, '个'),
(6, 1, 1, '个'),
(6, 18, 1, '勺'),
(6, 16, 1, '勺'),
(6, 19, 1, '根');

-- 凉拌黄瓜 (recipe_id=7)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(7, 20, 2, '根'),
(7, 13, 2, '瓣'),
(7, 15, 1, '勺'),
(7, 16, 1, '勺'),
(7, 17, 1, '勺'),
(7, 18, 1, '勺');

-- 土豆炖牛肉 (recipe_id=8)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, amount, unit) VALUES
(8, 3, 300, '克'),
(8, 6, 2, '个'),
(8, 11, 1, '根'),
(8, 15, 2, '勺'),
(8, 18, 2, '勺'),
(8, 14, 10, '克'),
(8, 13, 3, '瓣'),
(8, 16, 1, '勺');

-- 菜谱步骤
-- 番茄炒鸡蛋
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(1, 1, '鸡蛋打散，加少许盐搅匀；番茄洗净切块；葱切葱花备用'),
(1, 2, '热锅倒油，油温七成热时倒入蛋液，快速翻炒至凝固成块，盛出备用'),
(1, 3, '锅中留底油，放入番茄块翻炒，炒至番茄出汁变软'),
(1, 4, '加入少许糖和盐调味，翻炒均匀'),
(1, 5, '倒入炒好的鸡蛋，翻炒均匀，撒上葱花即可出锅');

-- 红烧肉
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(2, 1, '猪肉洗净切成3厘米见方的块，冷水下锅焯水去血沫，捞出沥干'),
(2, 2, '热锅放少许油，加入白糖小火炒糖色，炒至焦糖色起小泡'),
(2, 3, '放入焯好的猪肉翻炒，使每块肉均匀上色'),
(2, 4, '加入葱段、姜片、蒜瓣翻炒出香味'),
(2, 5, '加入酱油、料酒和适量热水，水量没过肉块，大火烧开'),
(2, 6, '转小火炖煮40分钟至肉酥烂，最后大火收汁至浓稠即可');

-- 麻婆豆腐
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(3, 1, '豆腐切成2厘米见方的块，放入加盐的开水中焯烫2分钟，捞出沥干'),
(3, 2, '牛肉切成肉末，蒜切末，葱切葱花备用'),
(3, 3, '热锅倒油，放入肉末炒至变色酥香'),
(3, 4, '加入蒜末和酱油翻炒出香味，加入小半碗水烧开'),
(3, 5, '放入豆腐块，小火煮3分钟入味，撒上葱花即可出锅');

-- 白切鸡
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(4, 1, '鸡胸肉洗净，放入锅中，加入冷水没过鸡肉'),
(4, 2, '加入姜片和葱段，大火烧开后撇去浮沫'),
(4, 3, '转最小火加盖煮15分钟，关火后焖10分钟'),
(4, 4, '取出鸡肉放入冰水中浸泡至凉透，沥干后切块装盘，蘸酱油食用');

-- 蛋炒饭
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(5, 1, '隔夜米饭拨散备用（新鲜米饭需放凉）；鸡蛋打散加少许盐；葱切葱花'),
(5, 2, '热锅倒油，油温高时倒入蛋液，快速炒散至凝固'),
(5, 3, '放入米饭大火翻炒，用锅铲将米饭拍散，使每粒米都裹上蛋液'),
(5, 4, '加盐调味，翻炒均匀，撒上葱花即可出锅');

-- 番茄蛋花汤
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(6, 1, '番茄洗净切小块；鸡蛋打散备用；葱切葱花'),
(6, 2, '锅中加水烧开，放入番茄块煮2分钟至软烂出汁'),
(6, 3, '将蛋液沿锅边缓缓倒入，待蛋花浮起后轻轻搅动'),
(6, 4, '加盐和少许食用油调味，撒上葱花即可');

-- 凉拌黄瓜
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(7, 1, '黄瓜洗净，用刀拍裂后切成小段；蒜剁成蒜末'),
(7, 2, '将黄瓜放入碗中，加盐腌制5分钟，倒掉多余水分'),
(7, 3, '加入蒜末、酱油、糖和食用油，拌匀即可食用');

-- 土豆炖牛肉
INSERT INTO recipe_steps (recipe_id, step_number, description) VALUES
(8, 1, '牛肉切块冷水下锅焯水，捞出洗净血沫；土豆去皮切块；胡萝卜切滚刀块'),
(8, 2, '热锅倒油，放入姜片、蒜瓣爆香'),
(8, 3, '放入牛肉块翻炒，加酱油上色'),
(8, 4, '加入足量热水，大火烧开后转小火炖30分钟'),
(8, 5, '加入土豆和胡萝卜块，继续炖15分钟至蔬菜软烂'),
(8, 6, '加盐调味，大火收汁至浓稠即可出锅');

-- 文章表
CREATE TABLE blog_article
(
    article_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    content       LONGTEXT     NOT NULL,
    summary       VARCHAR(500),
    cover_image   VARCHAR(255),
    status        CHAR(1)  DEFAULT '0' COMMENT '状态（0草稿 1发布 2私密）',
    is_top        CHAR(1)  DEFAULT '0' COMMENT '是否置顶（0否 1是）',
    is_comment    CHAR(1)  DEFAULT '1' COMMENT '是否允许评论（0否 1是）',
    category_id   BIGINT,
    user_id       BIGINT,
    view_count    INT      DEFAULT 0,
    like_count    INT      DEFAULT 0,
    comment_count INT      DEFAULT 0,
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY           idx_category_id (category_id),
    KEY           idx_user_id (user_id),
    KEY           idx_status (status)
) COMMENT = '文章表';

-- 分类表
CREATE TABLE blog_category
(
    category_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL,
    description   VARCHAR(200),
    parent_id     BIGINT   DEFAULT 0,
    order_num     INT      DEFAULT 0,
    status        CHAR(1)  DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by     VARCHAR(64),
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_category_name (category_name)
) COMMENT = '分类表';

-- 标签表
CREATE TABLE blog_tag
(
    tag_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name    VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    status      CHAR(1)  DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by   VARCHAR(64),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_tag_name (tag_name)
) COMMENT = '标签表';

-- 文章标签关联表
CREATE TABLE blog_article_tag
(
    article_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (article_id, tag_id)
) COMMENT = '文章标签关联表';

-- 评论表
CREATE TABLE blog_comment
(
    comment_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id  BIGINT NOT NULL,
    user_id     BIGINT,
    content     TEXT   NOT NULL,
    parent_id   BIGINT   DEFAULT 0 COMMENT '父评论ID',
    like_count  INT      DEFAULT 0,
    status      CHAR(1)  DEFAULT '0' COMMENT '状态（0正常 1审核 2删除）',
    ip_address  VARCHAR(45),
    user_agent  VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    KEY         idx_article_id (article_id),
    KEY         idx_parent_id (parent_id)
) COMMENT = '评论表';

-- 文章状态字典
INSERT INTO sys_dict_type(dict_name, dict_type, status, create_by, create_time, remark)
SELECT '博客文章状态', 'blog_article_status', '0', 'admin', sysdate(), '博客文章状态列表'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'blog_article_status');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1, '草稿', '0', 'blog_article_status', '', 'info', 'Y', '0', 'admin', sysdate(), '文章草稿'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'blog_article_status' AND dict_value = '0');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 2, '发布', '1', 'blog_article_status', '', 'success', 'N', '0', 'admin', sysdate(), '文章发布'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'blog_article_status' AND dict_value = '1');

INSERT INTO sys_dict_data(dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 3, '私密', '2', 'blog_article_status', '', 'warning', 'N', '0', 'admin', sysdate(), '文章私密'
WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'blog_article_status' AND dict_value = '2');

-- 博客管理菜单与权限
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '博客管理', 0, 5, 'blog', NULL, '', '', 1, 0, 'M', '0', '0', '', 'star', 'admin', sysdate(), '博客管理目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '博客管理' AND parent_id = 0);

SET @blog_menu_id := (SELECT menu_id FROM sys_menu WHERE menu_name = '博客管理' AND parent_id = 0 LIMIT 1);

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章管理', @blog_menu_id, 1, 'article', 'blog/article/index', '', 'Article', 1, 0, 'C', '0', '0', 'blog:article:list', 'edit', 'admin', sysdate(), '文章管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:article:list');

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类管理', @blog_menu_id, 2, 'category', 'blog/category/index', '', 'BlogCategory', 1, 0, 'C', '0', '0', 'blog:category:list', 'tree', 'admin', sysdate(), '分类管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:category:list');

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标签管理', @blog_menu_id, 3, 'tag', 'blog/tag/index', '', 'BlogTag', 1, 0, 'C', '0', '0', 'blog:tag:list', 'dict', 'admin', sysdate(), '标签管理菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:tag:list');

UPDATE sys_menu SET visible = '0', icon = 'star' WHERE menu_name = '博客管理' AND parent_id = 0;
UPDATE sys_menu SET visible = '0', icon = 'edit' WHERE perms = 'blog:article:list';
UPDATE sys_menu SET visible = '0', icon = 'tree' WHERE perms = 'blog:category:list';
UPDATE sys_menu SET visible = '0', icon = 'dict' WHERE perms = 'blog:tag:list';

SET @blog_article_menu_id := (SELECT menu_id FROM sys_menu WHERE perms = 'blog:article:list' LIMIT 1);
SET @blog_category_menu_id := (SELECT menu_id FROM sys_menu WHERE perms = 'blog:category:list' LIMIT 1);
SET @blog_tag_menu_id := (SELECT menu_id FROM sys_menu WHERE perms = 'blog:tag:list' LIMIT 1);

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章查询', @blog_article_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:article:query');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章新增', @blog_article_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:article:add');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章修改', @blog_article_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:article:edit');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章删除', @blog_article_menu_id, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:article:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:article:remove');

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类查询', @blog_category_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:category:query');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类新增', @blog_category_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:category:add');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类修改', @blog_category_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:category:edit');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类删除', @blog_category_menu_id, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:category:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:category:remove');

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标签查询', @blog_tag_menu_id, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:query', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:tag:query');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标签新增', @blog_tag_menu_id, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:add', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:tag:add');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标签修改', @blog_tag_menu_id, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:edit', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:tag:edit');
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '标签删除', @blog_tag_menu_id, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'blog:tag:remove', '#', 'admin', sysdate(), ''
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'blog:tag:remove');

-- AI 管家菜单与权限
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'AI 管家', 0, 6, 'ai', NULL, '', '', 1, 0, 'M', '0', '0', '', 'skill', 'admin', sysdate(), 'AI 管家目录'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = 'AI 管家' AND parent_id = 0);

SET @ai_menu_id := (SELECT menu_id FROM sys_menu WHERE menu_name = 'AI 管家' AND parent_id = 0 LIMIT 1);

INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '平台管家', @ai_menu_id, 1, 'steward', 'ai/steward/index', '', 'AiSteward', 1, 0, 'C', '0', '0', 'ai:steward:chat', 'skill', 'admin', sysdate(), '平台管理员 AI 管家'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'ai:steward:chat');

UPDATE sys_menu SET visible = '0', icon = 'skill' WHERE menu_name = 'AI 管家' AND parent_id = 0;
UPDATE sys_menu SET visible = '0', icon = 'skill' WHERE perms = 'ai:steward:chat';

-- 游客/前台文章展示菜单
INSERT INTO sys_menu(menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '文章展示', 0, 1, 'front', 'portal/index', '', 'PortalIndex', 1, 0, 'C', '0', '0', 'portal:article:list', 'documentation', 'admin', sysdate(), '游客文章展示菜单'
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'portal:article:list');

UPDATE sys_menu SET visible = '0', icon = 'documentation' WHERE perms = 'portal:article:list';

-- 游客角色：只绑定文章展示菜单
INSERT INTO sys_role(role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, remark)
SELECT '游客', 'visitor', 3, '1', 1, 1, '0', '0', 'admin', sysdate(), '仅可访问文章展示'
WHERE NOT EXISTS (SELECT 1 FROM sys_role WHERE role_key = 'visitor');

SET @visitor_role_id := (SELECT role_id FROM sys_role WHERE role_key = 'visitor' LIMIT 1);
SET @portal_menu_id := (SELECT menu_id FROM sys_menu WHERE perms = 'portal:article:list' LIMIT 1);

INSERT INTO sys_role_menu(role_id, menu_id)
SELECT @visitor_role_id, @portal_menu_id
WHERE @visitor_role_id IS NOT NULL
  AND @portal_menu_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM sys_role_menu WHERE role_id = @visitor_role_id AND menu_id = @portal_menu_id
  );

# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists init_db;

-- 切换库
use my_db;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    favourNum  int      default 0                 not null comment '收藏数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子' collate = utf8mb4_unicode_ci;

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';

insert into user (id, userAccount, userPassword, unionId, mpOpenId, userName, userAvatar, userProfile, userRole, createTime, updateTime, isDelete) values
                                                                                                                                                       (1, 'test1', 'password1', '', '', '', 'default.jpg', '简介1', 'user', '2023-03-15 10:00:00', '2023-03-15 10:00:00', 0),
                                                                                                                                                       (2, 'test2', 'password2', '', '', '小明', 'default.jpg', '简介2', 'admin', '2023-03-15 11:00:00', '2023-03-15 11:00:00', 0),
                                                                                                                                                       (3, 'test3', 'password3', 'unionId1', '', '张三', 'default.jpg', '简介3', 'user', '2023-03-15 12:00:00','2023-03-15 11:00:00' , 0),
                                                                                                                                                       (4, 'test4', 'password4', '', '', '', '', '', 'ban', '2023-03-16 14:00:00', '2023-03-15 11:00:00', 1),
                                                                                                                                                       (5, 'test5', 'password5', '', '', '', 'avatar.jpg', '', 'user', '2023-03-16 15:00:00','2023-03-15 11:00:00' , 1),
                                                                                                                                                       (6, 'test6', 'password6', '', '', '', '', '', 'admin', '2023-03-16 16:00:00','2023-03-15 11:00:00' , 1),
                                                                                                                                                       (7, 'test7', 'password7', '', '', '', '', '', 'admin', '2023-03-16 17:30:00', '2023-03-15 11:00:00', 1),
                                                                                                                                                       (8, 'test8', 'password8@test8', '', '', '', '/uploads/avatar/default.jpg', '', 'user', '2023-03-17 9:35:49','2023-03-15 11:00:00' , 1),
                                                                                                                                                       (9, 'test9@test9', '\$apr1\$Test9\$9d7a7f4b\$f4e7f7a88b6f4b4f6f6a9a7b78f4a9a8f9b6d7b5c/', '', '', '/uploads/avatar/default.jpg', '/uploads/profile/profile_test9.jpg', '', 'user', '2023-03-27 9:45:56', '2023-04-25 9:56:56', 1),
                                                                                                                                                       (10, 'test_user_new_role_test_role_new_role_test_role_new_role_new_role_new_role_new_role_new_role_new_role_new_role_role', '\$apr1\$Test_user_new_role\$f7b7d7d4\$8a4b9f4d4f588b56b9f4a8a8f7b7aafb96a6f6a6/', NULL, NULL, NULL, '/uploads/avatar/default.jpg', '/uploads/profile/profile_new_role.jpg', 'admin', '2023-04-25 9:56:56','2023-04-25 9:56:56' , 1);
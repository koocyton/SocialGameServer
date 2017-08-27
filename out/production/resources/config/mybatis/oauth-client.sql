DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(18) unsigned NOT NULL COMMENT '主 ID',
  `account` varchar(250) NOT NULL COMMENT '账号',
  `password` char(32) NOT NULL COMMENT '密码',
  `salt` varchar(32) NOT NULL COMMENT 'hash password 时参入这个字符串',
  `nickname` varchar(250) NOT NULL COMMENT '名称',
  `gender` tinyint(1) NOT NULL COMMENT '性别',
  `portrait` TEXT NOT NULL COMMENT '头像',
  `friends` TEXT NOT NULL COMMENT '好友的 ID',
  `created_at` int(10) NOT NULL COMMENT '用户注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user`
  (`id`, `account`, `password`, `salt`, `nickname`, `gender`, `portrait`, `friends`, `created_at`)
VALUES
  (864803922227892224,'koocyton@gmail.com','65b0adfe6fe854dfb8e7e1d9014d9312','cc3eea252176db5a1e73659e793163b9','koocyton',2,'','',1456565643);

create table zero.permission
(
	id int auto_increment comment '页面ID'
		primary key,
	name varchar(30) not null comment '页面名称',
	url varchar(80) not null comment '页面地址',
	pid int not null comment '父节点',
	type tinyint not null comment '类型：2、菜单 1、页面 0、子项',
	sort tinyint unsigned not null comment '排名',
	status bit not null comment '状态 1.启用 2.隐藏 3.禁用'
)
comment '权限表' charset=utf8;

create table zero.staff
(
	id int auto_increment comment '帐号ID'
		primary key,
	name varchar(16) not null comment '帐号名称',
	platform tinyint(3) not null comment '平台ID',
	alias varchar(16) not null comment '昵称',
	type tinyint unsigned null comment '类别',
	status bit not null comment '状态：1.启用 0.删除',
	salt char(8) not null comment '动态码',
	password char(32) not null comment '帐号密码',
	avatar varchar(80) not null comment '头像'
)
comment '员工表' charset=utf8;

create table zero.staff_permission
(
	userid int not null comment '帐号ID',
	permission int not null comment '权限ID',
	primary key (userid, permission)
)
comment '角色权限表' charset=utf8;


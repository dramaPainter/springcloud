use oa;

CREATE TABLE page (
	id     INT AUTO_INCREMENT COMMENT '页面ID' PRIMARY KEY,
	name   VARCHAR(60) NOT NULL COMMENT '页面名称',
	url    VARCHAR(80) NOT NULL COMMENT '页面地址',
	pId    INT         NOT NULL COMMENT '父节点',
	tId    INT         NOT NULL COMMENT '项目顶级ID',
	rank   INT         NOT NULL COMMENT '排名',
	type   TINYINT     NOT NULL COMMENT '类型 0.子项 1.菜单',
	listed TINYINT     NOT NULL COMMENT '是否菜单栏显示',
	status TINYINT     NOT NULL COMMENT '状态 1.启用 2.隐藏 3.禁用'
) COMMENT '权限列表';

CREATE TABLE staff (
	userid    int auto_increment comment '帐号ID' primary key,
	username  varchar(16)   not null comment '帐号名称',
	loginpass char(32)      not null comment '帐号密码',
	salt      char(8)       not null comment '动态码',
	nickname  varchar(16)   not null comment '昵称',
	headimage varchar(80)   not null comment '头像',
	status    tinyint       not null comment '状态：1.启用 2.冻结 3.删除'
) COMMENT '员工列表';

create table oa.staff_ip (
	userid int    not null comment '帐号ID',
	ip     bigint not null comment 'ip',
	primary key (userid, ip)
) COMMENT '员工绑定IP';

create table oa.staff_page (
	userid  int     not null comment '员工ID',
	pageid  int     not null comment '页面ID',
	fixdate int     not null comment '创建时间',
	status  tinyint not null comment '状态：1.启用 2.冻结 3.删除'
) COMMENT '员工绑定页面';



# 以下是ElasticSearch索引配置
/*

PUT oa-staff

POST /oa-staff/_mapping
{
  "properties": {
    "userid": {
      "type": "integer"
    },
    "username": {
      "type": "keyword"
    },
    "loginpass": {
      "type": "keyword"
    },
    "salt": {
      "type": "keyword"
    },
    "nickname": {
      "type": "keyword"
    },
    "headimage": {
      "type": "keyword"
    },
    "status": {
      "type": "keyword"
    },
    "ip": {
      "type": "keyword"
    },
    "permission": {
      "type": "keyword"
    }
  }
}

PUT oa-page

POST /oa-page/_mapping
{
  "properties": {
    "id": {
      "type": "integer"
    },
    "name": {
      "type": "keyword"
    },
    "url": {
      "type": "keyword"
    },
    "pId": {
      "type": "keyword"
    },
    "tId": {
      "type": "keyword"
    },
    "rank": {
      "type": "keyword"
    },
    "type": {
      "type": "keyword"
    },
    "listed": {
      "type": "keyword"
    },
    "status": {
      "type": "keyword"
    }
  }
}
*/
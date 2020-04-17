use oa;

CREATE TABLE page
(
    id     int         not null comment '页面ID' primary key,
    name   varchar(30) not null comment '页面名称',
    url    varchar(80) not null comment '页面地址',
    pid    int         not null comment '父节点',
    sort   int         not null comment '排名',
    type   tinyint     not null comment '类型 0.子项 1.菜单',
    status tinyint     not null comment '状态 1.启用 2.隐藏 3.禁用'
) COMMENT '权限列表';

CREATE TABLE staff
(
    userid    int auto_increment comment '帐号ID' primary key,
    username  varchar(16) not null comment '帐号名称',
    loginpass char(32)    not null comment '帐号密码',
    salt      char(8)     not null comment '动态码',
    nickname  varchar(16) not null comment '昵称',
    headimage varchar(80) not null comment '头像',
    status    tinyint     not null comment '状态：1.启用 2.冻结 3.删除'
) COMMENT '员工列表';

create table oa.staff_ip
(
    userid int    not null comment '帐号ID',
    ip     bigint not null comment 'ip',
    primary key (userid, ip)
) COMMENT '员工绑定IP';

create table oa.staff_page
(
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
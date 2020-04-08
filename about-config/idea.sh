# VM options
-Dlog.path=/output/springboot -Dlogging.config=classpath:logback-test.xml

#SSH无密码上传文件
ssh-keygen -t rsa -P ''
ssh-copy-id -i ~/.ssh/id_rsa.pub root@127.0.0.1

#实体类生成类器
select  group_concat('/**\n* ', COLUMN_COMMENT, '\n */\n', 'private ', case DATA_TYPE when 'decimal' then 'float'
        when 'varchar' then 'String' when 'char' then 'String' when 'date' then 'String'
        when 'tinyint' then 'byte' when 'bigint' then 'long' else DATA_TYPE end,
        ' ', COLUMN_NAME, ';' separator '\n\n')
from    information_schema.COLUMNS
where   TABLE_NAME = 'channel' and TABLE_SCHEMA = 'treasure'

#SVN证书失效问题
svn ls https://192.168.0.122/svn/web

#数据库Update原则：
能用一个索引尽量不要使用两个混合索引去更新，可以先根据索引查询出主键，再执行更新。

#Ubuntu修改时区
dpkg-reconfigure tzdata

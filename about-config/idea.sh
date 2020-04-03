# VM options
-Dlog.path=/output/springboot -Dlogging.config=classpath:logback-test.xml

SSH无密码上传文件
ssh-keygen -t rsa -P ''
ssh-copy-id -i ~/.ssh/id_rsa.pub root@127.0.0.1
实体类生成类器
select  group_concat('/**\n* ', COLUMN_COMMENT, '\n */\n', 'private ', case DATA_TYPE when 'decimal' then 'float'
        when 'varchar' then 'String' when 'char' then 'String' when 'date' then 'String'
        when 'tinyint' then 'byte' when 'bigint' then 'long' else DATA_TYPE end,
        ' ', COLUMN_NAME, ';' separator '\n\n')
from    information_schema.COLUMNS
where   TABLE_NAME = 'channel' and TABLE_SCHEMA = 'treasure'
SVN证书失效问题
svn ls https://192.168.0.122/svn/web
数据库Update原则：
能用一个索引尽量不要使用两个混合索引去更新，可以先根据索引查询出主键，再执行更新。
pk_id 主键索引   idx_userid 玩家索引  id_fixdate 时间索引
Update
Ubuntu修改时区
dpkg-reconfigure tzdata
地推税收占比算法
select round(sum(revenue_1) / sum(revenueTotal_1) * 100, 2), round(sum(recycle_1) / sum(revenueTotal_1) * 100, 2),
       round(sum(recycle_1) * 0.9 / sum(revenueTotal_1) * 100, 2),
       round(sum(case when buyuRecycle_1 > 400 then (buyuRecycle_1 - 400) * 0.5 + 400 else buyuRecycle_1 end +
                 case when recycle_1 - buyuRecycle_1 > 400 then (recycle_1 - buyuRecycle_1 - 400) * 0.5 + 400
                 else recycle_1 - buyuRecycle_1 end) / sum(revenueTotal_1) * 100, 2)
from (
SELECT b.id, a.revenue_1, a.recycle_1, a.revenueTotal_1, a.buyuRecycle_1
from stat.stat_channel a INNER JOIN treasure.channel b on a.channel = b.id
where   b.type = 3 and a.fixdate = '2019-06-25' and revenueTotal_1 > 0 and a.channel > 0

union all

SELECT a.pid, a.revenue_1, a.recycle_1, 0, a.buyuRecycle_1
from stat.stat_channel_sub a INNER JOIN treasure.channel b on a.pid = b.id
            inner join stat.stat_channel c on c.channel = a.userid and c.fixdate = a.fixdate
where   b.type = 3 and a.fixdate = '2019-06-25' and revenueTotal_1 > 0 and a.pid > 0
) x;


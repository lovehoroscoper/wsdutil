-- 名站管理

ALTER TABLE net_mingzhan MODIFY COLUMN starttime BIGINT(15)DEFAULT 946684800;

ALTER TABLE net_mingzhan MODIFY COLUMN endtime BIGINT(15)DEFAULT 2145916800;

-- 设置一个时间 结束时间为 2038-01-01
UPDATE net_mingzhan t
SET t.starttime = 946684800,
 t.endtime = 2145916800
WHERE
	t.starttime = 0
OR t.endtime = 0

;
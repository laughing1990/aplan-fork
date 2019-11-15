-- 3.4.3地方项目用地红线界址信息表
CREATE VIEW spgl_xmydhxjzxxb_view
AS
SELECT `file`.`SYS_FILE_ID` AS `DFSJZJ`, '000000' AS `XZQHDM`, `file`.`xmdm` AS `XMDM`, `file`.`SYS_FILE_ID` AS `JZWJXH`, `file`.`FILE_NAME` AS `JZWJMC`
	, `file`.`CMP` AS `JZWJSM`, `file`.`ejz` AS `JZWJFJ`, 1 AS `SJYXBS`, `file`.`CDT` AS `MODIFY_TIME`
FROM `sys_file` `file`
WHERE `file`.`iszip` = 'needzip';

-- 3.4.4项目前期意见信息表
CREATE VIEW spgl_xmqqyjxxb_view
AS
SELECT `hist`.`DBID_` AS `DFSJZJ`, '000000' AS `XZQHDM`, `info`.`CENTRAL_CODE` AS `XMDM`, `hist`.`DBID_` AS `QQYJSLBM`, `hist`.`ASSIGNEE_` AS `BLDWDM`
	, `hist`.`ASSIGNEE_NAME` AS `BLDWMC`, `step`.`CREATE_DATE` AS `FKSJ`, `step`.`CONTACTS` AS `BLR`, `hist`.`HANDLE_COMMENTS` AS `QQYJ`, `file`.`FILE_NAME` AS `FJMC`
	, `file`.`SYS_FILE_ID` AS `FJID`, `step`.`CREATE_DATE` AS `MODIFY_TIME`, 1 AS `SJYXBS`, '' AS `SJWXYY`
FROM `jbpm4_hist_task` `hist`
	JOIN `wf_bus_instance` `inst` ON `hist`.`EXECUTION_` = `inst`.`PROC_INST_ID`
	JOIN `xm_project_info` `info` ON `inst`.`MASTER_ENTITY_KEY` = `info`.`ID`
	JOIN `xm_proj_appr_step` `step` ON `hist`.`DBID_` = `step`.`TASK_INSTANCE_ID`
	LEFT JOIN `sys_file` `file` ON `hist`.`DBID_` = `file`.`MEMO1`
WHERE (`hist`.`STATE_` = 'completed')
	AND (`hist`.`IS_MAIN_TASK` = 0)
	AND (`hist`.`HANDLE_COMMENTS_TIME` >= '2019-07-07')
	AND (`hist`.`HANDLE_COMMENTS_TIME` <= '2020-08-08')
	AND (`info`.`CENTRAL_CODE` IS NOT NULL);

-- 3.7.1地方规划控制线信息表
CREATE VIEW spgl_dfghkzxxxb_view
AS
SELECT `file`.`SYS_FILE_ID` AS `DFSJZJ`, '000000' AS `XZQHDM`, `file`.`SYS_FILE_ID` AS `KZXWJXH`, `file`.`FILE_NAME` AS `KZXWJMC`, '2018-01-01' AS `KZXSYKSSJ`
	, '云浮控制线数据' AS `KZXWJSM`, `file`.`ejz` AS `KZXWJFJ`, `file`.`CDT` AS `MODIFY_TIME`, 1 AS `SJYXBS`
FROM `sys_file` `file`
WHERE `file`.`iszip` = 'kzxzip';
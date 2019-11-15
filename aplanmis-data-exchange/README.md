# 工程建设项目审批系统数据上传省平台

#### 介绍
本方案按照“工程建设项目审批管理系统数据共享交换标准”要求， 创建视图对数据进行转换后上传到省前置库。（不再使用kettle上传）

#### 使用说明

1. 在工改系统中执行SQL  
修改本工程中"sql/审批系统视图-mysql_4.0.sql"文件中行政区划代码‘000000’为对应城市编码,‘xx市’为对应城市名然后执行
*注意：各地上传需求和前置库结构可能不同，有出入的地方请自行修改*

2. 配置
application.properties配置
- 配置省通知接口地址
city.notice.open = true
city.notice.server-urls = 数据同步通知接口地址多个地址用‘,’分割
- 审批系统库
spring.datasource.aplanmis.jdbc-url=jdbc:mysql://127.0.0.1:3306/aplanmis_prd?characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.aplanmis.username=duj
spring.datasource.aplanmis.password=duj
- 省前置库
spring.datasource.province.jdbc-url=jdbc:mysql://127.0.0.1:3306/pre_xmjg_440700?characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.province.username=duj
spring.datasource.province.password=duj
- 配置redis（必须启动好redis）
spring.redis.host=localhost
spring.redis.port=6379

3. 可选配置
- 统计分析使用的前置库。（同时向另一个库上传数据）
aplanmis.data.exchange.analyse.open=false
spring.datasource.analyse.jdbc-url=jdbc:mysql://127.0.0.1:3306/pre_xmjg_000000?characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.analyse.username=duj
spring.datasource.analyse.password=duj
spring.datasource.analyse.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.analyse.type=com.zaxxer.hikari.HikariDataSource
- 上传多规三张表
(1)需要在多规的库创建三个视图（由多规部门提供）：spgl_xmydhxjzxxb_view,spgl_xmqqyjxxb_view,spgl_dfghkzxxxb_view
(2)需要上传多规三张表的配置改成true
aplanmis.data.exchange.upload-duogui=false
(3)配置多规数据库连接
spring.datasource.duogui.jdbc-url=jdbc:mysql://127.0.0.1:3306/aplanmis_prd?characterEncoding=utf-8&allowMultiQueries=true
spring.datasource.duogui.username=duj
spring.datasource.duogui.password=duj

###日常运维说明
1. 每次更新代码后"审批系统视图-mysql_4.0.sql"有变动的需要手动执行

### 其他说明  
1. 目前同步数据的表有9张： 
- 地方项目审批流程信息表：SPGL_DFXMSPLCXXB
- 地方项目审批流程阶段信息表：SPGL_DFXMSPLCJDXXB
- 地方项目审批流程阶段事项信息表：SPGL_DFXMSPLCJDSXXXB 
- 项目基本信息：SPGL_XMJBXXB
- 项目单位信息表：SPGL_XMDWXXB
- 项目审批事项办理信息表：SPGL_XMSPSXBLXXB
- 项目审批事项办理详细信息表：SPGL_XMSPSXBLXXXXB
- 项目审批事项批复文件信息表：SPGL_XMSPSXPFWJXXB
- 项目其他附件信息表：SPGL_XMQTFJXXB
- 地方项目用地红线界址信息表：SPGL_XMYDHXJZXXB（需要开启）
- 项目前期意见信息表：SPGL_XMQQYJXXB（需要开启）
- 地方规划控制线信息表：SPGL_DFGHKZXXXB（需要开启）

2. 支持单项申报数据的上传，支持辅线作为事项上传。

3. 定时5分钟上传一次数据。配置在BSC_JOB_TIMER表
定时表达式：0 0/5 * * * ?
*首次启动项目未报错也未上传数据，请重启。需要Liquibase初始化bsc_job_timer定时上传的数据*

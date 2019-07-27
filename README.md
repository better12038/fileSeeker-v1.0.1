#项目名称
+ 文件搜索助手（FileSeeker-v1.0.1）
# 项目改进版本
+ 将原版本的MySQL数据库改为嵌入式数据库H2
+ 为整个文件系统增加了监控，能实时监控到文件的增加、删除
+ 增加配置信息文件
#项目简述
+ FileSeeker是一款运行于Windows、Linux的文件搜索工具
#项目功能
+ 支持搜索文件的文件名称、文件类型
# 相关技术
+ JavaSE基础
+ 多线程
+ JDBC 
+ 嵌入式数据库H2
+ Apache Commons I/O库
# 使用方法
+ 下载程序的发布包
+ 解压程序发布包
+ [ 配置文件 ]
+ 启动程序
>java -jar fileSeeker-1.0.1.jar fileSeeker_config.properties
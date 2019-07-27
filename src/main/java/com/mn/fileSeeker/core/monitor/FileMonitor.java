package com.mn.fileSeeker.core.monitor;

import com.mn.fileSeeker.config.HandlerPath;

//文件监控接口
public interface FileMonitor {

    void start();//启动监控器

    //监控HandlerPath里面对应的目录
    void monitor(HandlerPath handlerPath);

    void stop();//停止监控器
}

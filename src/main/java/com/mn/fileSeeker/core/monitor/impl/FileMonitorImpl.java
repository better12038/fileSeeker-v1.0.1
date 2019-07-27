package com.mn.fileSeeker.core.monitor.impl;

import com.mn.fileSeeker.config.EverythingConfig;
import com.mn.fileSeeker.config.HandlerPath;
import com.mn.fileSeeker.core.common.FileConvertThing;
import com.mn.fileSeeker.core.dao.FileIndexDao;
import com.mn.fileSeeker.core.monitor.FileMonitor;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor {
    private final FileAlterationMonitor monitor;
    private final FileIndexDao fileIndexDao;

    public FileMonitorImpl(FileIndexDao fileIndexDao){
        this.monitor = new FileAlterationMonitor(EverythingConfig.getInstance().getInterval());
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public void start() {
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlerPath handlerPath) {
        Set<String>  includePath = handlerPath.getIncludePath();
        for (String path:includePath
             ) {
            FileAlterationObserver observer = new FileAlterationObserver(new File(path), new FileFilter() {
                //决定是否监听
                @Override
                public boolean accept(File pathname) {
                    for (String exclude: handlerPath.getExcludePath()) {
                        if(pathname.getAbsolutePath().startsWith(exclude)){
                            return false;
                        }
                    }
                    return true;
                }
            });
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("onFileCreate:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("onFileDelete:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));

    }

    @Override
    public void stop() {
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

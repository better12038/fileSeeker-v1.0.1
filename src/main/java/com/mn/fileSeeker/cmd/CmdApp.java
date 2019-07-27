package com.mn.fileSeeker.cmd;

import com.mn.fileSeeker.config.EverythingConfig;
import com.mn.fileSeeker.core.EverythingManager;
import com.mn.fileSeeker.core.model.Condition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class CmdApp {
    public static void main(String[] args) {
        if(args.length >= 1){
            String configFile = args[0];//配置文件名
            Properties p = new Properties();
            try {
                p.load(new FileInputStream(configFile));
                //提取文件中的值，赋给EverythingConfig对象
                everythingConfigInit(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EverythingManager manager = EverythingManager.getInstance();

        manager.monitor();

        Scanner scanner = new Scanner(System.in);
        System.out.println("欢迎使用 **搜索助手(fileSeeker)**");

        while (true) {
            System.out.print(">>");
            String commend = scanner.nextLine();
            switch (commend){
                case "help": {
                    manager.help();
                    break;
                }
                case "quit":{
                    manager.quit();
                    break;
                }
                case "index":{
                    manager.buildIndex();
                    break;
                }
                default:{
                    if(commend.startsWith("search")){
                        String[] segments = commend.split(" ");

                        if(segments.length >= 2){
                            String name = segments[1];
                            Condition condition = new Condition();
                            condition.setName(name);
                            if(segments.length >=3){
                                String type = segments[2];

                                condition.setFileType(type.toUpperCase());
                            }
                            manager.search(condition).forEach(thing -> {

                                System.out.println(thing.getPath());
                            });
                        }else{
                            manager.help();
                        }

                    }else{
                        manager.help();
                    }
                }
            }
        }

    }

    private static void everythingConfigInit(Properties p){
        EverythingConfig config = EverythingConfig.getInstance();
        String maxReturn = p.getProperty("fileSeeker.max_return");
        String interval = p.getProperty("fileSeeker.interval");
        try{
            if(maxReturn != null){
                config.setMaxReturn(Integer.parseInt(maxReturn));
            }
            if(interval != null){
                config.setInterval(Integer.parseInt(interval));
            }
        }catch (NumberFormatException e){

        }

        String enableBuildIndex = p.getProperty("fileSeeker.enable_build_index");
        config.setEnableBuildIndex(Boolean.parseBoolean(enableBuildIndex));

        String orderByDesc = p.getProperty("fileSeeker.order_by_desc");
        config.setOrderByDesc(Boolean.parseBoolean(orderByDesc));

        String includePath = p.getProperty("fileSeeker.handle_path.include_path");
        if(includePath != null){
            String[] paths = includePath.split(";");
            //防止用户配了一个空字符串
            if(paths.length >0){
                //清理掉已经有的默认值
                config.getHandlerPath().getIncludePath().clear();
                for(String path: paths){
                    config.getHandlerPath().addIncludePath(path);
                }
            }
        }

        String excludePath = p.getProperty("fileSeeker.handle_path.exclude_path");
        if(excludePath != null){
            String[] paths = excludePath.split(";");
            //防止用户配了一个空字符串
            if(paths.length >0){
                //清理掉已经有的默认值
                config.getHandlerPath().getExcludePath().clear();
                for(String path: paths){
                    config.getHandlerPath().addExcludePath(path);
                }
            }
        }
        System.out.println(config);
    }
}

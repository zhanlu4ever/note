package com.cbooy;

/**
 * Created by chenhao on 2016/6/29.
 */
public class VMShutdownHook {

    /**
     * 一般用于在关闭时回调来做一些资源处理类的操作
     * @param server
     */
    public static void addHock(final TServer server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("关闭啦///");
                server.stop();
            }
        }));
    }
}

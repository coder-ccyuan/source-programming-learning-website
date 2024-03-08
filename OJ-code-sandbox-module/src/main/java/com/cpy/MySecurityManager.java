package com.cpy;

/**
 * @Author:成希德
 */
public class MySecurityManager extends SecurityManager {
//    @Override
//    public void checkRead(String file) {
//        System.out.println(file);
//        if (file.matches("jre\\\\lib\\\\ext")) return;
//        throw new RuntimeException("不可读");
//    }

    @Override
    public void checkDelete(String file) {
        throw new RuntimeException("不可删除");
    }

    @Override
    public void checkExec(String cmd) {
        throw new RuntimeException("不可启动");
    }

    @Override
    public void checkWrite(String file) {
        throw new RuntimeException("不可写");
    }
}

package com.cpy;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.cpy.model.ExecuteCodeRequest;
import com.cpy.model.ExecuteCodeResponse;
import com.cpy.utils.SandboxUtils;
import com.cpy.utils.response.compileCodeResponse;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author:成希德 原生java代码沙箱类
 */
@Service
public class NativeJavaSandbox implements CodeSandBox {
    public static String GLOBAL_CODE_DIR = "tmp";
    public static String GLOBAL_CODE_EXE_FILE = "Main.java";
    public static String MY_SECURITY_MANAGER="OJ-code-sandbox-module/src/main/resources/security";
    public static Long TIME_OUT = 10000L;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        String language = request.getLanguage();
        String code = request.getCode();
        List<String> input = request.getInput();
        String globalCodePathName = System.getProperty("user.dir") + File.separator + GLOBAL_CODE_DIR;
//        如果没有用户代码存储目录则新建
        if (!FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }
        //将用户代码存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_CODE_EXE_FILE;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        //编译代码
        try {
            compileCodeResponse compileCodeResponse = SandboxUtils.compileCode(userCodeFile.getAbsolutePath());
            if (!compileCodeResponse.isFlag()){
                executeCodeResponse.setMessage(compileCodeResponse.getMessage());
                return executeCodeResponse;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //执行代码
        String exeCmd = String.format("java -Xmx256m -cp %s;%s -Djava.security.manager=MySecurityManager Main", userCodeParentPath,MY_SECURITY_MANAGER);
//        String exeCmd = String.format("java -Xmx256m -cp %s Main", userCodeParentPath);
        System.out.println(exeCmd);
        try {
            SandboxUtils.runCode(input, executeCodeResponse,exeCmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            //删除代码
            if (userCodeFile.getParentFile() != null) {
                boolean del = FileUtil.del(userCodeParentPath);
                System.out.println("删除代码" + (del ? "成功" : "失败"));
            }
        }
        return executeCodeResponse;
    }
}

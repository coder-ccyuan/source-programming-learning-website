package com.cpy.utils;

import com.cpy.model.ExecuteCodeResponse;
import com.cpy.model.JudgeInfo;
import com.cpy.utils.response.compileCodeResponse;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import static com.cpy.NativeJavaSandbox.TIME_OUT;

/**
 * @Author:成希德 沙箱工具类
 */
public class SandboxUtils {
    public static compileCodeResponse compileCode(String userCodeFileAbsolutePath) throws InterruptedException, IOException {
        String compileCmd = String.format("javac %s -encoding utf-8", userCodeFileAbsolutePath);
        Process compileProcess = Runtime.getRuntime().exec(compileCmd);
        //等待程序执行获取返回码
        int reCode = compileProcess.waitFor();
        String readLine = "";
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(reCode);
        if (reCode == 0) {
            System.out.println("编译成功");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(readLine);
            }
            return new compileCodeResponse(stringBuilder.toString(), true);
        } else {
            System.out.println("编译失败");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()));
            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(readLine);
            }
        }
        System.out.println(stringBuilder);
        return new compileCodeResponse(stringBuilder.toString(), false);
    }

    /**
     * 运行代码返回工具类
     * executeCodeResponse自动填充
     *
     * @param input               输入用例
     * @param executeCodeResponse 响应
     * @param exeCmd              执行程序命令
     * @throws IOException
     * @throws InterruptedException
     */

    public static void runCode(List<String> input, ExecuteCodeResponse executeCodeResponse, String exeCmd) throws IOException, InterruptedException {
        //response about
        List<String> output = new LinkedList<>();
        List<Long> times = new LinkedList<>();
        List<Long> memories = new LinkedList<>();
        List<String> messages = new LinkedList<>();
        JudgeInfo judgeInfo = new JudgeInfo();

        //读取每一条数据
        for (String s : input) {
            StringBuilder stringBuilder = new StringBuilder();
            String readLine = "";
            //todo 监控程序内存
            //linux 环境下必须
            String[] cmd = new String[]{"sh","-c",exeCmd};
            Process exec = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(exec.getOutputStream()));
            //写入控制台
            bufferedWriter.write(s + "\n");
            //刷新（约等于回车，但没有在字符串后面+\n）
            bufferedWriter.flush();
            //监控程序
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            new Thread(() -> {
                try {
                    System.out.println("时间监控开始");
                    Thread.sleep(TIME_OUT);
                    System.out.println("程序中断,超时！！");
                    times.add(TIME_OUT);
                    //销毁程序
                    exec.destroy();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            int resultCode = exec.waitFor();
            if (resultCode != 0) {
                System.out.println("运行失败");
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readLine + "\n");
                }
                System.out.println(stringBuilder);
                messages.add("运行失败");
            } else {
                stopWatch.stop();
                System.out.println("运行成功");
                long time = stopWatch.getTotalTimeMillis();
                System.out.println(time + "ms");
                times.add(time);
                //读取程序输出数据
                while ((readLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(readLine);
                }
                System.out.println(stringBuilder);
                //填充数据
                output.add(stringBuilder.toString());
                messages.add("运行成功");
            }
            //关流，回收资源
            bufferedWriter.close();
            bufferedReader.close();
        }
        executeCodeResponse.setOutput(output);
        executeCodeResponse.setMessage("运行成功");
        judgeInfo.setTimes(times);
        judgeInfo.setMessages(messages);
        //todo 实现内存监控
        judgeInfo.setMemory(memories);
        executeCodeResponse.setJudgeInfo(judgeInfo);
    }
}

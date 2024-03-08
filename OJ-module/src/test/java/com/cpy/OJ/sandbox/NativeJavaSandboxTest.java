package com.cpy.OJ.sandbox;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeRequest;
import com.cpy.OJ.judge.codeSandBox.model.ExecuteCodeResponse;
import org.apache.coyote.Request;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author:成希德
 */
public class NativeJavaSandboxTest {
    @Test
    public void test(){
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode("import java.util.Scanner;\n" +
                "\n" +
                "public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner in = new Scanner(System.in);\n" +
                "//        int[] arr=new int[5];\n" +
                "        int sum=0;\n" +
                "        for(int i=0;i<5;i++){\n" +
                "            sum+=in.nextInt();\n" +
                "        }\n" +
                "        System.out.println(sum);\n" +
                "    }\n" +
                "}");
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInput(Arrays.asList("1 2 3 4 5","23 334 44 4 4"));
        String url="localhost:8091/sandbox/nativeJavaSandbox";
        String body = JSONUtil.toJsonStr(executeCodeRequest);
        String response = HttpRequest.post(url).body(body).execute().body();
        ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(response, ExecuteCodeResponse.class);
        System.out.println(executeCodeResponse);
    }
}

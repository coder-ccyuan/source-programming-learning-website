import com.cpy.NativeJavaSandbox;
import com.cpy.model.ExecuteCodeRequest;
import com.cpy.model.ExecuteCodeResponse;
import org.junit.Test;

import java.util.Arrays;

/**
 * @Author:成希德
 */
public class NativeJavaCodeSandboxTest {
    @Test
    public void test() {
        NativeJavaSandbox nativeJavaSandBox = new NativeJavaSandbox();
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
        ExecuteCodeResponse executeCodeResponse = nativeJavaSandBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);

    }
    @Test
    public void timeOutTest() {
        NativeJavaSandbox nativeJavaSandBox = new NativeJavaSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode("public class Main {\n" +
                "    public static void main(String[] args) {\n" +
                "        try {\n" +
                "            Thread.sleep(5000L);\n" +
                "        } catch (Exception e) {\n" +
                "            throw new RuntimeException();\n" +
                "        }\n" +
                "\n" +
                "    }\n" +
                "}");
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInput(Arrays.asList("1 2 3 4 5","23 334 44 4 4"));
        ExecuteCodeResponse executeCodeResponse = nativeJavaSandBox.executeCode(executeCodeRequest);

    }
    @Test
   public void pathTest(){
        System.out.println(System.getProperty("user.dir"));
    }
}

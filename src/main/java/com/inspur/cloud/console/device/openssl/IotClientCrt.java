package com.inspur.cloud.console.device.openssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class IotClientCrt {
    private static final Logger logger = LoggerFactory.getLogger(IotClientCrt.class);

    /**
     * 生成Client证书crt和私钥key
     *
     * @param clientId
     */
    public static void genIotClientCrt(String openSSLPath, String rootCACrtPath, String rootCAKeyPath,
                                       String clientOutFolder, String opensslcnfPath, String clientId, String rsa, String days, String pass) throws Exception {
        logger.debug("openSSLPath:" + openSSLPath + ";rootCACrtPath:" + rootCACrtPath + ";rootCAKeyPath:"
                + rootCAKeyPath + ";rootCAKeyPath:" + rootCAKeyPath + ";clientOutFolder:" + clientOutFolder
                + ";opensslcnfPath:" + opensslcnfPath + ";clientId:" + clientId);

        File file = new File(clientOutFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        Runtime runtime = Runtime.getRuntime();
        // 打开openSSL
        // 生成一个没有加密的ca私钥 - pem
        String cmd1 = "openssl genrsa -out " + clientOutFolder + "/" + clientId + ".key " + rsa;
        Process process;
        int pcode = 0;
        process = runtime.exec(cmd1);
        pcode = process.waitFor();
        // pcode=0 ,无错误，等于1说明有错误。
        if (pcode == 1) {
            throw new Exception(getErrorMessage(process));
        }
        // 生成ca对应的csr文件
        String cmd2 = "openssl req -out " + clientOutFolder + "/" + clientId + ".csr -key " + clientOutFolder + "/"
                + clientId + ".key -new -subj /C=CN/ST=Shandong/L=Jinan/O=Inspur/OU=Iot/CN=" + clientId;
        process = runtime.exec(cmd2);
        pcode = process.waitFor();
        // pcode=0 ,无错误，等于1说明有错误。
        if (pcode == 1) {
            throw new Exception(getErrorMessage(process));
        }
        // 执行命令:转换key文件到pem文件
        String cmd2_1 = "openssl pkcs8 -topk8 -in " + clientOutFolder + "/" + clientId + ".key -out " + clientOutFolder
                + "/" + clientId + ".pem" + " -nocrypt";
        process = runtime.exec(cmd2_1);
        pcode = process.waitFor();
        // pcode=0 ,无错误，等于1说明有错误。
        if (pcode == 1) {
            throw new Exception(getErrorMessage(process));
        }
        // 自签名
        String cmd3 = "openssl x509 -req -extfile " + opensslcnfPath + " -extensions v3_req -in " + clientOutFolder + "/"
                + clientId + ".csr -CA " + rootCACrtPath + " -CAkey " + rootCAKeyPath + " -CAcreateserial -out "
                + clientOutFolder + "/" + clientId + ".crt -days " + days + " -passin pass:" + pass;
        process = runtime.exec(cmd3);
        pcode = process.waitFor();
        // pcode=0 ,无错误，等于1说明有错误。
        if (pcode == 1) {
            throw new Exception(getErrorMessage(process));
        }
        // 删除无关文件
        {
            // deleteFile(clientOutFolder + "/" + clientId + ".csr");
            // deleteFile(clientOutFolder + "/" + clientId + ".pem");
        }
    }

    // 得到控制台输出的错误信息
    private static String getErrorMessage(Process process) {
        String errMeaage = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            errMeaage = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errMeaage;
    }

    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }


}

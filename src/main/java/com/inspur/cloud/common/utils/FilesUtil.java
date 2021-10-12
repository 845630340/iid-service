package com.inspur.cloud.common.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FilesUtil {

	public static void deleteFile(String pathFileName) {
		try {
			File tmpFile = new File(pathFileName);
			if (tmpFile.exists()) {
				tmpFile.delete();
			}
		} catch (Exception e) {
			// ignore
		}
	}

	/**
	 * 功能：Java读取txt文件的内容 步骤：
	 * 
	 * 1：先获得文件句柄
	 * 
	 * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 
	 * 3：读取到输入流后，需要读取生成字节流
	 * 
	 * 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 */
	public static String readTxtFile(String filePath) {
		InputStreamReader is = null;
		BufferedReader br = null;

		StringBuilder sb = new StringBuilder();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				is = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
				br = new BufferedReader(is);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					sb.append(lineTxt).append("\n");
				}
			} else {
				throw new RuntimeException("找不到指定的文件");
			}
		} catch (Exception e) {
			throw new RuntimeException("读取文件内容出错:" + filePath, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public static void writeTxtFile(String filePath, String fileContent) {

		OutputStream out = null;

		try {
			out = new FileOutputStream(new File(filePath));
			byte b[] = fileContent.getBytes();
			out.write(b);
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("写入文件内容出错:" + filePath, e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

	}

	// public static void main(String[] args) {
	//
	// String fileContent = readTxtFile("/sslkey/openSSL1.cfg");
	//
	// String[] arrIp = { "10.110.20.110", "10.110.20.111" };
	// String[] arrDns = { "10.110.20.110", "10.110.20.111" };
	// String fileContent2 = appendFileContext(fileContent, arrIp, arrDns);
	//
	// writeTxtFile("/sslkey/openSSLxxxxx.cfg", fileContent2);
	// }

	public static void zipFile(File inputFile, ZipOutputStream zipoutputStream) {
		try {
			if (inputFile.exists()) { // 判断文件是否存在
				if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹
					// 创建输入流读取文件
					try (FileInputStream fis = new FileInputStream(inputFile);
							BufferedInputStream bis = new BufferedInputStream(fis);) {
						// 将文件写入zip内，即将文件进行打包
						ZipEntry ze = new ZipEntry(inputFile.getName()); // 获取文件名
						zipoutputStream.putNextEntry(ze);

						// 写入文件的方法，同上
						byte[] b = new byte[1024];
						long l = 0;
						while (l < inputFile.length()) {
							int j = bis.read(b, 0, 1024);
							l += j;
							zipoutputStream.write(b, 0, j);
						}
					}

				} else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
					try {
						File[] files = inputFile.listFiles();
						for (int i = 0; i < files.length; i++) {
							zipFile(files[i], zipoutputStream);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createOpenSSLCnf(String pathFile, String fileCopy, String[] arrIp, String[] arrDns) {

		String fileContent = FilesUtil.readTxtFile(fileCopy);

		StringBuilder sb = new StringBuilder(fileContent);

		sb.append("[ v3_req ]\n");
		sb.append("\n");
		sb.append("##################lxf begin#############\n");
		sb.append("\n");
		sb.append("# Extensions to add to a certificate request\n");
		sb.append("\n");
		sb.append("basicConstraints = CA:FALSE\n");
		sb.append("keyUsage = nonRepudiation, digitalSignature, keyEncipherment\n");
		sb.append("\n");
		sb.append("subjectAltName = @alt_names\n");
		sb.append("\n");
		sb.append("#ext key usage = TLS Web Server Authentication, TLS Web Client Authentication\n");
		sb.append("[alt_names]\n");

		if (arrIp != null) {
			for (int i = 0; i < arrIp.length; i++) {
				sb.append("IP.").append(i + 1).append("=").append(arrIp[i]).append("\n");
			}
			sb.append("\n");
		}

		if (arrDns != null) {
			for (int i = 0; i < arrDns.length; i++) {
				sb.append("DNS.").append(i + 1).append("=").append(arrDns[i]).append("\n");
			}
			sb.append("\n");
		}

		sb.append("##################lxf end#############\n");

		FilesUtil.writeTxtFile(pathFile, sb.toString());
	}




}

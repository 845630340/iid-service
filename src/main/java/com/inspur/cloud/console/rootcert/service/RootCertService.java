package com.inspur.cloud.console.rootcert.service;

import com.github.pagehelper.PageHelper;
import com.inspur.bss.commonsdk.utils.IdWorker;
import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.common.exception.ExceptionCode;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.common.utils.JsonUtils;
import com.inspur.cloud.console.product.entity.DeviceDo;
import com.inspur.cloud.console.rootcert.constant.RootCertConstant;
import com.inspur.cloud.console.rootcert.dao.RootCertMapper;
import com.inspur.cloud.console.rootcert.entity.CreateRootCertParams;
import com.inspur.cloud.console.rootcert.entity.ImportRootCertParams;
import com.inspur.cloud.console.rootcert.entity.RootCert;
import com.inspur.cloud.console.rootcert.entity.Subject;
import com.inspur.cloud.console.rootcert.filetools.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: JiangYP
 * @Description:
 */

@Slf4j
@Service
public class RootCertService {

    private final RootCertMapper rootCertMapper;

    @Autowired
    public RootCertService(RootCertMapper rootCertMapper) {
        this.rootCertMapper = rootCertMapper;
    }


    /**
     * create .crt file and .key file by openssl command
     */
    public Boolean createRootCertAndKey(String certId, String rsaCount, String days, String password) {
        String folder = RootCertConstant.FOLDERPATH;
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            String cmdPre = String.format(RootCertConstant.ROOTCMDPRE, rsaCount, days);
            String cmdMed = "-keyout " + folder + certId + ".key -out " + folder + certId + ".crt ";
            String cmdBack = String.format(RootCertConstant.ROOTCMDBACK, password);
            String cmd = cmdPre + cmdMed + cmdBack;
            log.info("================ Create CA cert cmd: {}", cmd);
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            int res = process.waitFor();
            if (res == 1) {
                throw new Exception("Failed to create root cert and key.");
            }
            return true;
        } catch (Exception e) {
            log.error("============ 创建x509证书失败：", e);
        }
        return false;
    }


    /**
     * 1、创建根证书、根私钥临时文件
     * 2、填充RootCert，存入数据库
     * 3、删除临时文件
     */
    public void createCA(CreateRootCertParams createRootCertParams) {
        verifyIpAndDns(createRootCertParams.getIpArray(), createRootCertParams.getDnsArray());
        String certId = String.valueOf(IdWorker.getNextId());
        // create .crt file and .key file
        Boolean isCreate = createRootCertAndKey(certId, createRootCertParams.getRsaCount(), "3650", createRootCertParams.getPassword());
        AssertUtils.isTrue(isCreate, RootCertConstant.FAILED, "Error: Failed to create .crt and .key file.", HttpStatus.OK);

        String crtFileName = String.format(RootCertConstant.CRTFILE, certId);
        String keyFileName = String.format(RootCertConstant.KEYFILE, certId);
        // read from .crt file and .key file
        String crtFileContent = FileUtils.readToString(RootCertConstant.FOLDERPATH, crtFileName);
        String keyFileContent = FileUtils.readToString(RootCertConstant.FOLDERPATH, keyFileName);
        AssertUtils.isFalse(crtFileContent == null || keyFileContent == null, RootCertConstant.FAILED, "Error: Failed to read .crt and .key file.", HttpStatus.OK);

        // package RootCert Object and insert into db
        RootCert rootCert = packagePubParams(createRootCertParams);
        rootCert.setId(certId);
        rootCert.setMainPubEncKey(crtFileContent);
        rootCert.setMainPriEncKey(keyFileContent);
        rootCert.setDays("3650");
        log.info("=========== rootCert: {}", rootCert);
        rootCertMapper.insertRootCert(rootCert);

        // delete .crt file and .key file
        FileUtils.deleteFile(RootCertConstant.FOLDERPATH, crtFileName, keyFileName);
    }


    /**
     * package internal cert params
     */
    private RootCert packagePubParams(CreateRootCertParams createRootCertParams) {
        String dnsArray = JsonUtils.toJson(createRootCertParams.getDnsArray());
        String ipArray = JsonUtils.toJson(createRootCertParams.getIpArray());
        RootCert rootCert = new RootCert();
        BeanUtils.copyProperties(createRootCertParams, rootCert);
        rootCert.setDnsList(dnsArray);
        rootCert.setIpList(ipArray);
        rootCert.setSource(RootCertConstant.SOURCEINTER);
        rootCert.setSubject(RootCertConstant.SUBJECT);
        packagePubParams(rootCert);
        return rootCert;
    }


    /**
     * 1、创建临时文件 .crt
     * 2、解析 .crt文件，得出subject
     * 3、删除临时文件 .crt
     * 4、.key file -> string
     * 5、封装RootCert
     * 6、save to db
     */
    public void importCA(ImportRootCertParams importRootCertParams) throws IOException {
        log.info("============= 开始导入根证书");
        verifyIpAndDns(importRootCertParams.getIpArray(), importRootCertParams.getDnsArray());
        String certId = String.valueOf(IdWorker.getNextId());
        String crtFileName = String.format(RootCertConstant.CRTFILE, certId);
        MultipartFile crtFile = importRootCertParams.getCrtFile();
        MultipartFile keyFile = importRootCertParams.getKeyFile();

        createCrtFile(crtFileName, crtFile);
        String subject = parseCrtFile(crtFileName);
        FileUtils.deleteFile(RootCertConstant.FOLDERPATH, crtFileName);

        InputStream keyInputStream = null, crtInputStream = null;
        try {
            keyInputStream = keyFile.getInputStream();
            crtInputStream = crtFile.getInputStream();
            String crtFileContent = IOUtils.toString(crtInputStream, FileUtils.ENCODING);
            String keyFileContent = IOUtils.toString(keyInputStream, FileUtils.ENCODING);
            RootCert rootCert = packagePubParams(importRootCertParams);
            rootCert.setId(certId);
            rootCert.setSubject(subject);
            rootCert.setMainPubEncKey(crtFileContent);
            rootCert.setMainPriEncKey(keyFileContent);
            // rootCert.setCertName(Objects.requireNonNull(crtFile.getOriginalFilename()).split("\\.")[0]);

            rootCertMapper.insertRootCert(rootCert);
        } finally {
            if (keyInputStream != null) {
                keyInputStream.close();
            }
            if (crtInputStream != null) {
                crtInputStream.close();
            }
        }
    }


    /**
     * MultipartFile -> InputStream -> byte[] -> File
     */
    public void createCrtFile(String crtFileName, MultipartFile importFile) {
        try(InputStream inputStream = importFile.getInputStream()) {
            String crtFileContent = IOUtils.toString(inputStream, FileUtils.ENCODING);
            String folder = RootCertConstant.FOLDERPATH;
            File file = new File(folder);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileUtils.writeToFile(crtFileContent, folder, crtFileName);
        } catch (IOException e) {
            log.error("========= 导入证书 创建ca.crt临时文件失败：", e);
        }
    }


    /**
     * Parsing .crt file generation subject content by openssl cmd
     * @return subject
     */
    public String parseCrtFile(String crtFileName) throws IOException {
        String subject = null;
        InputStream inputStream = null;
        try {
            String cmd = "openssl x509 -in " + RootCertConstant.FOLDERPATH + crtFileName + " -noout -text";
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            inputStream = process.getInputStream();
            String outContent = IOUtils.toString(inputStream, FileUtils.ENCODING);
            subject = regexParse(outContent);
        } catch (Exception e) {
            log.error("error");
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return subject;
    }


    /**
     * regex: ca.crt content -> subject
     */
    private String regexParse(String content) {
        Map<String, String> map = new LinkedHashMap<>();
        Pattern pattern = Pattern.compile("Subject: C=(\\w+), ST=(\\w+), L=(\\w+), O=(\\w+), OU=(\\w+), CN=(\\w+)");
        Matcher matcher = pattern.matcher(content);
        Class subject = Subject.class;
        Field[] fields = subject.getDeclaredFields();
        while (matcher.find()) {
            int count = matcher.groupCount();
            for(int i=1; i<=count; i++) {
                String name = fields[i-1].getName();
                String value = matcher.group(i);
                map.put(name, value);
            }
            break;
        }
        return JsonUtils.toJson(map);
    }


    /**
     * package external cert params
     */
    private RootCert packagePubParams(ImportRootCertParams importRootCertParams) {
        RootCert rootCert = new RootCert();
        CreateRootCertParams createRootCertParams = new CreateRootCertParams();
        BeanUtils.copyProperties(importRootCertParams, createRootCertParams);
        BeanUtils.copyProperties(createRootCertParams, rootCert);
        String dnsArray = JsonUtils.toJson(createRootCertParams.getDnsArray());
        String ipArray = JsonUtils.toJson(createRootCertParams.getIpArray());
        rootCert.setDnsList(dnsArray);
        rootCert.setIpList(ipArray);
        rootCert.setSource(RootCertConstant.SOURCEEXTER);
        packagePubParams(rootCert);
        return rootCert;
    }


    /**
     * package public cert params
     */
    private void packagePubParams(RootCert rootCert) {
        String userId = RequestHolder.getUserId();
        rootCert.setName(RequestHolder.getUserName());
        rootCert.setCode(userId);
        rootCert.setAccountId(userId);
        rootCert.setCreatorId(RequestHolder.getCreatorId());
        rootCert.setType(RootCertConstant.X509);
        rootCert.setCreatedTime(new Date());
        rootCert.setUpdatedTime(new Date());
    }


    private void verifyIpAndDns(List<String> ipList, List<String> dnsList) {
        AssertUtils.isFalse(ipList.size() > 20 || dnsList.size() > 20, ExceptionCode.NUMBER_TOO_LONG, HttpStatus.OK);
        String regex =  "^(?:(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}(?:[0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\/([1-9]|[1-2]\\d|3[0-2]))?$";
        boolean flag = ipList.stream().allMatch(s -> s.matches(regex));
        AssertUtils.isTrue(flag, ExceptionCode.IP_FORMAT_ERROR, HttpStatus.OK);

    }


    /**
     * download .crt file or .key file
     */
    public void downloadCA(HttpServletResponse response, String certId, String fileType) throws IOException {
        RootCert rootCert = rootCertMapper.getRootCertDetail(RequestHolder.getUserId(), certId);
        String fileName = "CRT".equals(fileType) ? String.format(RootCertConstant.CRTFILE, certId): String.format(RootCertConstant.KEYFILE, certId);
        String fileContent = "CRT".equals(fileType) ? rootCert.getMainPubEncKey() : rootCert.getMainPriEncKey();

        packageResponse(response, fileName);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(fileContent.getBytes());
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }


    public void downloadZip(HttpServletResponse response, String certId) {
        RootCert rootCert = rootCertMapper.getRootCertDetail(RequestHolder.getUserId(), certId);
        String certName = rootCert.getCertName();
        String crtFileName = String.format(RootCertConstant.CRTFILE, certName);
        String keyFileName = String.format(RootCertConstant.KEYFILE, certName);
        String zipFileName = String.format(RootCertConstant.ZIPFILE, certName);

        String newFolderPath = certName + "/";
        File folder = new File(newFolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        FileUtils.writeToFile(rootCert.getMainPubEncKey(), newFolderPath, crtFileName);
        FileUtils.writeToFile(rootCert.getMainPriEncKey(), newFolderPath, keyFileName);

        List<File> srcFiles = Arrays.asList(new File(newFolderPath, crtFileName), new File(newFolderPath, keyFileName));
        packageResponse(response, zipFileName);
        try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[(int) srcFile.length()];
                zos.putNextEntry(new ZipEntry(newFolderPath + srcFile.getName()));
                try(FileInputStream in = new FileInputStream(srcFile)) {
                    while ((in.read(buf)) != -1){
                        zos.write(buf);
                    }
                    zos.closeEntry();
                }
            }
        } catch (Exception e) {
            log.error("zip error: ", e);
        }

        FileUtils.deleteFile(newFolderPath, crtFileName, keyFileName, zipFileName);
        if (folder.exists()) {
            folder.delete();
        }
    }


    public void packageResponse(HttpServletResponse response, String fileName) {
        response.setContentType("application/x-x509-ca-cert");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        response.setContentType("application/x-msdownload");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,HEAD,PUT,PATCH");
        response.setHeader("Access-Control-Max-Age", "36000");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Authorization,authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
    }


    /**
     * get cert details
     */
    public RootCert getRootCertDetail(String certId) {
        return rootCertMapper.getRootCertDetail(RequestHolder.getUserId(), certId);
    }


    public void editCA(RootCert rootCert) {
        String userId = RequestHolder.getUserId();
        RootCert newRootCert = rootCertMapper.getRootCertDetail(userId, rootCert.getId());
        AssertUtils.notNull(newRootCert, ExceptionCode.CA_NOT_EXIST, HttpStatus.OK);
        rootCert.setAccountId(userId);
        rootCert.setUpdatedTime(new Date());
        rootCertMapper.editRootCert(rootCert);
    }


    public void nameValid(String certName) {
        int res = rootCertMapper.nameValid(RequestHolder.getUserId(), certName);
        AssertUtils.isFalse(res == 1, ExceptionCode.CA_NAME_EXIST, HttpStatus.OK);
    }


    public List<RootCert> queryRootCertList() {
        return rootCertMapper.queryRootCertList(RequestHolder.getUserId());
    }

    public Page queryProductList(int pageNo, int pageSize, String certName) {
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        com.github.pagehelper.Page<Object> pageInfo = PageHelper.startPage(pageNo, pageSize);
        String userId = RequestHolder.getUserId();
        List<RootCert> certsList = rootCertMapper.qryProductListForPage(userId, certName);
        page.setTotalCount((int)pageInfo.getTotal());
        page.setData(certsList);
        return page;
    }
}

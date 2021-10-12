package com.inspur.cloud.console.device.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.inspur.cloud.common.exception.AssertUtils;
import com.inspur.cloud.common.exception.ExceptionCode;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.cloud.common.http.RequestHolder;
import com.inspur.cloud.common.page.Page;
import com.inspur.cloud.common.redis.RedisHelper;
import com.inspur.cloud.common.utils.FilesUtil;
import com.inspur.cloud.common.utils.SerialNumberUtil;
import com.inspur.cloud.common.utils.date.DateUtils;
import com.inspur.cloud.console.device.dao.DeviceDao;
import com.inspur.cloud.console.device.entity.Device;
import com.inspur.cloud.console.device.entity.DeviceVO;
import com.inspur.cloud.console.device.entity.DownLoad;
import com.inspur.cloud.console.device.entity.UploadData;
import com.inspur.cloud.console.device.openssl.IotClientCrt;
import com.inspur.cloud.console.kgc.service.KgcService;
import com.inspur.cloud.console.rootcert.filetools.FileUtils;
import com.inspur.cloud.console.usercert.dao.UserCertDao;
import com.inspur.cloud.console.usercert.entity.UserCert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipOutputStream;

@Service
public class DeviceService {

    private static final String CREATE_FILEPATH = "ssl_iid2/";

    private static final String DOWN_FILEPATH_SM9 = "sm9_iid/";

    private static final String DOWN_FILEPATH_X509 = "x509_iid/";
    //文件openssl3.cfg的位置
    private static final String OPENSSL_PATH = "openssl/openssl.cnf";

    private final DeviceDao deviceDao;

    private final UserCertDao userCertDao;

    private final KgcService kgcService;

    private final RedisHelper redisHelper;

    @Autowired
    public DeviceService(DeviceDao deviceDao, UserCertDao userCertDao,
                         KgcService kgcService, RedisHelper redisHelper) {
        this.deviceDao = deviceDao;
        this.userCertDao = userCertDao;
        this.kgcService = kgcService;
        this.redisHelper = redisHelper;
    }

    public void download(ExcelWriter excelWriter, String type, String productName, String code, Integer status, HttpServletResponse response) {
        String userId = RequestHolder.getUserId();
        Date date = new Date();
        if (redisHelper.isExistForValue(userId)) {
            Long lastDate = (Long) redisHelper.getObjectValue(userId);
            Long newDate = date.getTime();
            AssertUtils.isTrue(newDate > lastDate, ExceptionCode.LOAD_AGAIN, HttpStatus.OK);
        }
        Date experiedDate = DateUtils.getDateByInterval(date, Calendar.MINUTE, 5);
        redisHelper.setForValue(userId, experiedDate.getTime());
        if ("0".equals(type)) {
            int totalCount = deviceDao.selectTotalCount(userId, type);
            int count = totalCount / 1000;
            WriteSheet writeSheet = EasyExcel.writerSheet("device").build();
            for (int i = 0; i < count; i++) {
                List<DownLoad> data = deviceDao.selectDevicesByPage(userId, i, 1000, productName, code, status, type);
//            数据写出
                excelWriter.write(data, writeSheet);
                data.clear();
            }
            List<DownLoad> data = deviceDao.selectDevicesByPage(userId, count, totalCount % 1000, productName, code, status, type);
//            数据写出
            excelWriter.write(data, writeSheet);
            data.clear();
            excelWriter.finish();
        } else {
            int totalCount = deviceDao.selectTotalCount(userId, type);
            int count = totalCount / 1000;
            String path = DOWN_FILEPATH_X509 + userId;
            List<String> filePath = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                List<DownLoad> data = deviceDao.selectDevicesByPage(userId, i, 1000, productName, code, status, type);
                for (DownLoad device : data) {
                    File hubCodeFile = new File(path);
                    if (!hubCodeFile.exists()) {
                        hubCodeFile.mkdirs();
                    }
                    String deviceCode = device.getCode();
                    createDeviceX509File(userId, path, filePath,
                            deviceCode, device.getDevPriVfyKey(), device.getDevPriEncKey(), device.getId());
                }
            }
            List<DownLoad> data = deviceDao.selectDevicesByPage(userId, count, totalCount % 1000, productName, code, status, type);
            for (DownLoad device : data) {
                File hubCodeFile = new File(path);
                if (!hubCodeFile.exists()) {
                    hubCodeFile.mkdirs();
                }
                String deviceCode = device.getCode();
                createDeviceX509File(userId, path, filePath, deviceCode, device.getDevPriVfyKey(), device.getDevPriEncKey(), device.getId());
            }
            try {
                //打包下载
                type = "X509";
                downloadCert(path, filePath, "s", type, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File folderFile = new File(path);
            IotClientCrt.delFile(folderFile);
        }
        redisHelper.removeForValue(userId);
    }

    public void create(String productId, String code, String type) {
        String userId = RequestHolder.getUserId();
        Integer num = deviceDao.selectDeviceByCode(code, userId);
        AssertUtils.isTrue(num == 0, ExceptionCode.CODE_AGAIN, HttpStatus.OK);
        List<String> list = deviceDao.selectProductId(userId);
        AssertUtils.isTrue(list.contains(productId), ExceptionCode.PRODUCT_ID_IS_INVALID, HttpStatus.OK);
        //判断使用加密的类型
        if ("0".equals(type)) {
            Device device = getDevice(productId, code, userId, type);
            UserCert userCert = userCertDao.getUserCertByProductId(userId, productId);
            kgcService.createIdentitySecret(userCert, device);
            deviceDao.insert(device);
        } else {
            Device device = getDevice(productId, code, userId, type);
            UserCert userCert = userCertDao.getUserCertByProductId(userId, productId);
            String clientOutFolder = CREATE_FILEPATH + userId + "_" + SerialNumberUtil.getRandomNum();
            try {
                //调openssl生成设备证书，设备密钥
                getDeviceKeyAndCertificate(clientOutFolder, code, userId, userCert);
                //读取文件中的设备证书，及设备密钥
                String deviceKeyFileName = clientOutFolder + "/" + code + ".pem";
                String deviceCertificateFileName = clientOutFolder + "/" + code + ".crt";
                String deviceKey = FileUtils.readToString(deviceKeyFileName);
                String deviceCertificate = FileUtils.readToString(deviceCertificateFileName);
                device.setDevPriEncKey(deviceKey);
                device.setDevPriVfyKey(deviceCertificate);
                //System.out.println(1/0);//模拟失败
                deviceDao.insert(device);
            } catch (Exception e) {
                File folderFile = new File(clientOutFolder);
                IotClientCrt.delFile(folderFile);
                e.printStackTrace();
                throw new ServiceException(HttpStatus.OK, ExceptionCode.CREATE_DEVICE_IS_INVALID);
            }
            //清理临时文件
            File folderFile = new File(clientOutFolder);
            IotClientCrt.delFile(folderFile);
        }
    }

    @Async
    public void createSecrets(InputStream inputStream, String userId, String creatorId, String type) {
        HashSet hashSet = new HashSet(deviceDao.selectProductId(userId));
        EasyExcel.read(inputStream, UploadData.class, new UploadDataListener(deviceDao, hashSet, kgcService, userId, creatorId, type, userCertDao)).sheet().doRead();
    }

    public Page querySecretKeyList(int pageNo, int pageSize, String productName, String code, Integer status, String type) {
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        com.github.pagehelper.Page<Object> pageInfo = PageHelper.startPage(pageNo, pageSize);
        String userId = RequestHolder.getUserId();
        List<DeviceVO> deviceList = deviceDao.querySecretKeyList(userId, productName, code, status, type);
        ArrayList<DeviceVO> deviceVOS = new ArrayList<>();
        for (DeviceVO device : deviceList) {
            device.setProductName(deviceDao.queryProductNameById(device.getProductId()));
            deviceVOS.add(device);
        }
        page.setTotalCount((int) pageInfo.getTotal());
        page.setData(deviceVOS);
        return page;
    }

    public void downloadDevice(String deviceId, HttpServletResponse response, String userId) {
        Device device = deviceDao.queryDeviceById(deviceId, userId);
        String deviceType = device.getType();
        String deviceCode = device.getCode();
        String path;
        List<String> filePath = new ArrayList<>();
        //sm9
        if ("0".equals(deviceType)) {
            path = DOWN_FILEPATH_SM9 + userId + "_" + SerialNumberUtil.getRandomNum();
            File hubCodeFile = new File(path);
            if (!hubCodeFile.exists()) {
                hubCodeFile.mkdirs();
            }
            createDeviceSM9File(device, deviceCode, path, filePath);
            deviceType = "SM9";
            //打包下载
            try {
                downloadCert(path, filePath, deviceCode, deviceType, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            path = DOWN_FILEPATH_X509 + userId + "_" + SerialNumberUtil.getRandomNum();
            File hubCodeFile = new File(path);
            if (!hubCodeFile.exists()) {
                hubCodeFile.mkdirs();
            }
            createDeviceX509File(userId, path, filePath, deviceCode, device.getDevPriVfyKey(), device.getDevPriEncKey(), deviceId);
            deviceType = "X509";
            //打包下载
            try {
                downloadCert(path, filePath, deviceCode, deviceType, response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        File folderFile = new File(path);
        IotClientCrt.delFile(folderFile);
    }

    public void setDeviceStatus(String deviceId, String deviceSwitch, String userId) {
        Integer num = deviceDao.selectDeviceById(deviceId);
        AssertUtils.isTrue(num>0, ExceptionCode.DEVICE_NOT_EXIST, HttpStatus.OK);
        AssertUtils.isTrue("0".equals(deviceSwitch)||"1".equals(deviceSwitch), ExceptionCode.DEVICE_STATUS_ERROR, HttpStatus.OK);
        deviceDao.setDeviceStatus(deviceId, deviceSwitch);
    }

    public String getDeviceStatus(String enterpriseId, String code) {
        AssertUtils.isTrue(deviceDao.selectUserByEnterpriseId(enterpriseId)>0, ExceptionCode.ENTERPRISE_NOT_EXIST, HttpStatus.OK);
        AssertUtils.isTrue(deviceDao.selectDeviceByCode(code,enterpriseId)>0, ExceptionCode.DEVICE_NOT_EXIST, HttpStatus.OK);
        return deviceDao.selectDeviceStatus(enterpriseId, code);
    }


    private Device getDevice(String productId, String code, String userId, String type) {
        Device device = new Device();
        device.setProductId(productId);
        device.setCode(code);
        device.setAccountId(userId);
        device.setCreatorId(RequestHolder.getCreatorId());
        device.setModifiedBy(userId);
        device.setCreatedBy(userId);
        device.setBatchNo(String.valueOf(new Date().getTime()));
        device.setCreatedTime(new Date());
        device.setUpdatedTime(new Date());
        device.setType(type);
        return device;
    }

    //生成x509文件
    private void createDeviceX509File(String userId, String path, List<String> filePath, String deviceCode, String devPriVfyKey, String devPriEncKey, String deviceId) {
        String certificatePath = path + "/" + deviceCode + ".crt";
        String keyPath = path + "/" + deviceCode + ".pem";
        String caPath = path + "/" + deviceCode + "-ca.crt";
        filePath.add(certificatePath);
        filePath.add(keyPath);
        filePath.add(caPath);
        //生成设备证书，设备私钥，ca.crt
        String devCertificate = devPriVfyKey;
        String deviceKey = devPriEncKey;
        String caCertificate = userCertDao.getUserCertByDeviceId(userId, deviceId).getMainPubEncKey();
        FileUtils.writeToFile(devCertificate, certificatePath);
        FileUtils.writeToFile(deviceKey, keyPath);
        FileUtils.writeToFile(caCertificate, caPath);
    }

    //生成sm9文件
    private void createDeviceSM9File(Device device, String deviceCode, String path, List<String> filePath) {
        String devPriEncKeyPath = path + "/" + deviceCode + ".jmkey";
        String devPriVfyKeyPath = path + "/" + deviceCode + ".qmkey";
        filePath.add(devPriEncKeyPath);
        filePath.add(devPriVfyKeyPath);
        //生成设备 加密私钥 签名私钥
        String devPriEncKey = device.getDevPriEncKey();
        String devPriVfyKey = device.getDevPriVfyKey();
        FileUtils.writeToFile(devPriVfyKey, devPriEncKeyPath);
        FileUtils.writeToFile(devPriEncKey, devPriVfyKeyPath);
    }

    //打包下载
    public void downloadCert(String zipBasePath, List<String> filePaths, String code, String type, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        // 创建压缩文件需要的空的zip包
        // 判断 /sslkeyclient/{{hubcode}}文件夹是否存在
        File hubCodeFile = new File(zipBasePath);
        if (!hubCodeFile.exists()) {
            hubCodeFile.mkdirs();
        }
        String zipFilePath = zipBasePath + "-" + code + ".zip";
        // 根据临时的zip压缩包路径，创建zip文件
        File zip = new File(zipFilePath);
        if (!zip.exists()) {
            zip.createNewFile();
        }
        // 创建zip文件输出流
        FileOutputStream fos = new FileOutputStream(zip);
        ZipOutputStream zos = new ZipOutputStream(fos);
        // 创建文件路径的集合，
        List<String> filePath = filePaths;
        // 循环读取文件路径集合，获取每一个文件的路径
        for (String fp : filePath) {
            File f = new File(fp); // 根据文件路径创建文件
            FilesUtil.zipFile(f, zos); // 将每一个文件写入zip文件包内，即进行打包
        }
        zos.close();
        fos.close();
        try {
            java.lang.Thread.currentThread();
            java.lang.Thread.sleep(2000);// 毫秒
        } catch (Exception e) {
        }
        // 将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
        // 设置文件类型()
        response.setContentType("application/pdf;charset=utf-8");
        String fileName = type + "-" + code + ".zip";
        // 文件名
        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + new String(fileName.getBytes(), "utf-8") + "\"");
        response.setContentLength((int) zip.length());
        byte[] buffer = new byte[4096];// 缓冲区
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
             BufferedInputStream input = new BufferedInputStream(new FileInputStream(zip))) {
            int n = -1;
            // 遍历，开始下载
            while ((n = input.read(buffer, 0, 4096)) > -1) {
                output.write(buffer, 0, n);
            }
            output.flush(); // 不可少
            response.flushBuffer();// 不可少
        } catch (Exception e) {
            // 异常自己捕捉
        }
        zip.delete();
    }

    //openssl生成设备证书，设备密钥
    private void getDeviceKeyAndCertificate(String clientOutFolder, String code, String userId, UserCert userCert) throws Exception {
        //openSSL的安装路径
        String openSSLPath = "openSSL的安装路径";
        //证书ca Crt 的路径
        String rootCACrtPath = clientOutFolder + "/ca.crt";
        File file = new File(clientOutFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        //查询得 caCrt 并生成文件，写到路径下
        String caCrt = userCert.getMainPubEncKey();
        FileUtils.writeToFile(caCrt, rootCACrtPath);
        //证书ca Key
        String rootCAKeyPath = clientOutFolder + "/ca.key";
        //查询得 caKey 并生成文件，写到路径下
        String caKey = userCert.getMainPriEncKey();
        FileUtils.writeToFile(caKey, rootCAKeyPath);
        //文件openssl3.cfg的位置
        //读取openssl中的openssl3.cfg配置文件
        String openssl3Prefix = FileUtils.readToStringByLinux(OPENSSL_PATH);
        StringBuffer sb = new StringBuffer();
        String ipList = userCert.getIpList();
        String DNSList = userCert.getDnsList();
        String ips = getString("IP.", ipList);
        String DNSs = getString("DNS.", DNSList);
        sb.append(openssl3Prefix).append("[ v3_req ] \n" +
                "# Extensions to add to a certificate request \n" +
                "basicConstraints = CA:FALSE \n" +
                "keyUsage = nonRepudiation, digitalSignature, keyEncipherment \n" +
                "subjectAltName = @alt_names \n" +
                "#ext key usage = TLS Web Server Authentication, TLS Web Client \n" +
                "#Authentication \n" +
                "\n" +
                "[alt_names] \n" +
                ips +
                DNSs);
        String oppenssl = sb.toString();
        FileUtils.writeToFile(oppenssl, clientOutFolder + "/openssl3.cfg");
        String opensslcnfPath = clientOutFolder + "/openssl3.cfg";

        String rsaCount = userCert.getRsaCount();
        String days = userCert.getDays();
        String pass = userCert.getPassword();

        IotClientCrt.genIotClientCrt(openSSLPath, rootCACrtPath, rootCAKeyPath,
                clientOutFolder, opensslcnfPath, code, rsaCount, days, pass);

    }

    private String getString(String s, String StringList) {
        JSONArray stringJsonArray = JSONArray.parseArray(StringList);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < stringJsonArray.size(); i++) {
            String str = stringJsonArray.get(i).toString();
            str = s + (i + 1) + "=" + str + " \n";
            sb.append(str);
        }
        return sb.toString();
    }
}

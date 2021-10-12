package com.inspur.cloud.console.device.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.inspur.cloud.common.exception.model.ServiceException;
import com.inspur.cloud.common.utils.SerialNumberUtil;
import com.inspur.cloud.console.device.dao.DeviceDao;
import com.inspur.cloud.console.device.entity.Device;
import com.inspur.cloud.console.device.entity.UploadData;
import com.inspur.cloud.console.device.openssl.IotClientCrt;
import com.inspur.cloud.console.kgc.service.KgcService;
import com.inspur.cloud.console.rootcert.filetools.FileUtils;
import com.inspur.cloud.console.usercert.dao.UserCertDao;
import com.inspur.cloud.console.usercert.entity.UserCert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点  不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class UploadDataListener extends AnalysisEventListener<UploadData> {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UploadDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 1000;

    private static final String CREATE_FILEPATH = "ssl_iid2/";

    private static final String OPENSSL_PATH = "openssl/openssl.cnf";

    List<UploadData> list = new ArrayList<UploadData>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private DeviceDao deviceDao;

    private HashSet<String> productList;

    private KgcService kgcService;

    private String batchNo;

    private String userId;

    private String creatorId;

    private String type;

    private UserCertDao userCertDao;
    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param deviceDao
     */
    public UploadDataListener(DeviceDao deviceDao, HashSet<String> productList,KgcService kgcService,
                              String userId,String creatorId, String type, UserCertDao userCertDao) {
        this.deviceDao = deviceDao;
        this.productList=productList;
        this.kgcService=kgcService;
        this.batchNo=String.valueOf(new Date().getTime());
        this.userId=userId;
        this.creatorId=creatorId;
        this.type = type;
        this.userCertDao = userCertDao;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data
     *            one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(UploadData data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LOGGER.info("{}条数据，开始存储数据库！", list.size());
        List<Device> devices = new ArrayList<>();
        List<String> exitList=deviceDao.selectCodeByData(list,userId);
        for(UploadData uploadData:list){
            if(productList.contains(uploadData.getProductId())){
                if(!exitList.contains(uploadData.getCode())){
                    if ("0".equals(type)){
                        UserCert userCert = userCertDao.getUserCertByProductId(userId, uploadData.getProductId());
                        Device device = getDevice(uploadData, type);
                        kgcService.createIdentitySecret(userCert,device);
                        devices.add(device);
                    }else if ("1".equals(type)){
                        Device device = getDevice(uploadData, type);
                        String clientOutFolder = CREATE_FILEPATH + userId + "_"+ SerialNumberUtil.getRandomNum();
                        String code = uploadData.getCode();
                        try {
                            //调openssl生成设备证书，设备密钥
                            UserCert userCert = userCertDao.getUserCertByProductId(userId, uploadData.getProductId());
                            getDeviceKeyAndCertificate(clientOutFolder, code, userId, userCert);
                            //读取文件中的设备证书，及设备密钥
                            String deviceKeyFileName = clientOutFolder+"/" + code +".pem";
                            String deviceCertificateFileName = clientOutFolder+"/" + code + ".crt";
                            String deviceKey = FileUtils.readToString(deviceKeyFileName);
                            String deviceCertificate =  FileUtils.readToString(deviceCertificateFileName);
                            device.setDevPriEncKey(deviceKey);//测试数据
                            device.setDevPriVfyKey(deviceCertificate);
                            //System.out.println(1/0);//模拟失败
                        } catch (Exception e) {
                            File folderFile = new File(clientOutFolder);
                            IotClientCrt.delFile(folderFile);
                            e.printStackTrace();
                            throw new ServiceException(e.toString());
                        }
                        devices.add(device);
                        //清理临时文件
                        File folderFile = new File(clientOutFolder);
                        IotClientCrt.delFile(folderFile);
                    }
                }
            }
        }
        if(!devices.isEmpty()){
            deviceDao.insertForList(devices);
        }
        LOGGER.info("存储数据库成功！");
    }

    private Device getDevice(UploadData uploadData, String type) {
        Device device = new Device();
        device.setProductId(uploadData.getProductId());
        device.setCode(uploadData.getCode());
        device.setAccountId(userId);
        device.setCreatorId(creatorId);
        device.setModifiedBy(userId);
        device.setCreatedBy(userId);
        device.setBatchNo(batchNo);
        device.setCreatedTime(new Date());
        device.setUpdatedTime(new Date());
        device.setType(type);
        return device;
    }



    private void getDeviceKeyAndCertificate(String clientOutFolder, String code, String userId, UserCert userCert) throws Exception {
        //openSSL的安装路径
        String openSSLPath = "openSSL的安装路径";
        //证书ca Crt 的路径
        String rootCACrtPath =clientOutFolder + "/ca.crt";
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
                ips+DNSs);
        String oppenssl = sb.toString();
        FileUtils.writeToFile(oppenssl,clientOutFolder+"/openssl3.cfg");
        String opensslcnfPath = clientOutFolder+"/openssl3.cfg";
        String rsaCount = userCert.getRsaCount();
        String days = userCert.getDays();
        String pass = userCert.getPassword();
        IotClientCrt.genIotClientCrt(openSSLPath, rootCACrtPath, rootCAKeyPath,
                clientOutFolder, opensslcnfPath , code, rsaCount, days, pass);
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
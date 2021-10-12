package com.inspur.cloud.console.rootcert.constant;

public class RootCertConstant {

    public static final String FOLDERPATH = "iid-sslkey/";

    public static final String ROOTCMDPRE = "openssl req -new -x509 -newkey rsa:%s -days %s ";  // -extensions v3_ca ";

    public static final String ROOTCMDBACK = "-passout pass:%s -subj /C=CN/ST=iid/L=iid/O=iid/OU=iid/CN=ca";

    public static final String CRTFILE = "%s.crt";

    public static final String KEYFILE = "%s.key";

    public static final String ZIPFILE = "%s.zip";

    public static final String FAILED = "404";

    public static final String SUBJECT = "{\"C\":\"CN\",\"ST\":\"iid\",\"L\":\"iid\",\"O\":\"iid\",\"OU\":\"iid\",\"CN\":\"ca\"}";

    public static final String X509 = "X509";

    public static final String SM9 = "SM9";

    public static final String SOURCEINTER = "Internal";

    public static final String SOURCEEXTER = "External";
}

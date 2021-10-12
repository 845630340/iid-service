package com.inspur.cloud.console.rootcert.entity;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CreateRootCertParams extends RootCert {

    protected List<String> dnsArray;

    protected List<String> ipArray;
}

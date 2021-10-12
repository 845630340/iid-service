package com.inspur.cloud.console.rootcert.entity;


import lombok.Data;

@Data
public class Subject {
    private String C = "CN";

    private String ST = "iid";

    private String L = "iid";

    private String O = "iid";

    private String OU = "iid";

    private String CN = "ca";
}

package com.inspur.cloud.common.liquibase;

import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.ResourceAccessor;

/**
 * 用于解决liquibase 打包成jar包后，路径有前缀BOOT-INF/classes/的问题
 * @author hexinyu
 */
public class MyChangeLogParser extends XMLChangeLogSAXParser {
    private final static String BOOT_CLASSES = "BOOT-INF/classes/";

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        DatabaseChangeLog log = super.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
        for (ChangeSet set : log.getChangeSets()) {
            String filePath = set.getFilePath();
            if (filePath.startsWith(BOOT_CLASSES)) {
                set.setFilePath(filePath.substring(BOOT_CLASSES.length()));
            }
        }
        return log;
    }
}

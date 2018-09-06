package com.rs.common.util.uuid;

import java.util.UUID;

/**
 * 生成uuid
 * @author liegou
 */
public class UuId {
    /**
     * 获取无符号的uuid
     * @author liegou
     * @return String
     * @throws
     */
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    /**
     *  获取有符号的uuid
     * @author liegou
     * @return String
     * @throws
     */
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}

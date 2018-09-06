package com.rs.common.txmanager;

import com.alibaba.dubbo.config.annotation.Service;
import com.codingapi.tx.config.service.TxManagerTxUrlService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 复写读取配置文件的类,读取自定义配置的协调中心地址
 */
@Component
@Service
public class CommonTxManagerTxUrlServiceImpl implements TxManagerTxUrlService{


    @Value("${tm.manager.url}")
    private String url;

    @Override
    public String getTxUrl() {
        return url;
    }
}

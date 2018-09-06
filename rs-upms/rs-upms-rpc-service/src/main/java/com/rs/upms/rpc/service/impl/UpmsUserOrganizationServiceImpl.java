package com.rs.upms.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.rs.common.annotation.BaseService;
import com.rs.common.base.BaseServiceImpl;
import com.rs.upms.dao.mapper.UpmsUserOrganizationMapper;
import com.rs.upms.dao.model.UpmsUserOrganization;
import com.rs.upms.dao.model.UpmsUserOrganizationExample;
import com.rs.upms.rpc.api.UpmsUserOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
*
* UpmsUserOrganizationService实现
* @author liegou
* @date 2018/6/20.
*/
@Service
@Transactional(rollbackFor = Exception.class)
@BaseService
public class UpmsUserOrganizationServiceImpl extends BaseServiceImpl<UpmsUserOrganizationMapper, UpmsUserOrganization, UpmsUserOrganizationExample> implements UpmsUserOrganizationService {

    private static Logger log = LoggerFactory.getLogger(UpmsUserOrganizationServiceImpl.class);

    @Autowired
    UpmsUserOrganizationMapper upmsUserOrganizationMapper;

}
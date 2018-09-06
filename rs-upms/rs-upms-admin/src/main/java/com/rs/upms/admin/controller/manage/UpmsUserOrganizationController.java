package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsUserOrganization;
import com.rs.upms.dao.model.UpmsUserOrganizationExample;
import com.rs.upms.rpc.api.UpmsUserOrganizationService;
import com.reger.dubbo.annotation.Inject;
import com.rs.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UpmsUserOrganizationcontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsUserOrganization")
@Api(value = "UpmsUserOrganization管理", description = "UpmsUserOrganization管理")
public class UpmsUserOrganizationController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsUserOrganizationController.class);

    @Inject
    private UpmsUserOrganizationService upmsUserOrganizationService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:user:organization:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsUserOrganizationExample upmsUserOrganizationExample = new UpmsUserOrganizationExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsUserOrganizationExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsUserOrganizationService.countByExample(upmsUserOrganizationExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsUserOrganization> rows = upmsUserOrganizationService.selectByExampleForOffsetPage(upmsUserOrganizationExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:user:organization:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsUserOrganization upmsUserOrganization) {
            int count = upmsUserOrganizationService.insertSelective(upmsUserOrganization);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:user:organization:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsUserOrganizationService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:user:organization:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsUserOrganization upmsUserOrganization) {
            int count = upmsUserOrganizationService.updateByPrimaryKeySelective(upmsUserOrganization);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:user:organization:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsUserOrganization upmsUserOrganization = upmsUserOrganizationService.selectByPrimaryKey(id);
            modelMap.put("upmsUserOrganization", upmsUserOrganization);
            return BaseResult.newSuccess(modelMap);
        }





}
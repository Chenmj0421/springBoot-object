package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsRolePermission;
import com.rs.upms.dao.model.UpmsRolePermissionExample;
import com.rs.upms.rpc.api.UpmsRolePermissionService;
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
 * UpmsRolePermissioncontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsRolePermission")
@Api(value = "UpmsRolePermission管理", description = "UpmsRolePermission管理")
public class UpmsRolePermissionController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsRolePermissionController.class);

    @Inject
    private UpmsRolePermissionService upmsRolePermissionService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:role:permission:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsRolePermissionExample upmsRolePermissionExample = new UpmsRolePermissionExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsRolePermissionExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsRolePermissionService.countByExample(upmsRolePermissionExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsRolePermission> rows = upmsRolePermissionService.selectByExampleForOffsetPage(upmsRolePermissionExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:role:permission:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsRolePermission upmsRolePermission) {
            int count = upmsRolePermissionService.insertSelective(upmsRolePermission);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:role:permission:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsRolePermissionService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:role:permission:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsRolePermission upmsRolePermission) {
            int count = upmsRolePermissionService.updateByPrimaryKeySelective(upmsRolePermission);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:role:permission:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsRolePermission upmsRolePermission = upmsRolePermissionService.selectByPrimaryKey(id);
            modelMap.put("upmsRolePermission", upmsRolePermission);
            return BaseResult.newSuccess(modelMap);
        }





}
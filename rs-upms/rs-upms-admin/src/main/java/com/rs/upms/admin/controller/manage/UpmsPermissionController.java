package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsPermission;
import com.rs.upms.dao.model.UpmsPermissionExample;
import com.rs.upms.rpc.api.UpmsPermissionService;
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
 * UpmsPermissioncontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsPermission")
@Api(value = "UpmsPermission管理", description = "UpmsPermission管理")
public class UpmsPermissionController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsPermissionController.class);

    @Inject
    private UpmsPermissionService upmsPermissionService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:permission:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsPermissionExample upmsPermissionExample = new UpmsPermissionExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsPermissionExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsPermissionService.countByExample(upmsPermissionExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsPermission> rows = upmsPermissionService.selectByExampleForOffsetPage(upmsPermissionExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:permission:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsPermission upmsPermission) {
            int count = upmsPermissionService.insertSelective(upmsPermission);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:permission:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsPermissionService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:permission:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsPermission upmsPermission) {
            int count = upmsPermissionService.updateByPrimaryKeySelective(upmsPermission);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("${permission}:read")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsPermission upmsPermission = upmsPermissionService.selectByPrimaryKey(id);
            modelMap.put("upmsPermission", upmsPermission);
            return BaseResult.newSuccess(modelMap);
        }





}
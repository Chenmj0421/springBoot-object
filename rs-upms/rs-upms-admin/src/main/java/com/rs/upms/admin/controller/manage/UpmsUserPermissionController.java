package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsUserPermission;
import com.rs.upms.dao.model.UpmsUserPermissionExample;
import com.rs.upms.rpc.api.UpmsUserPermissionService;
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
 * UpmsUserPermissioncontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsUserPermission")
@Api(value = "UpmsUserPermission管理", description = "UpmsUserPermission管理")
public class UpmsUserPermissionController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsUserPermissionController.class);

    @Inject
    private UpmsUserPermissionService upmsUserPermissionService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:user:permission:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsUserPermissionExample upmsUserPermissionExample = new UpmsUserPermissionExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsUserPermissionExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsUserPermissionService.countByExample(upmsUserPermissionExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsUserPermission> rows = upmsUserPermissionService.selectByExampleForOffsetPage(upmsUserPermissionExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:user:permission:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsUserPermission upmsUserPermission) {
            int count = upmsUserPermissionService.insertSelective(upmsUserPermission);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:user:permission:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsUserPermissionService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:user:permission:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsUserPermission upmsUserPermission) {
            int count = upmsUserPermissionService.updateByPrimaryKeySelective(upmsUserPermission);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:user:permission:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsUserPermission upmsUserPermission = upmsUserPermissionService.selectByPrimaryKey(id);
            modelMap.put("upmsUserPermission", upmsUserPermission);
            return BaseResult.newSuccess(modelMap);
        }





}
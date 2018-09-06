package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsUserRole;
import com.rs.upms.dao.model.UpmsUserRoleExample;
import com.rs.upms.rpc.api.UpmsUserRoleService;
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
 * UpmsUserRolecontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsUserRole")
@Api(value = "UpmsUserRole管理", description = "UpmsUserRole管理")
public class UpmsUserRoleController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsUserRoleController.class);

    @Inject
    private UpmsUserRoleService upmsUserRoleService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:user:role:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsUserRoleExample upmsUserRoleExample = new UpmsUserRoleExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsUserRoleExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsUserRoleService.countByExample(upmsUserRoleExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsUserRole> rows = upmsUserRoleService.selectByExampleForOffsetPage(upmsUserRoleExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:user:role:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsUserRole upmsUserRole) {
            int count = upmsUserRoleService.insertSelective(upmsUserRole);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:user:role:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsUserRoleService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:user:role:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsUserRole upmsUserRole) {
            int count = upmsUserRoleService.updateByPrimaryKeySelective(upmsUserRole);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:user:role:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsUserRole upmsUserRole = upmsUserRoleService.selectByPrimaryKey(id);
            modelMap.put("upmsUserRole", upmsUserRole);
            return BaseResult.newSuccess(modelMap);
        }





}
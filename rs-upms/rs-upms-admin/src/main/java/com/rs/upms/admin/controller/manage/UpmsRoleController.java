package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsRole;
import com.rs.upms.dao.model.UpmsRoleExample;
import com.rs.upms.rpc.api.UpmsRoleService;
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
 * UpmsRolecontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsRole")
@Api(value = "UpmsRole管理", description = "UpmsRole管理")
public class UpmsRoleController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsRoleController.class);

    @Inject
    private UpmsRoleService upmsRoleService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:role:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsRoleExample upmsRoleExample = new UpmsRoleExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsRoleExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsRoleService.countByExample(upmsRoleExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsRole> rows = upmsRoleService.selectByExampleForOffsetPage(upmsRoleExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:role:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsRole upmsRole) {
            int count = upmsRoleService.insertSelective(upmsRole);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:role:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsRoleService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:role:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsRole upmsRole) {
            int count = upmsRoleService.updateByPrimaryKeySelective(upmsRole);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:role:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsRole upmsRole = upmsRoleService.selectByPrimaryKey(id);
            modelMap.put("upmsRole", upmsRole);
            return BaseResult.newSuccess(modelMap);
        }





}
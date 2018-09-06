package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsLog;
import com.rs.upms.dao.model.UpmsLogExample;
import com.rs.upms.rpc.api.UpmsLogService;
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
 * UpmsLogcontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsLog")
@Api(value = "UpmsLog管理", description = "UpmsLog管理")
public class UpmsLogController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsLogController.class);

    @Inject
    private UpmsLogService upmsLogService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:log:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsLogExample upmsLogExample = new UpmsLogExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsLogExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsLogService.countByExample(upmsLogExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsLog> rows = upmsLogService.selectByExampleForOffsetPage(upmsLogExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:log:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsLog upmsLog) {
            int count = upmsLogService.insertSelective(upmsLog);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:log:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsLogService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:log:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsLog upmsLog) {
            int count = upmsLogService.updateByPrimaryKeySelective(upmsLog);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("${permission}:read")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsLog upmsLog = upmsLogService.selectByPrimaryKey(id);
            modelMap.put("upmsLog", upmsLog);
            return BaseResult.newSuccess(modelMap);
        }





}
package com.rs.upms.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.upms.dao.model.UpmsSystem;
import com.rs.upms.dao.model.UpmsSystemExample;
import com.rs.upms.rpc.api.UpmsSystemService;
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
 * UpmsSystemcontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsSystem")
@Api(value = "UpmsSystem管理", description = "UpmsSystem管理")
public class UpmsSystemController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsSystemController.class);

    @Inject
    private UpmsSystemService upmsSystemService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:system:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsSystemExample upmsSystemExample = new UpmsSystemExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsSystemExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsSystemService.countByExample(upmsSystemExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsSystem> rows = upmsSystemService.selectByExampleForOffsetPage(upmsSystemExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:system:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsSystem upmsSystem) {
            int count = upmsSystemService.insertSelective(upmsSystem);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:system:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsSystemService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:system:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsSystem upmsSystem) {
            int count = upmsSystemService.updateByPrimaryKeySelective(upmsSystem);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:system:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsSystem upmsSystem = upmsSystemService.selectByPrimaryKey(id);
            modelMap.put("upmsSystem", upmsSystem);
            return BaseResult.newSuccess(modelMap);
        }





}
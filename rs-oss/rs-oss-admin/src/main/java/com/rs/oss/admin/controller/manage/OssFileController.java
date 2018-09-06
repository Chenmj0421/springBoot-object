package com.rs.oss.admin.controller.manage;

import com.rs.common.base.BaseResult;
import com.rs.oss.dao.model.OssFile;
import com.rs.oss.dao.model.OssFileExample;
import com.rs.oss.rpc.api.OssFileService;
import com.reger.dubbo.annotation.Inject;
import com.rs.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * OssFilecontroller
 * @author liegou
 * @date 2018/7/16.
 */
@Controller
@RequestMapping("/manage/ossFile")
@Api(value = "OssFile管理", description = "OssFile管理")
public class OssFileController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(OssFileController.class);

    @Inject
    private OssFileService ossFileService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("oss:file:read")
    @ApiImplicitParams({
    @ApiImplicitParam(paramType = "query",name = "offset", value = "当前页数，默认 0", required = true, dataType = "Integer"),
    @ApiImplicitParam(paramType = "query",name = "limit", value = "每页限制数据条数，默认10", required = true, dataType = "Integer"),
    @ApiImplicitParam(paramType = "query",name = "sort", value = "排序字段", required = true, dataType = "String"),
    @ApiImplicitParam(paramType = "query",name = "order", value = "排序方式:降序 DESC 升序 ASC", required = true, dataType = "String")
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        OssFileExample ossFileExample = new OssFileExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            ossFileExample.setOrderByClause(sort + " " + order);
        }
        long total = ossFileService.countByExample(ossFileExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<OssFile> rows = ossFileService.selectByExampleForOffsetPage(ossFileExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("oss:file:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute OssFile ossFile) {
            int count = ossFileService.insertSelective(ossFile);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("oss:file:delete")
        @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "ids", value = "批量删除数据的主键，以‘,’逗号隔开", required = true, dataType = "String")
        })
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = ossFileService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("oss:file:update")
        @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "id", value = "主键", required = true, dataType = "String")
        })
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute OssFile ossFile) {
            int count = ossFileService.updateByPrimaryKeySelective(ossFile);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("oss:file:info")
        @ApiImplicitParams({
        @ApiImplicitParam(paramType = "path",name = "id", value = "主键", required = true, dataType = "String")
        })
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            OssFile ossFile = ossFileService.selectByPrimaryKey(id);
            modelMap.put("ossFile", ossFile);
            return BaseResult.newSuccess(modelMap);
        }





}
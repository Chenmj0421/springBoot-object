package com.rs.upms.admin.controller.manage;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.rs.common.base.BaseResult;
import com.rs.common.base.BaseResultConstant;
import com.rs.common.validator.LengthValidator;
import com.rs.common.validator.NotNullValidator;
import com.rs.upms.dao.model.UpmsUser;
import com.rs.upms.dao.model.UpmsUserExample;
import com.rs.upms.rpc.api.UpmsUserService;
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
 * UpmsUsercontroller
 * @author liegou
 * @date 2018/6/20.
 */
@Controller
@RequestMapping("/manage/upmsUser")
@Api(value = "UpmsUser管理", description = "UpmsUser管理")
public class UpmsUserController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsUserController.class);

    @Inject
    private UpmsUserService upmsUserService;


    @ApiOperation(value = "列表")
    @RequiresPermissions("upms:user:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") int offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") int limit,
            @RequestParam(required = false, defaultValue = "", value = "search") String search,
            @RequestParam(required = false, value = "sort") String sort,
            @RequestParam(required = false, value = "order") String order) {
        UpmsUserExample upmsUserExample = new UpmsUserExample();
        if (!StringUtils.isBlank(sort) && !StringUtils.isBlank(order)) {
            upmsUserExample.setOrderByClause(sort + " " + order);
        }
        long total = upmsUserService.countByExample(upmsUserExample);
        Map<String, Object> result = new HashMap<>(16);
        if (total == 0) {
           result.put("rows", new ArrayList());
           result.put("total", total);
           return result;
        }
        List<UpmsUser> rows = upmsUserService.selectByExampleForOffsetPage(upmsUserExample, offset, limit);
        result.put("rows", rows);
        result.put("total", total);
        return result;
    }

        @ApiOperation(value = "新增")
        @RequiresPermissions("upms:user:create")
        @RequestMapping(value = "/create", method = RequestMethod.POST)
        @ResponseBody
        public Object create(@ModelAttribute UpmsUser upmsUser) {
        ComplexResult result = FluentValidator.checkAll()
                .on(upmsUser.getUserName(), new LengthValidator(1, 20, "帐号"))
                .on(upmsUser.getPassword(), new LengthValidator(5, 32, "密码"))
                .on(upmsUser.getRealname(), new NotNullValidator("姓名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return BaseResult.newFailure(BaseResultConstant.INVALID_LENGTH.getCode(), BaseResultConstant.INVALID_LENGTH.getMessage());
        }
            int count = upmsUserService.insertSelective(upmsUser);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "删除")
        @RequiresPermissions("upms:user:delete")
        @RequestMapping(value = "/delete/{ids}",method = RequestMethod.GET)
        @ResponseBody
        public Object delete(@PathVariable("ids") String ids) {
            int count = upmsUserService.deleteByPrimaryKeys(ids);
            return BaseResult.newSuccess();
        }

        @ApiOperation(value = "修改")
        @RequiresPermissions("upms:user:update")
        @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
        @ResponseBody
        public Object update(@PathVariable("id") String id, @ModelAttribute UpmsUser upmsUser) {
            int count = upmsUserService.updateByPrimaryKeySelective(upmsUser);
            return BaseResult.newSuccess();
        }
        @ApiOperation(value = "查看")
        @RequiresPermissions("upms:user:info")
        @RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
        @ResponseBody
        public Object info(@PathVariable("id") String id, ModelMap modelMap) {
            UpmsUser upmsUser = upmsUserService.selectByPrimaryKey(id);
            modelMap.put("upmsUser", upmsUser);
            return BaseResult.newSuccess(modelMap);
        }





}
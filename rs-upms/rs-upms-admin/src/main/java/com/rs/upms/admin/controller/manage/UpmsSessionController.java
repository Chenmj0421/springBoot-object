package com.rs.upms.admin.controller.manage;


import com.rs.common.base.BaseController;
import com.rs.upms.client.shiro.session.UpmsSessionDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 会话管理controller
 * @author liegou
 */
@Controller
@Api(value = "会话管理", description = "会话管理")
@RequestMapping("/manage/session")
public class UpmsSessionController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(UpmsSessionController.class);

    @Autowired
    private UpmsSessionDao sessionDAO;



    @ApiOperation(value = "会话列表")
    @RequiresPermissions("upms:session:read")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(
            @RequestParam(required = false, defaultValue = "0", value = "offset") Long offset,
            @RequestParam(required = false, defaultValue = "10", value = "limit") Long limit) {
        return sessionDAO.getActiveSessions(offset, limit);
    }

    @ApiOperation(value = "强制退出")
    @RequiresPermissions("upms:session:forceout")
    @RequestMapping(value = "/forceout/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public Object forceout(@PathVariable("ids") String ids) {
        int count = sessionDAO.forceout(ids);
        return count;
    }

}
package com.rs.common.base;

import com.alibaba.dubbo.remoting.RemotingException;
import com.rs.common.exception.MyException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器基类
 *
 * @author liegou
 */
public abstract class BaseController {

    private final static Logger log = LoggerFactory.getLogger(BaseController.class);
    @Resource
    public HttpServletRequest request;

    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        log.error("统一异常处理：", exception);
        request.setAttribute("ex", exception);
        String xRequestedWith = "X-Requested-With";
        String xmlHttpRequest = "XMLHttpRequest";
        if (null != request.getHeader(xRequestedWith) && request.getHeader(xRequestedWith).equalsIgnoreCase(xmlHttpRequest)) {
            request.setAttribute("requestHeader", "ajax");
        }
        Map<String, Object> result = new HashMap<String, Object>(16);
        // shiro没有权限异常
        if (exception instanceof UnauthorizedException) {
            return BaseResult.newFailure(BaseResultConstant.UNAUTHORIZED_EXCEPTION.getCode(), BaseResultConstant.UNAUTHORIZED_EXCEPTION.getMessage());
        }
        // shiro会话已过期异常
        if (exception instanceof InvalidSessionException) {
            return BaseResult.newFailure(BaseResultConstant.INVALIDSESSION_EXCEPTION.getCode(), BaseResultConstant.INVALIDSESSION_EXCEPTION.getMessage());
        }
        if (exception instanceof MyException) {
            return BaseResult.newFailure(((MyException) exception).getErrorCode(), exception.getMessage());
        }
        // 未知错误异常返回
        String msg = "请求的url：" + request.getRequestURL() + "\n 错误信息：" + exception.getClass() + "\n 错误描述：" + exception.getStackTrace()[0];
        return BaseResult.newFailure(BaseResultConstant.FAILED.getCode(), msg);
    }

    /**
     * 返回防止sql注入的参数
     *
     * @param paramString
     * @return
     */
    public String getPara(String paramString) {
        String str = StringEscapeUtils.escapeSql(request.getParameter(paramString));
        return str;
    }

    /**
     * 接受日期类型参数处理(可以绑定其他参数)
     * 传输空值""时，转换为null
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

}

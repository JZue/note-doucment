package com.jzue.zuul.filter;

import com.jzue.zuul.exception.NullTokenException;
import com.jzue.zuul.utils.TokenUtils;
import com.netflix.ribbon.RequestTemplate;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: junzexue
 * @Date: 2019/4/4 下午4:07
 * @Description:
 **/
@Slf4j
public class PreFilter extends ZuulFilter {

    private static String loginUrl="/msauth/auth/login,/msauth/auth/register";

    static {
        List<String> accessList = Arrays.asList(loginUrl.split(","));
    }
    /**
     * @Description:
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     *     pre：可以在请求被路由之前调用
     *     routing：在路由请求时候被调用
     *     post：在routing和error过滤器之后被调用
     *     error：处理请求时发生错误时被调用
     **/
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     *
     *      filterOrder：通过int值来定义过滤器的执行顺序
     **/
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * shouldFilter：返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。
     * 如果我们直接返回true，所以该过滤器总是生效。
     **/
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * run：过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，不对其进行路由，
     *      然后通过ctx.setResponseStatusCode(401)设置了其返回的错误码，当然我们也可以进一步优化我们的返回，
     *      比如，通过ctx.setResponseBody(body)对返回body内容进行编辑等。
     **/
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        TokenUtils tokenUtils = new TokenUtils();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        log.info(request.getRequestURI());
        if (!request.getRequestURI().equals(loginUrl)&&!request.getRequestURI().equals("/msauth/auth/register")){
            String accessToken = request.getHeader("Authorization");
            if (accessToken == null) {
                log.warn("access token is empty");
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                tokenUtils.checkToken(accessToken);
            }
        }
        return null;
    }
}

package fun.billon.auth.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.HttpResponseUtils;
import fun.billon.common.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 授权相关的拦截器基类,定义了几个模板方法
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractAuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查参数,检查请求头
        if (checkParams(response)
                && checkHeaders(request, response)) {
            // 执行子类处理方法
            return handle(request, response);
        }
        return false;
    }

    /**
     * 设置要检查的参数
     *
     * @return Map 要检查的参数属性名称-->要检查的参数属性值
     */
    protected Map<String, Object> getCheckParams() {
        return null;
    }

    /**
     * 设置要检查的请求头
     *
     * @return Map 要检查的请求头-->请求头不存在时返回的错误码
     */
    protected Map<String, Integer> getCheckHeaders() {
        return null;
    }

    /**
     * 检查参数。从getCheckParams中依次取属性值,判断是否为空
     *
     * @param response HttpServletResponse
     * @return
     * @throws Exception
     */
    private boolean checkParams(HttpServletResponse response) throws Exception {
        Map<String, Object> map = getCheckParams();
        if (null == map || map.size() == 0) {
            return true;
        }
        ResultModel resultModel = new ResultModel();
        for (String key : map.keySet()) {
            if (null == map.get(key)) {
                // 参数未设置
                resultModel.setFailed(CommonStatusCode.PARAM_INVALID, "参数[" + key + "未设置]");
                HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
                return false;
            }
        }
        return true;
    }

    /**
     * 检查请求头。从HttpServletRequest中依次取出头信息,判断是否为空
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return
     * @throws Exception
     */
    private boolean checkHeaders(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Integer> map = getCheckHeaders();
        if (null == map || map.size() == 0) {
            return true;
        }
        ResultModel resultModel = new ResultModel();
        for (String key : map.keySet()) {
            String value = request.getHeader(key);
            if (StringUtils.isEmpty(value)) {
                // 要检查的请求头不存在
                resultModel.setFailed(map.get(key), "无访问权限[" + key + "不存在]");
                HttpResponseUtils.write(response, JSONObject.toJSONString(resultModel));
                return false;
            }
        }
        return true;
    }

    /**
     * 数据处理
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return
     * @throws Exception
     */
    protected abstract boolean handle(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
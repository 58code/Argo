package com.bj58.argo.client;

import com.bj58.argo.Argo;
import com.bj58.argo.controller.ReverseProxy;
import com.bj58.argo.servlet.ArgoRequest;
import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;
import com.google.inject.ImplementedBy;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;

/**
 * 获得客户端信息
 * 包括浏览器端所发送给服务器的所有信息
 *
 * 其中，
 * 1. 通过和nginx/apache等反向代理合作，可以安全获得客户端ip
 *   @see #getAddress()
 *   @see ReverseProxy
 * 2. 可以获得url上的参数即 http://bj.58.com/?paramname=value
 *   @see #queryString(String)
 * 3. form可以获得post的form中的参数
 *   @see #form(String)
 * 4. 方便获得上传文件
 *   @see #getUpload(String)
 *   @see Upload
 *
 * @author renjun
 */
@ImplementedBy(ClientContext.DefaultClientContext.class)
public interface ClientContext {

    /**
     * 获得远程用户cookies
     *
     * @return cookies
     */
    Cookies getCookies();

    /**
     * 得到当前请求的url,不包括参数
     *
     * @return 当前请求的url
     */
    String getRelativeUrl();

    /**
     * 获得url中的参数
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter#encoding(String)
     *
     * @param name 参数名
     * @return url中的参数值
     */
    String queryString(String name);

    /**
     * 获得url中的参数
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter#encoding(String)
     *
     * @param name 参数名
     * @return url中的参数值集合
     */
    Collection<String> queryStrings(String name);

    /**
     * 获得post中的参数
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter#encoding(String)
     *
     * @param name 参数名
     * @return url中的参数值
     */
    String form(String name);

    /**
     * 获得url中的参数
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter#encoding(String)
     *
     * @param name 参数名
     * @return url中的参数值集合
     */
    Collection<String> forms(String name);

    /**
     * 获得url中的参数
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter
     *
     * @return 所有url中的参数集合
     */
    Map<String, Collection<String>> queryStrings();


    /**
     * 获得post中的参数，不包括文件
     * 其中可以通过注入可以安全获得参赛防止 xss或sql注入等
     * @see SafeParameter
     *
     * @return 所有post中的参数集合
     */
    Map<String, Collection<String>> forms();


    /**
     * 获得post中文件
     *
     * @param name 文件名
     *
     * @return 对应文件
     */
    Upload getUpload(String name);


    /**
     * 获得post中所有文件
     *
     * @return 对应文件
     */
    Collection<Upload> getUploads();

    /**
     * 获得用户ip
     *
     * @see ReverseProxy
     *
     * @return 用户ip
     */
    InetAddress getAddress();

    /**
     * 默认的Client实现
     */
    public static class DefaultClientContext implements ClientContext {

        private final ArgoRequest request;
        private final ReverseProxy reverseProxy;

        private Cookies cookies = null;
        private String relativeUrl = null;
        //         private Uploads uploads = null;
        private InetAddress address = null;

        @Inject
        public DefaultClientContext(HttpServletRequest request, ReverseProxy reverseProxy) {
            this.request = (ArgoRequest) request;
            Preconditions.checkNotNull(request);

            this.reverseProxy = reverseProxy;
        }

        @Override
        public Cookies getCookies() {
            if (cookies != null)
                return cookies;

            Cookie[] cks = request.getCookies();
            cookies = Argo.instance.getInstance(Cookies.class);
            for (Cookie ck : cks) {
                cookies.add(ck);
            }
            return cookies;

        }

        @Override
        public String getRelativeUrl() {
            if (relativeUrl != null)
                return relativeUrl;

            String uri = request.getRequestURI();
            String contextPath = request.getContextPath();
            relativeUrl = uri.substring(contextPath.length());

            return relativeUrl;
        }

        @Override
        public String queryString(String name) {
            return request.queryString(name);
        }

        @Override
        public Collection<String> queryStrings(String name) {
            return request.queryStrings().get(name);
        }

        @Override
        public String form(String name) {
            return request.form(name);
        }

        @Override
        public Collection<String> forms(String name) {
            return request.forms().get(name);
        }

        @Override
        public Map<String, Collection<String>> queryStrings() {
            return request.queryStrings();
        }

        @Override
        public Map<String, Collection<String>> forms() {
            return request.forms();
        }

        @Override
        public Upload getUpload(String name) {
            return request.getUpload(name);
        }

        @Override
        public Collection<Upload> getUploads() {
            return request.uploads();
        }

        @Override
        public InetAddress getAddress() {
            if (address != null)
                return address;

            //TODO:synchronized
            address = gerRemoteAddress();
            if (!reverseProxy.isCluster(address))
                return address;

            return reverseProxy.getRemoteAddress(request);
        }


        protected InetAddress gerRemoteAddress() {
            return InetAddresses.forString(request.getRemoteAddr());
        }

    }

}

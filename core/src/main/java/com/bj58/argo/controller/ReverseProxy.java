package com.bj58.argo.controller;

import com.google.common.net.InetAddresses;
import com.google.inject.ImplementedBy;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * 用于集群的反向代理（nginx/apache）后的web网站
 * 用于甄别用户真实的ip，防止ip欺诈
 * @author renjun
 */
@ImplementedBy(ReverseProxy.DefaultReverseProxy.class)
public interface ReverseProxy {

    /**
     * a判断请求的ip是否是当前集群ip
     * @param address 需要判断的ip地址
     * @return true:是反向代理服务器地址; false:不是反向代理服务器地址，用户真实地址
     */
    boolean isCluster(InetAddress address);


    /**
     * 从当前request头中获得用户的ip
     *
     * @param request 当前request
     * @return ip
     */
    InetAddress getRemoteAddress(HttpServletRequest request);


    /**
     * 默认实现，依据用户的ip是否是10.*.*.*，192.168.*.*等判别是否是反向代理服务器
     * 如果是反向代理服务器，则应该重写request的头如x-forwarded-for
     *
     * refer to RFC 1918
     * 10/8 prefix
     * 172.16/12 prefix
     * 192.168/16 prefix
     */
    public static class DefaultReverseProxy implements ReverseProxy {

        @Override
        public boolean isCluster(InetAddress address) {

            return !address.isSiteLocalAddress();

        }

        @Override
        public InetAddress getRemoteAddress(HttpServletRequest request) {

            return InetAddresses.forString(getRemoteAddressByRequest(request));
        }

        protected String getRemoteAddressByRequest(HttpServletRequest request) {
            return request.getHeader("x-forwarded-for");
        }
    }

}

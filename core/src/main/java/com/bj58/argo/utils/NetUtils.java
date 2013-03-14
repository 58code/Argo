package com.bj58.argo.utils;

import com.bj58.argo.ArgoException;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {

    private NetUtils() {}


    /**
     * 将ip地址由文本转换为byte数组
     * @param ipText
     * @return
     */
    public static byte[] getDigitalFromText(String ipText) {
        byte[] ip = IPAddressUtil.textToNumericFormatV4(ipText);

        if (ip != null)
            return ip;

        ip = IPAddressUtil.textToNumericFormatV6(ipText);

        if (ip != null)
            return ip;

        throw ArgoException.raise(new UnknownHostException("[" + ipText + "]"));
    }

    public static InetAddress getInetAddressFromText(String ipText) {
        byte[] ip = getDigitalFromText(ipText);

        try {
            return InetAddress.getByAddress(ip);
        } catch (UnknownHostException e) {
            throw ArgoException.raise(new UnknownHostException("[" + ipText + "]"));
        }
    }
}

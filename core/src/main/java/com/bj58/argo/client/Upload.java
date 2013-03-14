package com.bj58.argo.client;

import javax.servlet.http.Part;

/**
 * 上传文件的封装，继承与servlet3.0的
 * @see Part
 * 并增加了获得文件名的方法
 *
 */
public interface Upload extends Part {

    String getFileName();

}

// 包的命名规则受 GroupConvetionAnnotation.groupPackagesPrefix
package com.bj58.argo.controllers;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;
import com.bj58.argo.annotations.POST;
import com.bj58.argo.annotations.Path;
import com.bj58.argo.client.ClientContext;
import com.bj58.argo.client.Upload;
import com.bj58.argo.controller.AbstractController;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

// 类的命名受 GroupConvetionAnnotation.controllerPattern约束
public class HomeController extends AbstractController {

    @Path("")
    public ActionResult home() {
//        System.out.println(1);
        return view("index"); // velocity模板是 /src/main/java/resources/views/index.html
    }

    @Path("post.html")
    @POST  // 只处理post的请求
    public ActionResult postForm() {

        BeatContext beat = beat();

        ClientContext client = beat.getClient();

        Preconditions.checkState(Strings.isNullOrEmpty(client.form("company")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.form("address")));

        client.queryString("name");

        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("name")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("phone")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("submit")));


        beat.getModel()
                .add("company", client.queryString("company"))
                .add("address", client.queryString("address"))

                .add("name", client.form("name"))
                .add("phone", client.form("phone"))
                .add("submit", client.form("submit"));


        return view("post"); // resources/views/post.html velocity模板

    }

    /**
     *
     * 处理文件上传
     *
     */
    @Path("post-upload.html")
    @POST  // 只处理post的请求
    public ActionResult postUpload() {

        BeatContext beat = beat();

        ClientContext client = beat.getClient();

        Preconditions.checkState(Strings.isNullOrEmpty(client.form("company")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.form("address")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.form("file")));

        client.queryString("name");

        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("name")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("phone")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("submit")));
        Preconditions.checkState(Strings.isNullOrEmpty(client.queryString("file")));


        Upload upload = client.getUpload("file");


        beat.getModel()
                .add("company", client.queryString("company"))
                .add("address", client.queryString("address"))

                .add("name", client.form("name"))
                .add("phone", client.form("phone"))
                .add("submit", client.form("submit"))

                .add("upload", upload);



        return view("post-upload"); // resources/views/post-upload.html velocity模板

    }
}

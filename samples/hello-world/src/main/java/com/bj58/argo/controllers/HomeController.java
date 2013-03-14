// 包的命名规则受 GroupConvetionAnnotation.groupPackagesPrefix
package com.bj58.argo.controllers;

import com.bj58.argo.ActionResult;
import com.bj58.argo.BeatContext;
import com.bj58.argo.annotations.POST;
import com.bj58.argo.annotations.Path;
import com.bj58.argo.client.ClientContext;
import com.bj58.argo.controller.AbstractController;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.sun.jndi.toolkit.url.Uri;

// 类的命名受 GroupConvetionAnnotation.controllerPattern约束
public class HomeController extends AbstractController {

    @Path("/")
    public ActionResult home() {
        Uri uri;
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
}

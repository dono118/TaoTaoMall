package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 生成静态页面Controller
 * @author   Shanks
 * @data     2018年5月4日 下午9:11:36
 * @version  V1.1
 */
@Controller
public class FreemarkerController {

	@Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genHtml")
    @ResponseBody
    public String genHtml() throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map map = new HashMap();
        map.put("hello", "freemarker整合spring测试");
        FileWriter out = new FileWriter(new File("D:/www/html/out.html"));
        template.process(map, out);
        out.close();
        return "ok";
    }
}

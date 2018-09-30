package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.taotao.utils.FastDFSClient;

/**
 * 图片上传Controller
 * @author   Shanks
 * @data     2018年4月30日 下午11:09:19
 * @version  V1.1
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		Map result = new HashMap<>();
		try {
			// 1.接收上传的文件
			// 2.获取扩展名
			String originalName = uploadFile.getOriginalFilename();
			String extName = originalName.substring(originalName.lastIndexOf(".") + 1);
			// 3.上传到图片服务器
			FastDFSClient client = new FastDFSClient("classpath:resource/fdfs_client.conf");
			String url = client.uploadFile(uploadFile.getBytes(), extName);
			// 拼接图片服务器的ip地址或域名
			url = IMAGE_SERVER_URL + url;
			result.put("error", 0);
			result.put("url", url);
			return JSON.toJSONString(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("error", 1);
			result.put("message", "上传图片失败!");
			return JSON.toJSONString(result);
		}
	}
}

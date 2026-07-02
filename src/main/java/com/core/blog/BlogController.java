package com.core.blog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.core.common.CommonUtils;

@RestController
@RequestMapping("blog")
public class BlogController {
	@Value("${app.company.images}")
	String imagePath;

	@Value("${server.servlet.context-path}")
	String contextPath;
	
	@Autowired
	BlogService postService;

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "{id}", method = RequestMethod.PATCH)
	public void patchPost(@PathVariable("id") long id) {
		postService.patchPost(id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "{id}/comments/{cid}", method = RequestMethod.PATCH)
	public void patchComment(@PathVariable("id") long id, @PathVariable("cid") long cid) {
		postService.patchComment(id, cid);
	}

	@ResponseBody
	@GetMapping("images/{imageId}.jpg")
	public void getPostImage(HttpServletResponse response, @PathVariable("imageId") String imageId) {
		try {
			InputStream is = new FileInputStream(imagePath + "blogs/" + imageId);
			CommonUtils.sendImageResponse(response, is);
		} catch (FileNotFoundException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "images", method = RequestMethod.POST)
	public BlogImageResponse getSaveUserImage(@RequestParam("upload") MultipartFile imgfile) throws Exception {
		String url = UUID.randomUUID().toString();
		IOUtils.copy(imgfile.getInputStream(), new FileOutputStream(imagePath + "blogs/" + url));

		BlogImageResponse response = new BlogImageResponse();
		response.setUrl(contextPath + "/blog/images/" + url + ".jpg");
		response.setUploaded(1);
		response.setFileName(url + ".jpg");
		return response;
	}

}

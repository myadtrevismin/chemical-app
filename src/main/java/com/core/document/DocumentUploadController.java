package com.core.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.core.blog.BlogImageResponse;
import com.core.common.CommonUtils;
import com.core.document.service.Document;
import com.core.document.service.DocumentService;

@RequestMapping("docs")
@RestController
@PreAuthorize("isFullyAuthenticated()")
public class DocumentUploadController {
	@Value("${app.company.images}")
	String imagePath;

	@Autowired
	DocumentService docService;

	@PostMapping(path = "upload")
	public BlogImageResponse uploadDoc(HttpServletRequest request, @RequestParam("file") MultipartFile imgfile,
			@RequestParam("from") String from) {
		BlogImageResponse response = new BlogImageResponse();
		try {
			String url = UUID.randomUUID().toString();
			Document doc = new Document();
			doc.setFileFrom(from);
			doc.setFileType(request.getParameter("fileType"));
			doc.setParent(Long.parseLong(request.getParameter("parent")));

			if (request.getParameter("expiryDate") != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				doc.setExpiryDate(format.parse(request.getParameter("expiryDate")));
			}

			doc.setFileName(imgfile.getOriginalFilename());
			doc.setFileRef(url);
			Long id = docService.saveFile(doc);
			IOUtils.copy(imgfile.getInputStream(), new FileOutputStream(imagePath + "docs/" + url));

			response.setUrl(id + "");
			response.setUploaded(1);
			response.setFileName(url + ".jpg");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setUploaded(0);
		return response;
	}

	@ResponseBody
	@GetMapping("/{docId}")
	public void getDc(HttpServletResponse response, @PathVariable("docId") Long docId) {
		try {

			Document doc = docService.getDocument(docId);

			Path path = new File(doc.getFileName()).toPath();
			String mimeType = Files.probeContentType(path);
			response.setContentType(mimeType);
			response.addHeader("Content-Disposition", "attachment; filename=" + doc.getFileName());

			InputStream is = new FileInputStream(imagePath + "docs/" + doc.getFileRef());
			CommonUtils.sendImageResponse(response, is);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	@ResponseBody
	@GetMapping("/ref/{docId}/{refType}")
	public void getRefDoc(HttpServletResponse response, @PathVariable("docId") Long docId,@PathVariable("refType")  String refType) {
		try {

			Document doc = docService.getDocumentByRefId(docId,refType);

			Path path = new File(doc.getFileName()).toPath();
			String mimeType = Files.probeContentType(path);
			response.setContentType(mimeType);
			response.addHeader("Content-Disposition", "attachment; filename=" + doc.getFileName());

			InputStream is = new FileInputStream(imagePath + "docs/" + doc.getFileRef());
			CommonUtils.sendImageResponse(response, is);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}

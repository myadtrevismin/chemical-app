package com.core.blog;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.core.common.CommonUtils;

@Component
@RepositoryEventHandler(Blog.class)
public class BlogListener {
	@Value("${app.company.images}")
	String imagePath;

	@HandleBeforeSave
	public void validateBeforeSave(Blog post) {
		prePersist(post);
	}

	@HandleBeforeCreate
	public void validateBeforeCreate(Blog post) {
		prePersist(post);
	}

	@HandleAfterSave
	public void validateAfterSave(Blog post) {
		saveImage(post);
	}

	@HandleAfterCreate
	public void validateAfterCreate(Blog post) {
		saveImage(post);
	}
	
	private void prePersist(Blog post) {
		post.setDescription(CommonUtils.getDescriptionFromPost(post.getContent()));
		post.setVideoUrl(CommonUtils.updateYoutubeEmbed(post.getVideoUrl()));
		updateImagePresence(post);
	}
	
	private void updateImagePresence(Blog post) {
		if(post.getImage() != null) {
			post.setImagePresence(true);
		} else {
			post.setImagePresence(false);
		}
	}

	private void saveImage(Blog post) {
		if (post.getImage() != null) {
			String partSeparator = ",";
			String encodedImg = post.getImage().split(partSeparator)[1];
			byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));

			try {
				FileUtils.writeByteArrayToFile(new File(imagePath + "blogs/" + post.getId()), decodedImg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

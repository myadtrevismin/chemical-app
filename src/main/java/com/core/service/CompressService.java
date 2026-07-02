package com.core.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CompressService {
	@Value("${app.company.images}")
	String imagePath;
	
	public void compressImage(String input, String output) {
		try {
			File source = new File(imagePath + input);
			File dest = new File(imagePath + output);

			long kbytes = source.length() / 1024;

			if (kbytes < 100) {
				FileUtils.copyFile(source, dest);
			} else {
				float factor = 0.2f;
				if (kbytes > 512) {
					factor = 0.1f;
				} else if (kbytes < 100) {
					factor = 1f;
				}

				BufferedImage image = ImageIO.read(source);
				OutputStream os = new FileOutputStream(dest);

				Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
				ImageWriter writer = (ImageWriter) writers.next();

				ImageOutputStream ios = ImageIO.createImageOutputStream(os);
				writer.setOutput(ios);

				ImageWriteParam param = writer.getDefaultWriteParam();

				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(factor); // Change the quality value you prefer
				writer.write(null, new IIOImage(image, null, null), param);

				os.close();
				ios.close();
				writer.dispose();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

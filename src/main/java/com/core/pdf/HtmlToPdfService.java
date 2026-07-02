package com.core.pdf;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

@Service
public class HtmlToPdfService {
	@Value("${app.company.images}")
	private String templateImagePath;

	/* 
	 * Styling RULES
	 * **************
	 * 1. border styles can only be applied to <td> element.
	 * 2. avoid page break should be used with <div> element.
	 * 
	 * */
	
	public void htmlToPdf(String html, OutputStream os) throws Exception {

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, os);
		document.open();

		CSSResolver cssResolver = new StyleAttrCSSResolver();
		InputStream csspathtest = new FileInputStream(templateImagePath + "pdf/template.css");
		CssFile cssfiletest = XMLWorkerHelper.getCSS(csspathtest);
		cssResolver.addCss(cssfiletest);

		HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		htmlContext.setImageProvider(new AbstractImageProvider() {
			public String getImageRootPath() {
				return templateImagePath + "pdf";
			}
		});
		/*
		 * htmlContext.setLinkProvider(new LinkProvider() { public String getLinkRoot()
		 * { return RELATIVE_PATH; } });
		 */

		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, htmlPipeline);
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(IOUtils.toInputStream(html, "UTF-8"));

		// step 5
		document.close();
	}
}

package com.core.document.service;

import java.util.List;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentService {
	@Autowired
	EntityManager em;

	@Transactional
	public Long saveFile(Document doc) {
		em.createQuery("update Document set active=false where parent=" + doc.getParent() + " and fileType='"
				+ doc.getFileType() + "' and fileFrom='" + doc.getFileFrom() + "'").executeUpdate();
		doc.setActive(true);

		em.persist(doc);

		return doc.getId();
	}

	public Document getDocument(Long docId) {
		return em.find(Document.class, docId);
	}

	public Document getDocumentByRefId(Long parent, String fileFrom) {
		List<Document> doc = em.createQuery("select d from Document d where d.fileType=:fileType and d.parent=:parent")
				.setParameter("parent", parent).setParameter("fileType", fileFrom).getResultList();
		if (doc.size() > 0) {
			return doc.get(0);
		} else {
			return null;
		}
	}
}

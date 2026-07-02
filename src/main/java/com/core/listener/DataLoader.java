package com.core.listener;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.core.common.CommonDao;
import com.core.common.CommonData;

@Component
public class DataLoader {
	@Autowired
	CommonDao commonDao;
	
	@Value("${app.company.images}")
	String imagePath;

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		System.out.println("********************************************************************");
		System.out.println("Loading Data into Cache");
		System.out.println("********************************************************************");
		long t = System.currentTimeMillis();

		CommonData.imagePath = imagePath;
		
		t = System.currentTimeMillis() - t;
		System.out.println("\n********************************************************************");
		System.out.printf("Data loaded into Cache in %1d sec ", t / 1000);
		System.out.println("\n********************************************************************");
	}

}

package com.core.contact;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.core.common.CommonUtils;
import com.core.common.SystemException;

@Service
public class ContactService {
	
	@Autowired
	ContactRepo contactRepo;

	public void uploadContacts(MultipartFile file, Long gid) throws Exception {
		List<Contact> lis = null;
		
		String fileName = file.getOriginalFilename();
		if (fileName.endsWith("csv")) {
			lis = getContactsFromCsv(file, gid);
		} else if (fileName.endsWith("xls") || fileName.endsWith("xlsx")) {
			lis = getContactsFromExcel(file, gid);
		} else {
			throw new SystemException("Invalid File format");
		}
		
		if(CommonUtils.isNotNullOrEmpty(lis)) {
			contactRepo.saveAll(lis);
		}
	}

	private List<Contact> getContactsFromCsv(MultipartFile file, long gid) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		List<Contact> lis = new ArrayList<Contact>();
		String line = "";
		
		ContactGroup cg = new ContactGroup();
		cg.setId(gid);
		long uid = CommonUtils.getUserIdFromSession();
		
		reader.readLine();
		while ((line = reader.readLine()) != null) {
			String text[] = line.split(",");

			if (CommonUtils.validPhone(text[1])) {
				Contact contact = new Contact();
				contact.setUid(uid);
				contact.setGroup(cg);
				contact.setFirstName(text[0]);
				contact.setMobile(CommonUtils.convertPhoneToRequiredFormat(text[1]));
				contact.setEmail(text[2]);
				lis.add(contact);
			}
		}

		return lis;
	}

	private List<Contact> getContactsFromExcel(MultipartFile file, long gid) throws Exception {
		String fileName = file.getOriginalFilename();
		
		Workbook workbook = null;
		if (fileName.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		} else if (fileName.endsWith("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}

		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowItr = sheet.iterator();
		
		long uid = CommonUtils.getUserIdFromSession();
		List<Contact> lis = new ArrayList<Contact>();
		ContactGroup cg = new ContactGroup();
		cg.setId(gid);
		
		rowItr.next();
		while (rowItr.hasNext()) {
			Row r = rowItr.next();
			
			Cell cell = r.getCell(1);
			String phone = null;
			
			if (cell.getCellType() == CellType.NUMERIC) {
				phone = (long)cell.getNumericCellValue() + "";
			} else if (cell.getCellType() == CellType.STRING) {
				phone = Long.parseLong(cell.getStringCellValue()) + "";
			}
			
			if (CommonUtils.validPhone(phone)) {
				Contact contact = new Contact();
				contact.setUid(uid);
				contact.setGroup(cg);
				contact.setFirstName(r.getCell(0).getStringCellValue());
				contact.setMobile(CommonUtils.convertPhoneToRequiredFormat(phone));
				contact.setEmail(r.getCell(2).getStringCellValue());
				lis.add(contact);
			}			
		}
		
		return lis;
	}

}
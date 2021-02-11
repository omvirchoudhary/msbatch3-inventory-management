package com.sl.ms.inventorymanagement.service;

import com.sl.ms.inventorymanagement.product.ProductFileRepository;
import com.sl.ms.inventorymanagement.product.ProductUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class ProductFileUploadService {
	
	@Autowired
	  private ProductFileRepository prodfilerepo;

	  public ProductUpload store(MultipartFile file) throws IOException {
	    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	    ProductUpload pro = new ProductUpload(fileName, file.getContentType(), file.getBytes());
	    return prodfilerepo.save(pro);
	  }

	  public ProductUpload getFile(String id) {
	    return prodfilerepo.findById(id).get();
	  }
	  
	  public Stream<ProductUpload> getAllFiles() {
	    return prodfilerepo.findAll().stream();
	  }
	}



package com.sl.ms.inventorymanagement.service;

import com.sl.ms.inventorymanagement.product.Product;
import com.sl.ms.inventorymanagement.product.ProductController;
import com.sl.ms.inventorymanagement.product.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	ProductRepository productRepository;

	public List<Product> save(List<Product> product) {
		return productRepository.saveAll(product);
	}
	
	public Product save(Product product) {
		return productRepository.save(product);
	}


	public void delete(Long id) {
		
		 productRepository.deleteById(id);
	}

	public Optional<Product> getById(Long id) {
		return productRepository.findById(id);
	}

	
	@Cacheable("Products")
	public Object[] findSupportedProducts() {
		try  {
            Thread.sleep(1000*5);
        } 
        catch (InterruptedException e)  {
            e.printStackTrace();
        }
		return productRepository.findSupportedProducts();
	}

}

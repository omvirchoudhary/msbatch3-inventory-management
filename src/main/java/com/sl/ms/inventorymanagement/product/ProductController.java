package com.sl.ms.inventorymanagement.product;

import com.sl.ms.inventorymanagement.inventory.Inventory;
import com.sl.ms.inventorymanagement.inventory.InventoryRepository;
import com.sl.ms.inventorymanagement.service.ProductFileUploadService;
import com.sl.ms.inventorymanagement.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;



@RestController
public class ProductController {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
    ProductService productservice;
	
	@Autowired
	ProductFileUploadService productFileUploadService;
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class.getName());

	//1
	@GetMapping("/products")
	public List<Product> getProducts(){
		log.info("Fetching all product details");
		List<Product> product = (List<Product>) productRepository.findAll();
		return product;
	}
	//2
	@RequestMapping("/products/{Id}")
	public Optional<Product> getProductsById(@PathVariable("Id") Long Id) throws RuntimeException{
		log.info("Fetching product details of Id: "+ Id);
		Optional<Product> product = productRepository.findById(Id);
		if (!product.isPresent())
			throw new RuntimeException("Product with "+Id + " not available in inventoryId");
		else {
			return product;
		}
	}
	//3
	@PostMapping(path = "/products/{Id}", consumes = {"application/json"})
	@ResponseBody
	public ResponseEntity<Object> createProductsForInv(@PathVariable ("Id") Long Id, @RequestBody Product product) throws RuntimeException{
		log.info("Fetching product details of Id: "+ Id);
		Optional<Inventory> inv = inventoryRepository.findById(Id);
		if(!inv.isPresent()) {
			throw new RuntimeException("Product with "+Id + " not available in inventoryId");
		}else {
			Inventory inventory = inv.get();
			((Product) product).setInventory(inventory);
			productRepository.save(product);
		}
		return new ResponseEntity<Object>(product, HttpStatus.OK);
	}
	//4
	@PostMapping(path = "/products", consumes = {"application/json"})
	@ResponseBody
	public ResponseEntity<List<Product>> saveProducts(@RequestBody List<Product> product) {
		log.info("Saving Products: ");
		productservice.save(product);
		return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
	}
	//5
	@PostMapping("product/file")
	public ResponseEntity<String> uploadFileObject(@RequestParam("file") MultipartFile file) {
		log.info("Inside File upload for creating products");
		String message = "";
		try {
			productFileUploadService.store(file);
			message = "Uploaded successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(message);
		} catch (Exception e) {
			message = "Upload fails for file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}
	}
	//6
	@PutMapping(path ="/products/{Id}" , consumes = {"application/json"})
	@ResponseBody
	private ResponseEntity<Object> updateProduct(@PathVariable ("Id") Long Id, @RequestBody Product product) throws RuntimeException{
		log.info("Updating Product details using PUT mapping");
		Optional<Inventory> inv = inventoryRepository.findById(Id);
		Optional<Product> pro = productRepository.findById(Id);
		if(!pro.isPresent()) {
			throw new RuntimeException("Product with "+Id + " not available in inventoryId");
		}else {
			Inventory inventory = inv.get();
			((Product) product).setInventory(inventory);
			productRepository.save(product);
		}
		return new ResponseEntity<Object>(product, HttpStatus.OK);
	}
	//7
	@DeleteMapping("/products/{id}")
	private Optional<Product> deleteProductsById(@PathVariable("id") Long id) throws RuntimeException{
		log.info("Delete Products for Id: "+id);
		Optional<Product> pro = productRepository.findById(id);
		if(!pro.isPresent()) {
			throw new RuntimeException("Product with "+id + " not available in inventoryId");
		}else {
			Optional<Product> delete = productservice.getById(id);
			productservice.delete(id);
			return delete;
		}
	}

}

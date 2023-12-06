package com.example.OnlineShop.Objects.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.OnlineShop.Database.Dtos.ProductDto;
import com.example.OnlineShop.Database.Dtos.ProductLessDto;
import com.example.OnlineShop.Database.Models.ProductModel;
import com.example.OnlineShop.Database.Repositories.ProductRepository;
import com.example.OnlineShop.Objects.Services.ProductService;
import com.example.OnlineShop.Other.Filter;

@Service
public class Product implements ProductService{

	private ProductRepository productRepo;
	
	@Autowired
	public Product(ProductRepository productRepo) {
		this.productRepo=productRepo;
	}
	
	@Override
	public ProductDto getById(Long id) {
		ProductModel product=productRepo.findById(id).orElse(null);
		ProductDto productDto =null;
		
		if (product!=null) {
			productDto=new ProductDto(product.getId(),product.getName(),product.getDescription(),
							product.getAmountInStock(),product.getPrice(),product.getSeller(),
							product.getCategories(),product.getImages(),product.getReviews());
		}
		return productDto;
	}

	@Override
	public  List<ProductLessDto> getByCategoryandFilter(Filter filter) {
		
		
		 String name = (filter.getName()!=null)? "%"+filter.getName()+"%": "%";
		 String category = (filter.getCategory()!=null)? filter.getCategory(): "%";
		 double minPrice =(filter.getMinPrice()!=null)?filter.getMinPrice():0;
		 double maxPrice =(filter.getMaxPrice()!=null)?filter.getMaxPrice():Double.MAX_VALUE;
		 
		 int offset=filter.getOffset();
		 int limit=filter.getLimit();
		 Pageable pageable = PageRequest.of(offset, limit);
		 
		 List<ProductLessDto> filteredProducts=productRepo.findDtoByFilter(name,category,minPrice,maxPrice,pageable);
		
		return filteredProducts;
	}

	@Override
	public  List<ProductLessDto> getByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductLessDto> getPopular() {
		//random for now
		Random generator = new Random();
		List<Long> popular = new ArrayList<Long>();
		for(int i=0; i<12; i++) {
				popular.add(generator.nextLong(1,55));
			}
		
		 List<ProductLessDto> products = productRepo.findAllDtosByIdIn(popular);
		
		return products ;
	}

	
}
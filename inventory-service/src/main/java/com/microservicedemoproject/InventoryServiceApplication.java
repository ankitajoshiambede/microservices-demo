package com.microservicedemoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

/*	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("Iphone 13");
			inventory.setQuantity(100);
			inventoryRepository.save(inventory);

			inventory.setSkuCode("Iphone 17");
			inventory.setQuantity(200);
			inventoryRepository.save(inventory);
		};
	}*/

}

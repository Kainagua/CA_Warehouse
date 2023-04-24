package com.vin.WareHouse;

import com.vin.WareHouse.grpc.JmHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;

@SpringBootApplication()
@EnableJpaRepositories()
public class WareHouseApplication {

	@Autowired
	JmHelper jmHelper;

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(WareHouseApplication.class, args);
		/*Server server= ServerBuilder.forPort(9090)
				.addService(new InventoryImpl())
				.build();
		server.start().awaitTermination();*/

	}



}

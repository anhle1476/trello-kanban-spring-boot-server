package com.codegym.kanban;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TrelloKanbanApplication {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
//	@Bean
//	public ObjectMapper objectMapper() {
//		return new ObjectMapper();
//	}

	public static void main(String[] args) {
		SpringApplication.run(TrelloKanbanApplication.class, args);
	}

}

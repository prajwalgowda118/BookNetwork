package com.scaler.booknetwork.booknetwork;

import com.scaler.booknetwork.booknetwork.Models.Role;
import com.scaler.booknetwork.booknetwork.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApplication.class, args);
    }
    @Bean

    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {

        return args -> {
           if(roleRepository.findByName("USER").isEmpty()){
               Role role = new Role();
               role.setName("USER");
               roleRepository.save(role);

           }
        };
    }
}

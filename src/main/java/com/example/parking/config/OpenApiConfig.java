package com.example.parking.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration public class OpenApiConfig {
 @Bean public OpenAPI parkingOpenAPI(){return new OpenAPI().info(new Info().title("Smart Parking Management API").version("1.0.0").description("REST API for parking slots, bookings, checkout, billing and daily revenue."));}
}

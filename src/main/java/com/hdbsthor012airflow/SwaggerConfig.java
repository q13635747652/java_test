package com.hdbsthor012airflow;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// http://localhost:60011/swagger-ui/index.html#/
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerOpenApi() {
        return new OpenAPI()
                .info(new Info().title("hdbsthor-011-third-logistics")
                        .description("对象服务")
                        .version("v1.0.0"));
    }
}

package springboot.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * info URL: https://jaxenter.com/spring-boot-tutorial-rest-services-and-microservices-135148.html
 * 
 */

@Configuration
@EnableSwagger2
//@EnableWebMvc
public class SwaggerCorsConfig
	extends WebMvcConfigurationSupport 
{
	
	public static final String lineSeparator = System.getProperties().getProperty("line.separator");
	
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    	System.out.println("did the Resource Registry");
    	
        registry.addResourceHandler("/swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        registry.addResourceHandler("/h2-console/**")
        	.addResourceLocations("classpath:/META-INF/resources/");
    } 
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {

    	System.out.println("Set Up Cors for Angular");
    	// Angular Testing from AngularIDE or localhost(manual from a Browser)
    	// Support for CRUD
    	
        registry.addMapping("/rest/api/**")
            .allowedOrigins("http://localhost:4200", "http://localhost:8080")
            .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Accept", "Content-Type", "Authorization")
            .allowedMethods("POST", "OPTIONS", "GET", "DELETE", "PUT", "PATCH")
     		.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
     		.allowCredentials(false);  

    	System.out.println("Set Up Cors for Swagger");
    	
    	// Mapping for Swagger Testing
    	// Support for CRUD
    	
        registry.addMapping("/swagger-ui.html")
        .allowedOrigins("http://localhost:8080")
//        .allowedOrigins("*")
        .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Access-Control-Allow-Headers", "Accept", "Content-Type", "Vary", "Authorization", "api-key")
//        .allowedHeaders("*")
        .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
//    	.allowedMethods("*")
     	.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
    	.allowCredentials(false);  
    	
    	// Mapping for H2 Console
        registry.addMapping("/h2-console/**")
        	.allowedOrigins("http://localhost:8080")
//            .allowedOrigins("*")
            .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Access-Control-Allow-Headers", "Accept", "Content-Type", "Vary")
        	.allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS")
         	.exposedHeaders("Content-Type", "Content-Range", "Access-Control-Allow-Origin")   // headers for the response does not support wildcards
        	.allowCredentials(false);  
        
        // Add more mappings...
    }
	
    
    @Bean
    public Docket api() {
    	
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("springboot.controllers.rest"))
//            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
//            .paths(PathSelectors.regex("/user.*|/register.*|/oauth/token.*"))
            .build().apiInfo(metaData());
    }
 
    private ApiInfo metaData()
    {
    	Contact aContact = new Contact(
                "Joe Fraser", //name
                "https://www.linkedin.com/in/joe-fraser-333127b/", // url: linkedin, facebook
                "jfraser2@yahoo.com"); // email
    	
    	String descriptionText = "Request and Response format is JSON, Request validation is: javax validation enhanced with a Framework." + lineSeparator
    			+ "Since there are so many ways to do Security. I choose a simple one for this project." + lineSeparator
    			+ "The user must put a JWT Token in the request header or in a request cookie (will make the AngularJS folks happy)." + lineSeparator
    			+ "JWT Tokens copied directly from the site http://jwt.io may not work. Why you ask?" + lineSeparator
    			+ "On the Windows Platform the Character set gets changed, it is not longer UTF-8, it becomes Cp1252 (Eclipse Project Default)." + lineSeparator
    			+ "No worries, a Junit Test was made to build Tokens. It is called BuildJwtToken. For Swagger usage, copy the token from the Eclipse project Console instead." + lineSeparator
				+ "The Secret to making the JUnit produce a UTF-8 token is to set the Windows Environment Variable JAVA_TOOL_OPTIONS to value -Dfile.encoding=UTF-8"; 
    	
        return new ApiInfoBuilder()
                .title("Available REST Services")
                .description(descriptionText)
                .version("1.0.0")
                .license("Apache License Version 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .contact(aContact)
                .build();
    }
    
    public ApiInfo apiInfo()
    {
        final ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("Swagger Test App").version("1.0").license("(C) Copyright")
        .description("The API provides a platform to Register a User");

        return builder.build();
    }
    
}

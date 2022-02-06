package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = { "curso.api.rest.model" })
@ComponentScan(basePackages = { "curso.*" })
@EnableJpaRepositories(basePackages = { "curso.api.rest.repository" })
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
		// System.out.println(new BCryptPasswordEncoder().encode("123"));
		/*
		 * 
		 * #spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.
		 * DataSourceAutoConfiguration #configuracoes hikari #numero maximo de
		 * milissegundos que um cliente aguardara por uma conexão
		 * #spring.datasource.hikari.connection-timeout=2000 #numero mínimo de conexões
		 * inativas mantidas pelo HirakiCP em um conjunto de conexoes
		 * #spring.datasource.hikari.minimum-idle= 10 #maximo de pool de conexão
		 * #spring.datasource.hikari.maximum-pool-size= 40 #tempo ocioso para conexão
		 * #spring.datasource.hikari.idle-timeout= 10000 #salvando dados no banco
		 * automaticamente #spring.datasource.hikari.auto-commit= true
		 */
	}

	// Mapeamento global que refletem em todo o sistema
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// liberando apenas requisições post para o usuário do servidor
		registry.addMapping("/usuario/**").allowedMethods("*").allowedOriginPatterns("*");

	}

}

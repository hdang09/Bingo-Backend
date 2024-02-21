package hdang09;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BingoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BingoApplication.class, args);
	}
}

package kr.or.kids;

import kr.or.kids.global.config._FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(_FileProperties.class)
public class KidsApplication {
	public static void main(String[] args) {
		SpringApplication.run(KidsApplication.class, args);
	}

}

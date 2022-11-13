package KMC.KoreaMusicChart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KoreaMusicChartApplication {

	public static void main(String[] args) {
		SpringApplication.run(KoreaMusicChartApplication.class, args);
	}
}

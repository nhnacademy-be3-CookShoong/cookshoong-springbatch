package store.cookshoong.www.cookshoongspringbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Cookshoong springbatch application.
 */

@EnableScheduling
@SpringBootApplication
@EnableDiscoveryClient
@EnableBatchProcessing // spring batch module infra structure 을 구성.
@ConfigurationPropertiesScan
public class CookshoongSpringbatchApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CookshoongSpringbatchApplication.class, args);
    }

}

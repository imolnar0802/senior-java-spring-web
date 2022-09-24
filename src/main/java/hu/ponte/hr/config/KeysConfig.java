package hu.ponte.hr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "sign.certs")
public class KeysConfig {

    private String publicKeyPath;

    private String privateKeyPath;
}

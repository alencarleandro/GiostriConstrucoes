package GiostriConstrucoes.dev.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfiguracao {

    @PostConstruct
    public void init(){
        MercadoPagoConfig.setAccessToken("APP_USR-963007695352846-051523-9b0c7f725046c3b54f559ee03eba5dd1-339266158");
    }
}



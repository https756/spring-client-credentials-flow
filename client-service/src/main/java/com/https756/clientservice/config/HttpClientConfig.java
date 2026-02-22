package com.https756.clientservice.config;

import com.https756.clientservice.client.OrderClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration(proxyBeanMethods = false)
@ImportHttpServices(group = "orders", types = {OrderClient.class})
public class HttpClientConfig {

}

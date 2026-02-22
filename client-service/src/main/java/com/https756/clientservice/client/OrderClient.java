package com.https756.clientservice.client;

import com.https756.clientservice.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.annotation.ClientRegistrationId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "http://ms-resource-service:8081/orders", accept = "application/json")
@ClientRegistrationId("client-service")
public interface OrderClient {

    @GetExchange
    ResponseEntity<List<OrderDto>> getAllOrders();

    @GetExchange("/{id}")
    ResponseEntity<OrderDto> getOrderById(@PathVariable String id);

}

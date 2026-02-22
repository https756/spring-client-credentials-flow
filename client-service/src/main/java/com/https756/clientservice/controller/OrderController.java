package com.https756.clientservice.controller;

import com.https756.clientservice.client.OrderClient;
import com.https756.clientservice.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderClient orderClient;

    public OrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        List<OrderDto> orderDtoList = orderClient.getAllOrders().getBody();

        Map<String, Object> response = Map.of(
                "orders", orderDtoList != null ? orderDtoList : "",
                "status", "VERIFIED_BY_CLIENT_SERVICE"
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable String id) {
        OrderDto orderDto = orderClient.getOrderById(id).getBody();

        Map<String, Object> response = Map.of(
                "orders", orderDto != null ? orderDto : "",
                "status", "VERIFIED_BY_CLIENT_SERVICE"
        );

        return ResponseEntity.ok(response);
    }

}

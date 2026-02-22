package com.https756.resourceservice.controller;

import com.https756.resourceservice.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final List<OrderDto> orderDtoList = List.of(
            new OrderDto(1, "MacBook Pro M3", 2500),
            new OrderDto(2, "Dell XPS-15", 2300),
            new OrderDto(3, "Lenovo ThinkPad X1-Carbon", 2000)
    );

    @GetMapping
    @PreAuthorize("hasRole('get-access')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderDtoList);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('get-access')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(orderDtoList.get(id - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

package com.example.order.controller;

import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import com.example.order.repositories.OrderItemRepository;
import com.example.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;


    @PostMapping("/order")
    public ResponseEntity<Order> createNewOrder(@RequestBody Order orderBody){
        Order order = orderRepository.save(orderBody);

        if (order.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new order");

        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }

    @PostMapping("/orderItem")
    public ResponseEntity<List<OrderItem>> createNewOrderItems(@RequestBody List<OrderItem> orderItemsBody){

        List<OrderItem> orderItem = orderItemRepository.saveAll(orderItemsBody);

        if (orderItem.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new orderItems");

        return new ResponseEntity<List<OrderItem>>(orderItem, HttpStatus.CREATED);
    }



}

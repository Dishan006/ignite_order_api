package com.dishanm.ignite.order_api.controllers;

import com.dishanm.ignite.order_api.Exceptions.OutOfStockException;
import com.dishanm.ignite.order_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.order_api.model.Order;
import com.dishanm.ignite.order_api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/order_api/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public List<Order> getAllItems() {
        return orderService.getAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderByID(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
        return orderService.getOrderByID(id);
    }

    @PostMapping("/orders")
    public Order createOrder(@Valid @RequestBody Order newOrder) throws ResourceNotFoundException, OutOfStockException {
        return orderService.addOrder(newOrder);
    }

}

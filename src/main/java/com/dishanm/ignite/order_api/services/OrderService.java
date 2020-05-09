package com.dishanm.ignite.order_api.services;

import com.dishanm.ignite.order_api.Exceptions.OutOfStockException;
import com.dishanm.ignite.order_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.order_api.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;


public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderByID(long id) throws ResourceNotFoundException;

    Order addOrder(Order order) throws ResourceNotFoundException, OutOfStockException;
}

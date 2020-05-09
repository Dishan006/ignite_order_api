package com.dishanm.ignite.order_api.services.impl;


import com.dishanm.ignite.order_api.Exceptions.OutOfStockException;
import com.dishanm.ignite.order_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.order_api.model.LineItem;
import com.dishanm.ignite.order_api.model.Order;
import com.dishanm.ignite.order_api.repositories.OrderRepository;
import com.dishanm.ignite.order_api.services.IgniteCacheService;
import com.dishanm.ignite.order_api.services.OrderService;
import dishanm.ignite.beans.Item;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IgniteCacheService igniteCacheService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderByID(long id) throws ResourceNotFoundException {
        Order order = orderRepository.findById(id).orElse(null);

        if (order != null) {
            return order;
        }

        throw new ResourceNotFoundException("Order not found id:" + id);
    }

    @Override
    public Order addOrder(Order order) throws ResourceNotFoundException, OutOfStockException {

        Map<Item, Integer> items = new HashMap<>();
        if (order.getLineItems() != null) {
            double total = 0.0;
            for (LineItem lineItem : order.getLineItems()) {

                Item cachedItem = igniteCacheService.getItemFromName(lineItem.getItemName());
                if (cachedItem.getStockCount() < lineItem.getItemCount()) {
                    throw new OutOfStockException("Item out of stock: " + cachedItem.getName());
                }

                total += cachedItem.getPrice() * lineItem.getItemCount();
                items.put(cachedItem, lineItem.getItemCount());
            }
            order.setTotal(total);
        }

        log.debug("Updated Order: " + order);
        igniteCacheService.updateStockCount(items);
        return orderRepository.save(order);

    }
}

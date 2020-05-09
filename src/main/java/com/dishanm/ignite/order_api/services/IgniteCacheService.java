package com.dishanm.ignite.order_api.services;

import com.dishanm.ignite.order_api.Exceptions.OutOfStockException;
import com.dishanm.ignite.order_api.Exceptions.ResourceNotFoundException;
import dishanm.ignite.beans.Item;

import java.util.List;
import java.util.Map;


public interface IgniteCacheService {
    Item getItemFromName(String itemName) throws ResourceNotFoundException;
    void updateStockCount(Map<Item, Integer>items) throws OutOfStockException;
}

package com.dishanm.ignite.order_api.services.impl;

import com.dishanm.ignite.order_api.Exceptions.OutOfStockException;
import com.dishanm.ignite.order_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.order_api.services.IgniteCacheService;
import dishanm.ignite.Constants;
import dishanm.ignite.beans.Item;
import lombok.extern.log4j.Log4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Log4j
public class IgniteCacheServiceImpl implements IgniteCacheService {

    @Autowired
    private Ignite ignite;

    @Override
    public Item getItemFromName(String itemName) throws ResourceNotFoundException {
        IgniteCache<Long, Item> cache = ignite.cache(Constants.ITEM_CACHE_NAME);
        log.debug("Cache size:" + cache.size());

        SqlFieldsQuery sql = new SqlFieldsQuery("SELECT id,price,stockCount FROM item WHERE name = ?").setArgs(itemName);

        try (QueryCursor<List<?>> cursor = cache.query(sql)) {
            for (List<?> row : cursor) {
                Object idObject = row.get(0);

                if (idObject instanceof Number) {
                    Item item = new Item();
                    item.setId(Long.valueOf(idObject.toString()));
                    item.setPrice(Double.valueOf(row.get(1).toString()));
                    item.setStockCount(Integer.valueOf(row.get(2).toString()));
                    item.setName(itemName);

                    log.debug("item found= " + item);
                    return item;
                }
            }
        }

        throw new ResourceNotFoundException("Item with the name " + itemName + " not found");
    }

    @Override
    public void updateStockCount(Map<Item, Integer> itemAndQuantityMap) throws OutOfStockException {
        try (Transaction tx = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED)) {
            IgniteCache<Long, Item> cache = ignite.cache(Constants.ITEM_CACHE_NAME);

            for (Map.Entry<Item, Integer> itemAndQuantity : itemAndQuantityMap.entrySet()) {

                Item cachedItem = cache.get(itemAndQuantity.getKey().getId());
                if (cachedItem != null) {

                    if (cachedItem.getStockCount() < itemAndQuantity.getValue()) {
                        throw new OutOfStockException("Item out of stock: " + cachedItem.getName());
                    }

                    cachedItem.setStockCount(cachedItem.getStockCount() - itemAndQuantity.getValue());
                    cache.put(itemAndQuantity.getKey().getId(), cachedItem);
                }
            }

            tx.commit();
            log.debug("Item Cache size after update:" + cache.size());
        }
    }


}

package com.dishanm.ignite.menu_api.services;

import com.dishanm.ignite.menu_api.model.Item;
import lombok.extern.log4j.Log4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.transactions.Transaction;
import org.apache.ignite.transactions.TransactionConcurrency;
import org.apache.ignite.transactions.TransactionIsolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.ignite.transactions.TransactionIsolation.REPEATABLE_READ;

@Service
@Log4j
public class IgniteService {
    private static final Logger logger = LoggerFactory.getLogger(IgniteService.class);

    @Autowired
    private Ignite ignite;

    public void addOrUpdateItemCache(Item item) {
        try (Transaction tx = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED)) {
            IgniteCache<Long, Item> cache = ignite.cache("items");
            logger.info("[addOrUpdateItemCache] Item Cache size before update:" + cache.size());
            cache.put(item.getId(), item);
            logger.info("[addOrUpdateItemCache] Item Cache size after update:" + cache.size());

            tx.commit();
        }
    }

    public void addItemList(List<Item> items) {
        try (Transaction tx = ignite.transactions().txStart(TransactionConcurrency.OPTIMISTIC, TransactionIsolation.READ_COMMITTED)) {
            IgniteCache<Long, Item> cache = ignite.cache("items");
            items.forEach((item) -> cache.put(item.getId(), item));
            logger.info("[addItemList] Item Cache size after update:" + cache.size());
            tx.commit();
        }
    }

}

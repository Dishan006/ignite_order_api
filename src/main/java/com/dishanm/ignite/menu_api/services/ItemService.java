package com.dishanm.ignite.menu_api.services;


import com.dishanm.ignite.menu_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.menu_api.model.Item;
import com.dishanm.ignite.menu_api.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemByID(long id) throws ResourceNotFoundException {
        Item item = itemRepository.findById(id).orElse(null);

        if (item != null) {
            return item;
        }

        throw new ResourceNotFoundException("Item not found id:" + id);
    }

    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
        }
    }

    public Item updateItem(long id, Item item) throws ResourceNotFoundException {
        if (itemRepository.existsById(id)) {
            return itemRepository.save(item);
        }

        throw new ResourceNotFoundException("Item not found id:" + id);
    }
}

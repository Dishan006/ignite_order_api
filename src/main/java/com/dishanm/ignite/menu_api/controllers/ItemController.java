package com.dishanm.ignite.menu_api.controllers;

import com.dishanm.ignite.menu_api.Exceptions.ResourceNotFoundException;
import com.dishanm.ignite.menu_api.model.Item;
import com.dishanm.ignite.menu_api.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/menu_api/")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public Item getItemByID(@PathVariable(value = "id") long id) throws ResourceNotFoundException {
        return itemService.getItemByID(id);
    }

    @PostMapping("/items")
    public Item createItem(@Valid @RequestBody Item newItem){
        return itemService.addItem(newItem);
    }

    @DeleteMapping("/items/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItem(@PathVariable(value = "id") long id)
    {
        itemService.deleteItem(id);
    }

}

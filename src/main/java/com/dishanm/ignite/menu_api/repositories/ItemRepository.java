package com.dishanm.ignite.menu_api.repositories;


import com.dishanm.ignite.menu_api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {}
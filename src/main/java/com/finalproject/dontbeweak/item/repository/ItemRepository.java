package com.finalproject.dontbeweak.item.repository;

import com.finalproject.dontbeweak.item.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
package org.example.test.service;

import org.example.test.model.Item;
import org.example.test.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public void saveItem(Item item) {
        this.itemRepository.save(item);
    }

    public Item getItemById(long id) {
        return itemRepository.getItemById(id);
    }

    public void deleteById(long id) {
        itemRepository.deleteById(id);
    }

    public boolean isProductInUse(long id) {
        return itemRepository.existsByProduct_Id(id);
    }
}

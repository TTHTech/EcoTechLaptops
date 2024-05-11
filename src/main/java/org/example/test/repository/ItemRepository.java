package org.example.test.repository;

import org.example.test.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item getItemById(long id);

    boolean existsByProduct_Id(Long productId);

    boolean existsByProduct_IdIsNotNull();
}

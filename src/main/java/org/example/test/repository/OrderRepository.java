package org.example.test.repository;

import org.example.test.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders WHERE create_date LIKE CONCAT('%',:currentDay,'%')", nativeQuery = true)
    List<Order> findOrdersByDate(String currentDay); // format: YYYY-MM-DD
}


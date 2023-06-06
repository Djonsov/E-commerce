package ru.ecommerce.highstylewear.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ecommerce.highstylewear.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends GenericRepository<Order>{
    List<Order> findAllByUserId(final Long id);


}

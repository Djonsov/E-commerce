package ru.ecommerce.highstylewear.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.ecommerce.highstylewear.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends GenericRepository<Item> {
    @Query(nativeQuery = true, value = """
            select distinct 
            from items i 
            where i.title ilike '%' || coalesce(:title,'%') || '%'
                and i.is_deleted = false
            """)
    List<Item> searchItemsByTitle(@Param(value = "title") String title);

    Item findItemById(Long id);
}

package ru.ecommerce.highstylewear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.model.GenericModel;

import java.util.List;

@NoRepositoryBean
public interface GenericRepository<T extends GenericModel> extends JpaRepository<T, Long> {
    List<T> findAllByIsDeletedFalse();


}

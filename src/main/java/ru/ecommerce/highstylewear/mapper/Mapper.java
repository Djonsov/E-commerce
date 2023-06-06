package ru.ecommerce.highstylewear.mapper;

/*
Интерфейс, имеющий основной набор методов для преобразования из сущности в DTO
 */

import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.model.GenericModel;

import java.util.List;

public interface Mapper <E extends GenericModel, D extends GenericDTO>{
    E toEntity(D dto);
    D toDTO(E entity);
    List<E> toEntities(List<D> dtos);
    List<D> toDTOs(List<E> entities);
}

package ru.ecommerce.highstylewear.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.exception.OrderDeleteException;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.repository.GenericRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Абстрактный сервис который хранит в себе реализацию CRUD операций по умолчанию
 * Если реализация отличная от того что представлено в этом классе,
 * то она переопределяется в сервисе для конкретной сущности
 *
 * @param <T> - Сущность с которой мы работаем
 * @param <N> - DTO, которую мы будем отдавать/принимать дальше
 */

@Service
public abstract class GenericService<T extends GenericModel, N extends GenericDTO>  {
    protected final GenericRepository<T> repository;
    protected final GenericMapper<T, N> mapper;

    public GenericService(GenericRepository<T> repository, GenericMapper<T, N> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<N> getAll(){
        return mapper.toDTOs(repository.findAll());
    }

    public List<N> getAllNotDeleted(){
        List<T> res = repository.findAllByIsDeletedFalse();
        return mapper.toDTOs(res);
    }

    public N getById(Long id){
        return mapper.toDTO(repository.findById(id).orElseThrow(()->new NotFoundException("Данных по заданному id: " + id + " не найдено")));
    }

    public N create(N newObject){
        return mapper.toDTO(repository.save(mapper.toEntity(newObject)));
    }

    public N update(N updatedObject){
        return mapper.toDTO(repository.save(mapper.toEntity(updatedObject)));
    }

    public void delete(final Long id) throws OrderDeleteException {
        repository.deleteById(id);
    }

    public void deleteSoft(final Long id){
        T obj = repository.findById(id).orElseThrow(()->new NotFoundException("Запись не найдена"));
        markAsDeleted(obj);
        repository.save(obj);
    }

    public void restore(final Long id){
        T obj = repository.findById(id).orElseThrow(()->new NotFoundException("Запись не найдена"));
        unMarkAsDeleted(obj);
        repository.save(obj);
    }

    public void markAsDeleted(GenericModel genericModel){
        genericModel.setIsDeleted(true);
        genericModel.setDeletedWhen(LocalDateTime.now());
        genericModel.setDeletedBy(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void unMarkAsDeleted(GenericModel genericModel){
        genericModel.setIsDeleted(false);
        genericModel.setDeletedWhen(null);
        genericModel.setDeletedBy(null);
    }



}

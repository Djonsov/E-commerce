package ru.ecommerce.highstylewear.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.exception.DeleteException;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetails;

public abstract class GenericTest<E extends GenericModel, D extends GenericDTO> {
    protected GenericService<E,D> service;
    protected GenericRepository<E> repository;
    protected GenericMapper<E,D> mapper;

    @BeforeEach
    void init(){
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(CustomUserDetails
                .builder()
                .username("USER"),
                null,
                null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    protected abstract void getAll();

    protected abstract void getOne();

    protected abstract void create();

    protected abstract void update();

    protected abstract void delete() throws DeleteException;

    protected abstract void restore();

    protected abstract void getAllNotDeleted();

}

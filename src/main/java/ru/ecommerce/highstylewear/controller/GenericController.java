package ru.ecommerce.highstylewear.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.exception.OrderDeleteException;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.service.GenericService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Абстрактный контроллер
 * который реализует все EndPoint`ы для crud операций используя абстрактный репозиторий
 *
 * @param <T> - Сущность с которой работает контроллер
 * @param <N> - DTO с которой работает контроллер
 */


@RestController
@Slf4j
public abstract class GenericController<T extends GenericModel, N extends GenericDTO> {
    protected GenericService<T,N> service;

    public GenericController(GenericService<T,N> genericService){
        this.service = genericService;
    }

    @CrossOrigin
    @Operation(description = "Получить запись по ID", method = "getById")
    @RequestMapping(value = "/getById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> getById(@RequestParam(value = "id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getById(id));
    }

    @Operation(description = "Получить все записи", method = "getAll")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<N>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAll());
    }

    @Operation(description = "Создать запись", method = "add")
    @RequestMapping(value = "/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> create(@RequestBody N newEntity) {
        newEntity.setCreatedWhen(LocalDateTime.now());
        newEntity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(newEntity));
    }

    @Operation(description = "Обновить запись", method = "update")
    @RequestMapping(value = "/update", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<N> update(@RequestBody N updatedEntity,
                                    @RequestParam(value = "id") Long id) {
        updatedEntity.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(service.update(updatedEntity));
    }


    @Operation(description = "Удалить запись", method = "delete")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Long id) {
        service.deleteSoft(id);
    }

    @Operation(description = "Удалить запись по Id", method = "delete")
    @RequestMapping(value = "/delete/hard/{id}", method = RequestMethod.DELETE)
    public void deleteHard(@PathVariable(value = "id") Long id) throws OrderDeleteException {
        service.delete(id);
    }

}

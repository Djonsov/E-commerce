package ru.ecommerce.highstylewear.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.ecommerce.highstylewear.dto.ItemDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTest extends CommonRestTest {
    private static Long createdTestItemId ;

    @Test
    @Order(0)
    @Override
    protected void getAll() throws Exception {
        log.info("Тест по просмотру всех предметов одежды через REST начат");
        String result = mvc.perform(
                        MockMvcRequestBuilders.get("http://localhost:8080/api/rest/items/getAll")
                                .headers(super.headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.*", hasSize(greaterThan(0))))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ItemDTO> itemsDTO = objectMapper.readValue(result, new TypeReference<List<ItemDTO>>() {
        });
        itemsDTO.forEach(a -> log.info(a.toString()));
        log.info("Тест по просмотру всех предметов одежды через REST закончен");
    }

    @Test
    @Order(1)
    @Override
    protected void createObject() throws Exception {
        log.info("Тест по созданию предмета одежды через REST начат");
        //Создаем нового автора через REST-контроллер
        ItemDTO itemDTO = new ItemDTO("black", 500D, 48, "t-shirt", "1", new ArrayList<>());

        /*
        Вызываем метод создания (POST) в контроллере, передаем ссылку на REST API в MOCK.
        В headers передаем токен для авторизации (под админом, смотри родительский класс).
        Ожидаем, что статус ответа будет успешным и что в ответе есть поле ID, а далее возвращаем контент как строку
        Все это мы конвертируем в AuthorDTO при помощи ObjectMapper от библиотеки Jackson.
        Присваиваем в статическое поле ID созданного автора, чтобы далее с ним работать.
         */
        ItemDTO result = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.post("http://localhost:8080/api/rest/items/add")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .content(asJsonString(itemDTO))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ItemDTO.class);
        createdTestItemId = result.getId();
        log.info("Тест по созданию предмета одежды через REST закончен");

    }

    @Test
    @Order(2)
    @Override
    protected void updateObject() throws Exception {
        log.info("Тест по обновлению предмета одежды через REST начат");
        ItemDTO existingTestItem = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.get("http://localhost:8080/api/rest/items/getById")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .param("id", String.valueOf(createdTestItemId))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ItemDTO.class);
        //обновляем поля
        existingTestItem.setTitle("REST_TestItemTitle_UPDATED");
        existingTestItem.setPrice(550D);

        //вызываем update по REST API
        mvc.perform(
                        MockMvcRequestBuilders.put("http://localhost:8080/api/rest/items/update")
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .content(asJsonString(existingTestItem))
                                .param("id", String.valueOf(createdTestItemId))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        log.info("Тест по обновлению предмета одежды через REST закончен");
    }

    @Test
    @Order(3)
    @Override
    protected void deleteObject() throws Exception {
        log.info("Тест по удалению предмета одежды через REST начат");
        mvc.perform(
                        MockMvcRequestBuilders.delete("http://localhost:8080/api/rest/items/delete/{id}", createdTestItemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        ItemDTO existingTestItem = objectMapper.readValue(
                mvc.perform(
                                MockMvcRequestBuilders.get("http://localhost:8080/api/rest/items/getById")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .headers(super.headers)
                                        .param("id", String.valueOf(createdTestItemId))
                                        .accept(MediaType.APPLICATION_JSON)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                ItemDTO.class);
        assertTrue(existingTestItem.isDeleted());
        log.info("Тест по удалению предмета одежды через REST закончен");
        mvc.perform(
                        MockMvcRequestBuilders.delete("http://localhost:8080/api/rest/items/delete/hard/{id}", createdTestItemId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .headers(super.headers)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        log.info("Данные очищены!");
    }


}

package ru.ecommerce.highstylewear.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ecommerce.highstylewear.constants.Errors;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.MyAccessDeniedException;
import ru.ecommerce.highstylewear.exception.OrderDeleteException;
import ru.ecommerce.highstylewear.model.Order;
import ru.ecommerce.highstylewear.service.GenericService;
import ru.ecommerce.highstylewear.service.OrderService;
import ru.ecommerce.highstylewear.service.UserService;
import ru.ecommerce.highstylewear.service.userdetails.CustomUserDetails;

import java.util.List;


@RestController
@RequestMapping("/api/rest/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@Tag(name = "Заказы", description = "Контроллер для работы с заказами магазина")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController extends GenericController<Order, OrderDTO> {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(GenericService<Order, OrderDTO> genericService, OrderService orderService, UserService userService) {
        super(genericService);
        this.orderService = orderService;
        this.userService = userService;
    }


    @PostMapping("/order")
    public ResponseEntity<?> order(@RequestBody OrderDTO newEntity) throws Exception {
        if (userService.checkForAccess(newEntity.getUser())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.order(newEntity));
        } else
            throw new AccessDeniedException("У вас нет прав делать такое");
    }

    @CrossOrigin
    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestBody List<ItemDTO> items) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userService.checkForAccess(userDetails.getUserId())) {
            return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(items));
        } else
            throw new AccessDeniedException("У вас нет прав делать такое");
    }


    @Override
    @Secured(value = "ROLE_ADMIN")
    @DeleteMapping("/delete/hard/{id}")
    public void deleteHard(@PathVariable Long id) {
        try {
            orderService.delete(id);
        } catch (OrderDeleteException e) {
            log.error(e.getMessage() + "OrderId: " + id + " не прошло и 5 лет");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllUsersOrders(@PathVariable("id") Long userId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userId.equals(userDetails.getUserId())) {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllUsersOrders(userId));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Errors.Users.USER_FORBIDDEN_ERROR);
        }
    }

}

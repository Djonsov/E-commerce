package ru.ecommerce.highstylewear.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.ecommerce.highstylewear.dto.GenericDTO;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.OrderDeleteException;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.model.Order;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.repository.ItemRepository;
import ru.ecommerce.highstylewear.repository.OrderRepository;
import ru.ecommerce.highstylewear.utils.MailUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService extends GenericService<Order, OrderDTO> {
    private final GenericRepository itemRepository;
    private final GenericRepository userRepository;

    private final ItemService itemService;


    private final SpringTemplateEngine springTemplateEngine;

    private final JavaMailSender javaMailSender;

    private final UserService userService;
    private final OrderRepository orderRepository;

    public OrderService(GenericRepository<Order> repository, GenericMapper<Order, OrderDTO> mapper, ItemService itemService, SpringTemplateEngine springTemplateEngine, UserService userService, OrderRepository orderRepository,
                        @Qualifier("userRepository") GenericRepository userRepository, JavaMailSender javaMailSender,
                        @Qualifier("itemRepository") GenericRepository itemRepository) {
        super(repository, mapper);
        this.itemService = itemService;
        this.springTemplateEngine = springTemplateEngine;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.itemRepository = itemRepository;
    }

    public List<OrderDTO> getAllUsersOrders(Long userId) {

        return mapper.toDTOs(((OrderRepository) repository).findAllByUserId(userId));
    }


    public OrderDTO order(OrderDTO orderDTO) throws MessagingException {
        OrderDTO newOrder = mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
        sendOrder(newOrder);
        return newOrder;
    }

    public OrderDTO placeOrder(List<ItemDTO> items) throws MessagingException {
        UserDTO userDTO = userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(userDTO.getId());
        orderDTO.setDetails("online");
        orderDTO.setItemsIds(items.stream().map(GenericDTO::getId).collect(Collectors.toList()));
        orderDTO.setCreatedWhen(LocalDateTime.now());
        orderDTO.setCreatedBy(userDTO.getLogin());
        mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
        sendOrder(orderDTO);
        return orderDTO;
    }

    public void sendOrder(OrderDTO orderDTO) throws MessagingException {
        UserDTO userDTO = userService.getById(orderDTO.getUser());
        Double total = 0D;

        List<Long> itemIds = orderDTO.getItemsIds();
        List<Item> items = itemRepository.findAllById(itemIds);
        for (Item item : items) {
            total += item.getPrice();
        }

        SimpleMailMessage simpleMailMessage = MailUtils.crateMailMessage(userDTO.getEmail(),
                "fromemail@yandex.ru",
                "Заказ в магазине " ,
                String.format("""
                        Здравствуйте.
                        Вы заказали товар в магазине на сумму %f рублей.
                        В ближайшее время с вами в WhatsUp свяжется администратор с уточнениями по оплате и доставке
                                                
                                                
                        """, total));


        javaMailSender.send(simpleMailMessage);
        log.info("-------MAIL SEND-------");
    }

    @Override
    public void delete(Long id) throws OrderDeleteException {

        if (checkOrderForDelete(id)) {
            super.delete(id);
        } else {
            throw new OrderDeleteException();
        }
    }

    private boolean checkOrderForDelete(Long id) {
        return orderRepository.getById(id).getCreatedWhen().plusYears(5).isBefore(LocalDateTime.now());
    }
}

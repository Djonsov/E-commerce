package ru.ecommerce.highstylewear.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import ru.ecommerce.highstylewear.dto.ItemDTO;
import ru.ecommerce.highstylewear.dto.OrderDTO;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.exception.OrderDeleteException;
import ru.ecommerce.highstylewear.mapper.GenericMapper;
import ru.ecommerce.highstylewear.model.Item;
import ru.ecommerce.highstylewear.model.Order;
import ru.ecommerce.highstylewear.repository.GenericRepository;
import ru.ecommerce.highstylewear.repository.OrderRepository;
import ru.ecommerce.highstylewear.utils.MailUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService extends GenericService<Order, OrderDTO> {
    private final GenericRepository userRepository;


    private final SpringTemplateEngine springTemplateEngine;

    private final JavaMailSender javaMailSender;

    private final UserService userService;
    private final OrderRepository orderRepository;

    public OrderService(GenericRepository<Order> repository, GenericMapper<Order, OrderDTO> mapper, SpringTemplateEngine springTemplateEngine, UserService userService, OrderRepository orderRepository,
                        @Qualifier("userRepository") GenericRepository userRepository, JavaMailSender javaMailSender) {
        super(repository, mapper);
        this.springTemplateEngine = springTemplateEngine;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }

    public List<OrderDTO> getAllUsersOrders(Long userId) {

        return mapper.toDTOs(((OrderRepository) repository).findAllByUserId(userId));
    }


    public OrderDTO order(OrderDTO orderDTO) throws MessagingException {
        OrderDTO newOrder = mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
        sendOrder(newOrder);
        return newOrder;
    }

    public OrderDTO placeOrder(List<ItemDTO> items){
        UserDTO userDTO = userService.getUserByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUser(userDTO.getId());
        orderDTO.setDetails("online");
        orderDTO.setItemsIds(items.stream().map(m->m.getId()).collect(Collectors.toList()));
        orderDTO.setCreatedWhen(LocalDateTime.now());
        orderDTO.setCreatedBy(userDTO.getLogin());

        return mapper.toDTO(orderRepository.save(mapper.toEntity(orderDTO)));
    }

    public void sendOrder(OrderDTO orderDTO) throws MessagingException {
        UserDTO userDTO = userService.getById(orderDTO.getUser());

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("order", mapper.toEntity(orderDTO));
        context.setVariable("total", mapper.toEntity(orderDTO).getItems().stream().mapToDouble(Item::getPrice).sum());

        String html = springTemplateEngine.process("order",context);

        helper.setTo("djekadjonsov@yandex.ru");
        helper.setText(html, true);
        helper.setSubject("Заказ № " + orderDTO.getId());
        helper.setFrom("djekadjonsov@yandex.ru");
        javaMailSender.send(message);

//        SimpleMailMessage simpleMailMessage = MailUtils.crateMailMessage(//userDTO.getEmail(),
//                "djekadjonsov@yandex.ru",
//                "djekadjonsov@yandex.ru",
//                "Заказ № " + orderDTO.getId(),
//                html);
//
//
//        javaMailSender.send(simpleMailMessage);
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

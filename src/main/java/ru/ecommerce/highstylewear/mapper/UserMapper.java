package ru.ecommerce.highstylewear.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.ecommerce.highstylewear.dto.UserDTO;
import ru.ecommerce.highstylewear.model.GenericModel;
import ru.ecommerce.highstylewear.model.User;
import ru.ecommerce.highstylewear.repository.OrderRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    private final OrderRepository orderRepository;

    protected UserMapper(ModelMapper modelMapper, OrderRepository orderRepository) {
        super(User.class, UserDTO.class, modelMapper);
        this.orderRepository = orderRepository;
    }

    @Override
    protected void mapSpecificFields(UserDTO source, User destination) {
        if (!Objects.isNull(source.getOrders())) {
            destination.setOrders(orderRepository.findAllById(source.getOrders()));
        } else {
            destination.setOrders(Collections.emptyList());
        }
    }

    @Override
    protected void mapSpecificFields(User source, UserDTO destination) {
        destination.setOrders(getIds(source));
    }

    @Override
    protected void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDTO.class)
                .addMappings(m -> m.skip(UserDTO::setOrders)).setPostConverter(toDTOConverter());
        modelMapper.createTypeMap(UserDTO.class, User.class)
                .addMappings(m -> m.skip(User::setOrders)).setPostConverter(toEntityConverter());
    }

    @Override
    protected List<Long> getIds(User entity) {
        return Objects.isNull(entity) || Objects.isNull(entity.getOrders())
                ? null
                : entity.getOrders().stream().map(GenericModel::getId).collect(Collectors.toList());
    }
}

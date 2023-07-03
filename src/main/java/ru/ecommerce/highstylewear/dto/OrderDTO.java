package ru.ecommerce.highstylewear.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO extends GenericDTO{
    private String details;
    private Long user;
    private List<Long> itemsIds;

}

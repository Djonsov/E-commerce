package ru.ecommerce.highstylewear.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO extends GenericDTO {
    private String color;
    private Double price;
    private Integer size;
    private String title;
    private String image;
    private List<Long> ordersIds;
}

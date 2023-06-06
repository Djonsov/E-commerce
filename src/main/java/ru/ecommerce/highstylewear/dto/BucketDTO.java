package ru.ecommerce.highstylewear.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BucketDTO extends GenericDTO{
    //private Long userId;
    private List<ItemDTO> itemsIds;
}

package ru.ecommerce.highstylewear.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "default_generator", sequenceName = "orders_seq", allocationSize = 1)
public class Order extends GenericModel{
    @Column(name = "details")
    private String details;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ORDERS_USERS"))
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "orders_items",
            joinColumns = @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDERS_ITEMS")),
            inverseJoinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "FK_ITEMS_ORDERS")))
    private List<Item> items;

}

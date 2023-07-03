package ru.ecommerce.highstylewear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "items_seq", allocationSize = 1)
public class Item extends GenericModel {
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "image", nullable = false)
    private String image;

//    @Column(name = "image_path")
//    private String imagePath;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "orders_items",
            joinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "FK_ITEMS_ORDERS")),
            inverseJoinColumns = @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_ORDERS_ITEMS")))
    private List<Order> orders;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "buckets_items",
            joinColumns = @JoinColumn(name = "item_id"), foreignKey = @ForeignKey(name = "fk_items_buckets"),
            inverseJoinColumns = @JoinColumn(name = "bucket_id"), inverseForeignKey = @ForeignKey(name = "fk_buckets_items")
    )
    private List<Bucket> buckets;
}

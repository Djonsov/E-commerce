package ru.ecommerce.highstylewear.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buckets")
@SequenceGenerator(name = "default_generator", sequenceName = "buckets_seq", allocationSize = 1)
public class Bucket extends GenericModel {
//    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
//    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "buckets_items",
            joinColumns = @JoinColumn(name = "bucket_id", foreignKey = @ForeignKey(name = "fk_buckets_items")),
            inverseJoinColumns = @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_items_buckets"))
    )
    private List<Item> items;
}

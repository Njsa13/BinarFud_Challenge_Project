package com.binarfud.binarfud_challenge5.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(length = 50)
    private Integer price;

    @Column(name = "image_file")
    private byte[] imageFile;

    @ManyToOne
    @JoinColumn(name = "merchant_id", referencedColumnName = "merchant_id", nullable = false)
    private Merchant merchant;

    @OneToMany(mappedBy = "product", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<OrderDetail> orderDetails;
}

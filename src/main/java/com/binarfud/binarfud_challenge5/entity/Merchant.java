package com.binarfud.binarfud_challenge5.entity;

import com.binarfud.binarfud_challenge5.enums.MerchantStatus;
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
@Table(name = "merchant", uniqueConstraints = {@UniqueConstraint(columnNames = "merchant_name")})
public class Merchant {
    @Id
    @Column(name = "merchant_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String merchantId;

    @Column(name = "merchant_name", length = 100)
    private String merchantName;

    @Column(name = "merchant_location", columnDefinition = "text")
    private String merchantLocation;

    @Column(name = "merchant_status")
    @Enumerated(EnumType.STRING)
    private MerchantStatus merchantStatus;

    @OneToMany(mappedBy = "merchant", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<Product> products;
}

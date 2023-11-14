package com.binarfud.binarfud_challenge7.entity;

import com.binarfud.binarfud_challenge7.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String orderId;

    @Column(name = "order_time")
    private Date orderTime;

    @Column(name = "destination_address", columnDefinition = "text")
    private String destinationAddress;

    @Column(name = "total_price", length = 50)
    private Integer totalPrice;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<OrderDetail> orderDetails;
}

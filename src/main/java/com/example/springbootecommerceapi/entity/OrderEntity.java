package com.example.springbootecommerceapi.entity;

import com.example.springbootecommerceapi.model.OrderStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "orderEntity"
)
public class OrderEntity {

    @Id
    @Column(
            name = "orderNumber"
    )
    @SequenceGenerator(
            name = "orderSequence",
            sequenceName = "orderSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "orderSequence"
    )
    private Long orderNumber;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "UserNumber",
            referencedColumnName = "UserNumber"
    )
    private UserEntity customer;
    @Column(
            name = "datePlaced",
            nullable = false
    )
    private LocalDateTime datePlaced;
    @Column(
            name = "dateShipped"
    )
    private LocalDateTime dateShipped;
    @Column(
            nullable = false
    )
    @Enumerated(
            EnumType.STRING
    )
    private OrderStatus status;
}

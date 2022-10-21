package com.example.springbootecommerceapi.entity;

import com.example.springbootecommerceapi.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
            fetch = FetchType.LAZY
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime datePlaced;
    @Column(
            name = "dateShipped"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime dateShipped;
    @Column(
            nullable = false
    )
    @Enumerated(
            EnumType.STRING
    )
    private OrderStatus status;

    public OrderEntity() {
    }

    public OrderEntity(UserEntity customer) {
        this.status = OrderStatus.PROCESSING;
        this.customer = customer;
        this.datePlaced = LocalDateTime.now();
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public LocalDateTime getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(LocalDateTime datePlaced) {
        this.datePlaced = datePlaced;
    }

    public LocalDateTime getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(LocalDateTime dateShipped) {
        this.dateShipped = dateShipped;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(orderNumber, that.orderNumber) && Objects.equals(customer, that.customer) && Objects.equals(datePlaced, that.datePlaced) && Objects.equals(dateShipped, that.dateShipped) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customer, datePlaced, dateShipped, status);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "orderNumber=" + orderNumber +
                ", customer=" + customer +
                ", datePlaced=" + datePlaced.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")) +
                ", dateShipped=" + dateShipped +
                ", status=" + status +
                '}';
    }
}

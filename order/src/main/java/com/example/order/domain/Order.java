package com.example.order.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ShoppingOrder")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    private Double prixTotal;

    @OneToMany(cascade = CascadeType.ALL)
    private  List<OrderItem> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public void addProduct(OrderItem orderItem) {
        products.add(orderItem);
    }
}

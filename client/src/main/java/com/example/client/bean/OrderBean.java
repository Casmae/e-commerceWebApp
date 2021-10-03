package com.example.client.bean;

import java.util.Date;
import java.util.List;

public class OrderBean {

    private Long id;

    private Date date=new Date();

    private Double prixTotal;

    private List<OrderItemBean> products;

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

    public List<OrderItemBean> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItemBean> products) {
        this.products = products;
    }
}

package com.example.StoreProjectSpringInit.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private Long id;
    private String fullName;
    private double moneyAmount;
    private List<Product> productList;

    public Customer() {
    }

    public Customer(Long id, String fullName, double moneyAmount) {
        this.id = id;
        this.fullName = fullName;
        this.moneyAmount = moneyAmount;
        this.productList = new LinkedList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", moneyAmount=" + moneyAmount +
                ", productList=" + productList +
                '}';
    }
}

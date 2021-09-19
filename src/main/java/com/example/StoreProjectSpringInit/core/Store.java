package com.example.StoreProjectSpringInit.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class Store {
    public static final String JSON_PRODUCTS_FILE_NAME = "products.json";
    public static final String JSON_CUSTOMERS_FILE_NAME = "customers.json";
    private Set<Product> productSet;
    private Set<Customer> customersSet;
    private static Store store;

    //Singleton
    public static Store getStore() {
        if(null == store) {
            store = new Store();
        }
        return store;
    }

    private Store() {
        try {
            loadProductsFromJson();
            loadCustomersFromJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Product Actions*/
    private void loadProductsFromJson() throws IOException {
        if(new File(JSON_PRODUCTS_FILE_NAME).exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference typeReferenceSet = new TypeReference<Set<Product>>(){};
                productSet = (HashSet<Product>) objectMapper.readValue(new File(JSON_PRODUCTS_FILE_NAME),typeReferenceSet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.productSet = new HashSet<>();
        }
    }

    @Scheduled(fixedRate = 30000)
    public void publishProductsToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(JSON_PRODUCTS_FILE_NAME), productSet);
    }

    public boolean createAndAddProduct(long productId, String productName, String productDescription, double productPrice) {
        Product product = new Product(productId, productName, productDescription, productPrice);
        return addProduct(product);
    }

    public boolean addProduct(Product product) {
        return productSet.add(product);
    }

    public boolean removeProduct(Product product) {
        return productSet.remove(product);
    }

    public Product getProduct(Long id) throws Exception {
        for(Product product: productSet) {
            if(product.getProductId().equals(id))
                return product;
        }
        throw new Exception(String.format("Product with ID %d Not Found", id));
    }

    public Set<Product> getProducts() {
        return productSet;
    }

    public boolean updateProduct(Product product) throws Exception {
            Product productToRemove = getProduct(product.getProductId());

            return removeProduct(productToRemove) &&
                   addProduct(product);
    }

    /*Customer Actions*/
    private void loadCustomersFromJson() throws IOException {
        if(new File(JSON_CUSTOMERS_FILE_NAME).exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference typeReferenceSet = new TypeReference<Set<Customer>>(){};
                customersSet = (HashSet<Customer>) objectMapper.readValue(new File(JSON_CUSTOMERS_FILE_NAME),typeReferenceSet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.customersSet = new HashSet<>();
        }
    }

    @Scheduled(fixedRate = 30000)
    public void publishCustomersToJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File(JSON_CUSTOMERS_FILE_NAME), customersSet);
    }

    public boolean createAndAddCustomer(Long id, String customerName, double customerMoney) {
        Customer customer = new Customer(id, customerName, customerMoney);
        return addCustomer(customer);
    }

    public boolean addCustomer(Customer customer) {
        return customersSet.add(customer);
    }

    private Customer getCustomer(Long id) throws Exception {
        for(Customer customer: customersSet) {
            if(customer.getId().equals(id))
                return customer;
        }
        throw new Exception(String.format("Customer with ID %d Not Found", id));
    }

    public boolean addMoney(Long id, double moneyToAdd) throws Exception {
        Customer customer = getCustomer(id);
        customersSet.remove(customer);
        customer.setMoneyAmount(customer.getMoneyAmount() + moneyToAdd);

        return store.addCustomer(customer);
    }

    public boolean removeCustomer(Customer customer) {
        return customersSet.remove(customer);
    }

    public boolean buyProduct(Long customerId, Long productId) throws Exception {
        Customer customer = getCustomer(customerId);
        Product product = getProduct(productId);

        if(customer.getMoneyAmount() < product.getProductPrice())
            throw new Exception(String.format("Sorry, but you do not have enough money to purchase this product. Please deposit more money and try again"));

        customer.getProductList().add(product);
        customer.setMoneyAmount(customer.getMoneyAmount() - product.getProductPrice());

        return customersSet.remove(customer) &&
               customersSet.add(customer);
    }
}

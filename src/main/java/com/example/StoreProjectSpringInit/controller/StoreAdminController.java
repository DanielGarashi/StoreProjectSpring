package com.example.StoreProjectSpringInit.controller;

import com.example.StoreProjectSpringInit.core.Product;
import com.example.StoreProjectSpringInit.core.Store;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/Admin")
@EnableScheduling
public class StoreAdminController {

    private static Store store = Store.getStore();

    @RequestMapping(value = "addProduct", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addProject(@RequestParam("id") Long id,
                                             @RequestParam("name") String productName,
                                             @RequestParam("description") String productDescription,
                                             @RequestParam("price") double productPrice) {
        var added = store.createAndAddProduct(id, productName, productDescription, productPrice);

        return added ? ResponseEntity.ok().body("Product has added successfully.") :
                ResponseEntity.internalServerError().body("Could not add product.");
    }

    @RequestMapping(value = "getProduct", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProduct(@RequestParam("id") Long id) {
        try {
            Product product = store.getProduct(id);
            ObjectMapper objectMapper = new ObjectMapper();
            var json = objectMapper.writeValueAsString(product);
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @RequestMapping(value = "getProducts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProducts() {
        try {
            var products = store.getProducts();
            ObjectMapper objectMapper = new ObjectMapper();
            var json = objectMapper.writeValueAsString(products);
            return ResponseEntity.ok().body(json);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @RequestMapping(value = "removeProduct", method = RequestMethod.DELETE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> removeProduct(@RequestParam("id") Long id){
        try {
            Product product = store.getProduct(id);
            var removed = store.removeProduct(product);
            return removed ? ResponseEntity.ok().body("Product removed successfully") :
                ResponseEntity.internalServerError().body("Could not removed product");

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @RequestMapping(value = "updateProduct", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateProduct(@RequestParam("id") Long id,
                                                @RequestParam("name") String productName,
                                                @RequestParam("description") String productDescription,
                                                @RequestParam("price") double productPrice){
        try {
            Product product = new Product(id, productName, productDescription, productPrice);
            var updated = store.updateProduct(product);
            return updated ? ResponseEntity.ok().body(String.format("Product has updated successfully.")) :
                ResponseEntity.internalServerError().body(String.format("Could not update product %d.", id));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @Scheduled(fixedRate = 10000L)
    private void saveProducts(){
        try {
            store.publishProductsToJson();
            System.out.println(String.format("{ %s:StoreAdminController::saveProducts- Save Products list successfully }", new SimpleDateFormat("{dd.MM.yyyy-HH:mm:ss}").format(new Date())));
        } catch (IOException e) {
                System.out.println(String.format("{ %s:StoreAdminController::saveProducts- Problem with save Products list\n%s }", new SimpleDateFormat("{dd.MM.yyyy-HH:mm:ss}").format(new Date()), e.getMessage()));
        }
    }
}

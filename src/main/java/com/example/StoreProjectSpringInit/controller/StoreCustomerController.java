package com.example.StoreProjectSpringInit.controller;

import com.example.StoreProjectSpringInit.core.Store;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/Customer")
@EnableScheduling
public class StoreCustomerController {

    private static Store store = Store.getStore();

    @RequestMapping(value = "addCustomer", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addCustomer(@RequestParam("id") Long id,
                                              @RequestParam("name") String customerName,
                                              @RequestParam("money") double customerMoney){
            var added = store.createAndAddCustomer(id, customerName, customerMoney);
            return added ? ResponseEntity.ok().body("Customer has added successfully.") :
                           ResponseEntity.internalServerError().body("Could not add customer, this customer already exits.");
    }

    @RequestMapping(value = "addMoney", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> addMoney(@RequestParam("id") Long id,
                                           @RequestParam("money") double money){
        try {
            boolean added = store.addMoney(id, money);
            return added ? ResponseEntity.ok().body("Money added successfully.") :
                           ResponseEntity.internalServerError().body("Could not add money.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }

    @RequestMapping(value = "buyProduct", method = RequestMethod.PUT, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> buyProduct(@RequestParam("customerId") Long customerId,
                                           @RequestParam("productId") Long productId){
        try {
            var succeedToBuy = store.buyProduct(customerId, productId);
            return succeedToBuy ? ResponseEntity.ok().body(String.format("Customer with id %d buy Product with id %d successfully.",customerId, productId)) :
                                  ResponseEntity.internalServerError().body("Could not buy product.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }

    }

    @Scheduled(fixedRate = 10000L)
    private void saveCustomers(){
        try {
            store.publishCustomersToJson();
            System.out.println(String.format("{ %s:StoreCustomerController::saveCustomers- Save Customers list successfully }", new SimpleDateFormat("{dd.MM.yyyy-HH:mm:ss}").format(new Date())));
        } catch (IOException e) {
            System.out.println(String.format("{ %s:StoreCustomerController::saveCustomers- Problem with save Customers list\n%s }", new SimpleDateFormat("{dd.MM.yyyy-HH:mm:ss}").format(new Date()), e.getMessage()));
        }
    }

}

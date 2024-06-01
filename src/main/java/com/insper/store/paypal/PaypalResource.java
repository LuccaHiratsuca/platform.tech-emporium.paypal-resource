package com.insper.store.paypal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.paypal.base.rest.PayPalRESTException;


@RestController
public class PaypalResource implements PaypalController {

    @Autowired
    private PaypalService paypalService;

    @Override
    public ResponseEntity<?> createPayment(PaypalIn paypalIn) {
        try {
            return ResponseEntity.ok(paypalService.createPayment(paypalIn));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> executePayment(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId) {
    try {
        PaypalModel payment = paypalService.executePayment(paymentId, payerId);
        return ResponseEntity.ok(payment);
    } catch (PayPalRESTException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }
}


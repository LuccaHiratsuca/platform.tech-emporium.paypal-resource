package com.insper.store.paypal;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;


@Service
public class PaypalService {

    @Autowired
    private PaypalRepository paypalRepository;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    private APIContext getAPIContext(){
        return new APIContext(clientId, clientSecret, mode);
    }

    @Transactional
    public PaypalModel createPayment(PaypalIn in) throws PayPalRESTException {
        APIContext apiContext = getAPIContext();
    
        // Configura o pagamento baseado no objeto PaypalIn.
        Payment payment = new Payment();
        payment.setIntent("sale");
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        payment.setPayer(payer);

        Amount amount = new Amount("USD", String.format("%.2f", in.amount()));
        Transaction transaction = new Transaction();
        transaction.setDescription(in.description());
        transaction.setAmount(amount);
        payment.setTransactions(Arrays.asList(transaction));
    
        // Cria o pagamento utilizando a API do PayPal
        Payment createdPayment = payment.create(apiContext);
    
        // Salva os detalhes do pagamento no banco de dados
        PaypalModel model = new PaypalModel();
        model.userId(in.userId());
        model.amount(in.amount());
        model.currency(in.currency());
        model.paymentMethod("paypal");
        model.status(createdPayment.getState());
        model.description(in.description());
        model.paymentId(createdPayment.getId());

        return paypalRepository.save(model);
    }
    
    @Transactional
    public PaypalModel executePayment(String paymentId, String payerId) throws PayPalRESTException {
        APIContext apiContext = getAPIContext();
    
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        Payment executedPayment = payment.execute(apiContext, paymentExecution);
    
        // Atualiza o status da transação no banco de dados
        PaypalModel existingModel = paypalRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        existingModel.status(executedPayment.getState());
    
        return paypalRepository.save(existingModel);
    }
}

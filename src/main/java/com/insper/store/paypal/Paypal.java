package com.insper.store.paypal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter @Setter @Accessors(fluent = true, chain = true)
@AllArgsConstructor @NoArgsConstructor
public class Paypal {

    private String id;              // Identificador único para a transação
    private String userId;          // Adicionado para vincular o pagamento ao usuário
    private Double amount;          // Valor do pagamento
    private String currency;        // Moeda utilizada no pagamento
    private String description;     // Descrição do pagamento
    private String paymentMethod;   // Método de pagamento utilizado
    private String status;          // Status da transação
    private String paymentId;       // Identificador do pagamento

}
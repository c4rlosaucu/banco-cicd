package com.udea.lab1arq.controller;

import com.udea.lab1arq.dto.TransactionDTO;
import com.udea.lab1arq.dto.TransferRequestDTO;
import com.udea.lab1arq.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private TransactionDTO dto(Long id) {
        TransactionDTO d = new TransactionDTO();
        d.setId(id);
        d.setSenderAccountNumber("111");
        d.setReceiverAccountNumber("222");
        d.setAmount(25.0);
        return d;
    }

    @Test
    void transferMoney_devuelve200ConTransaccion() {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setSenderAccountNumber("111");
        request.setReceiverAccountNumber("222");
        request.setAmount(25.0);
        when(transactionService.transferMoney(request)).thenReturn(dto(1L));

        ResponseEntity<TransactionDTO> response = transactionController.transferMoney(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getTransactionsForAccount_devuelve200ConLista() {
        when(transactionService.getTransactionsForAccount("111")).thenReturn(List.of(dto(1L), dto(2L)));

        ResponseEntity<List<TransactionDTO>> response = transactionController.getTransactionsForAccount("111");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllTransactions_devuelve200ConLista() {
        when(transactionService.getAllTransactions()).thenReturn(List.of(dto(1L)));

        ResponseEntity<List<TransactionDTO>> response = transactionController.getAllTransactions();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }
}
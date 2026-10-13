package com.udea.lab1arq.service;

import com.udea.lab1arq.dto.TransactionDTO;
import com.udea.lab1arq.dto.TransferRequestDTO;
import com.udea.lab1arq.entity.Customer;
import com.udea.lab1arq.entity.Transaction;
import com.udea.lab1arq.repository.CustomerRepository;
import com.udea.lab1arq.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Customer customer(String account, double balance) {
        Customer c = new Customer();
        c.setAccountNumber(account);
        c.setFirstName("Nombre");
        c.setLastName("Apellido");
        c.setBalance(balance);
        return c;
    }

    private TransferRequestDTO request(String sender, String receiver, Double amount) {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber(sender);
        dto.setReceiverAccountNumber(receiver);
        dto.setAmount(amount);
        return dto;
    }

    private Transaction transaction(Long id, String sender, String receiver, double amount) {
        Transaction t = new Transaction();
        t.setId(id);
        t.setSenderAccountNumber(sender);
        t.setReceiverAccountNumber(receiver);
        t.setAmount(amount);
        t.setTimestamp(LocalDateTime.of(2026, 10, 12, 10, 0));
        return t;
    }

    @Test
    void transferMoney_exitosa_actualizaSaldosYGuardaTransaccion() {
        Customer sender = customer("111", 500.0);
        Customer receiver = customer("222", 100.0);
        when(customerRepository.findByAccountNumber("111")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("222")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(10L);
            t.setTimestamp(LocalDateTime.of(2026, 10, 12, 10, 0));
            return t;
        });

        TransactionDTO result = transactionService.transferMoney(request("111", "222", 150.0));

        assertEquals(350.0, sender.getBalance(), 0.0001);
        assertEquals(250.0, receiver.getBalance(), 0.0001);
        assertEquals(10L, result.getId());
        assertEquals("111", result.getSenderAccountNumber());
        assertEquals("222", result.getReceiverAccountNumber());
        assertEquals(150.0, result.getAmount(), 0.0001);
        assertEquals(LocalDateTime.of(2026, 10, 12, 10, 0), result.getTimestamp());
        verify(customerRepository).save(sender);
        verify(customerRepository).save(receiver);
    }

    @Test
    void transferMoney_cuentaOrigenNula_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request(null, "222", 10.0)));

        assertEquals("Sender Account Number or Receiver Account Number cannot be null", ex.getMessage());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_cuentaDestinoNula_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("111", null, 10.0)));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferMoney_origenNoExiste_lanzaExcepcion() {
        when(customerRepository.findByAccountNumber("111")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("111", "222", 10.0)));

        assertEquals("Sender Account Number not found", ex.getMessage());
    }

    @Test
    void transferMoney_destinoNoExiste_lanzaExcepcion() {
        when(customerRepository.findByAccountNumber("111")).thenReturn(Optional.of(customer("111", 100.0)));
        when(customerRepository.findByAccountNumber("222")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("111", "222", 10.0)));

        assertEquals("Receiver Account Number not found", ex.getMessage());
    }

    @Test
    void transferMoney_saldoInsuficiente_lanzaExcepcionYNoGuarda() {
        when(customerRepository.findByAccountNumber("111")).thenReturn(Optional.of(customer("111", 50.0)));
        when(customerRepository.findByAccountNumber("222")).thenReturn(Optional.of(customer("222", 0.0)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(request("111", "222", 100.0)));

        assertEquals("Sender Balance not enough", ex.getMessage());
        verify(customerRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void getTransactionsForAccount_devuelveTransaccionesDeLaCuenta() {
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("111", "111"))
                .thenReturn(List.of(transaction(1L, "111", "222", 20.0), transaction(2L, "333", "111", 5.0)));

        List<TransactionDTO> result = transactionService.getTransactionsForAccount("111");

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("222", result.get(0).getReceiverAccountNumber());
        assertEquals(5.0, result.get(1).getAmount(), 0.0001);
    }

    @Test
    void getAllTransactions_devuelveTodas() {
        when(transactionRepository.findAll()).thenReturn(List.of(transaction(1L, "111", "222", 20.0)));

        List<TransactionDTO> result = transactionService.getAllTransactions();

        assertEquals(1, result.size());
        assertEquals("111", result.get(0).getSenderAccountNumber());
        assertEquals(LocalDateTime.of(2026, 10, 12, 10, 0), result.get(0).getTimestamp());
    }
}
package com.udea.lab1arq.mapper;

import com.udea.lab1arq.dto.CustomerDTO;
import com.udea.lab1arq.dto.TransactionDTO;
import com.udea.lab1arq.entity.Customer;
import com.udea.lab1arq.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MappersTest {

    @Test
    void customerMapper_entidadADto() {
        Customer c = new Customer();
        c.setId(1L);
        c.setAccountNumber("111");
        c.setFirstName("Ana");
        c.setLastName("Perez");
        c.setBalance(200.0);

        CustomerDTO dto = CustomerMapper.INSTANCE.toDTO(c);

        assertEquals(1L, dto.getId());
        assertEquals("111", dto.getAccountNumber());
        assertEquals("Ana", dto.getFirstName());
        assertEquals("Perez", dto.getLastName());
        assertEquals(200.0, dto.getBalance(), 0.0001);
    }

    @Test
    void customerMapper_dtoAEntidad() {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(2L);
        dto.setAccountNumber("222");
        dto.setFirstName("Luis");
        dto.setLastName("Gomez");
        dto.setBalance(75.0);

        Customer c = CustomerMapper.INSTANCE.toEntity(dto);

        assertEquals(2L, c.getId());
        assertEquals("222", c.getAccountNumber());
        assertEquals("Luis", c.getFirstName());
        assertEquals(75.0, c.getBalance(), 0.0001);
    }

    @Test
    void transactionMapper_entidadADto() {
        Transaction t = new Transaction();
        t.setId(7L);
        t.setSenderAccountNumber("111");
        t.setReceiverAccountNumber("222");
        t.setAmount(30.0);
        t.setTimestamp(LocalDateTime.of(2026, 10, 12, 9, 30));

        TransactionDTO dto = TransactionMapper.INSTANCE.toDTO(t);

        assertEquals(7L, dto.getId());
        assertEquals("111", dto.getSenderAccountNumber());
        assertEquals("222", dto.getReceiverAccountNumber());
        assertEquals(30.0, dto.getAmount(), 0.0001);
        assertEquals(LocalDateTime.of(2026, 10, 12, 9, 30), dto.getTimestamp());
    }

    @Test
    void transactionMapper_dtoAEntidad() {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(8L);
        dto.setSenderAccountNumber("333");
        dto.setReceiverAccountNumber("444");
        dto.setAmount(12.5);

        Transaction t = TransactionMapper.INSTANCE.toEntity(dto);

        assertEquals(8L, t.getId());
        assertEquals("333", t.getSenderAccountNumber());
        assertEquals("444", t.getReceiverAccountNumber());
        assertEquals(12.5, t.getAmount(), 0.0001);
    }
}
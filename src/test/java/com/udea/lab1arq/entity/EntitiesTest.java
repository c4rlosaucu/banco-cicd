package com.udea.lab1arq.entity;

import com.udea.lab1arq.dto.TransferRequestDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EntitiesTest {

    @Test
    void customer_constructorCompletoYGetters() {
        Customer c = new Customer(1L, "111", "Ana", "Perez", 300.0);

        assertEquals(1L, c.getId());
        assertEquals("111", c.getAccountNumber());
        assertEquals("Ana", c.getFirstName());
        assertEquals("Perez", c.getLastName());
        assertEquals(300.0, c.getBalance(), 0.0001);
    }

    @Test
    void transaction_prePersist_asignaFechaSiEsNula() {
        Transaction t = new Transaction();

        t.prePersist();

        assertNotNull(t.getTimestamp());
    }

    @Test
    void transaction_prePersist_conservaFechaExistente() {
        Transaction t = new Transaction();
        LocalDateTime fecha = LocalDateTime.of(2026, 1, 1, 8, 0);
        t.setTimestamp(fecha);

        t.prePersist();

        assertEquals(fecha, t.getTimestamp());
    }

    @Test
    void transferRequestDto_gettersYSetters() {
        TransferRequestDTO dto = new TransferRequestDTO();
        dto.setSenderAccountNumber("111");
        dto.setReceiverAccountNumber("222");
        dto.setAmount(10.0);

        assertEquals("111", dto.getSenderAccountNumber());
        assertEquals("222", dto.getReceiverAccountNumber());
        assertEquals(10.0, dto.getAmount(), 0.0001);
    }
}
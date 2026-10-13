package com.udea.lab1arq.controller;

import com.udea.lab1arq.dto.CustomerDTO;
import com.udea.lab1arq.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDTO dto(Long id, Double balance) {
        CustomerDTO d = new CustomerDTO();
        d.setId(id);
        d.setAccountNumber("111");
        d.setFirstName("Ana");
        d.setLastName("Perez");
        d.setBalance(balance);
        return d;
    }

    @Test
    void getAllCustomers_devuelve200ConLista() {
        when(customerService.getAllCustomer()).thenReturn(List.of(dto(1L, 100.0)));

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getCustomerById_devuelve200ConCliente() {
        when(customerService.getCustomerById(1L)).thenReturn(dto(1L, 100.0));

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("111", response.getBody().getAccountNumber());
    }

    @Test
    void createCustomer_valido_devuelve200() {
        CustomerDTO input = dto(null, 50.0);
        when(customerService.createCustomer(input)).thenReturn(dto(5L, 50.0));

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(input);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(5L, response.getBody().getId());
    }

    @Test
    void createCustomer_sinSaldo_lanzaExcepcion() {
        CustomerDTO input = dto(null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> customerController.createCustomer(input));

        assertEquals("Balance cannot be null", ex.getMessage());
        verifyNoInteractions(customerService);
    }
}
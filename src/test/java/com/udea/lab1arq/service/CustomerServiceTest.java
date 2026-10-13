package com.udea.lab1arq.service;

import com.udea.lab1arq.dto.CustomerDTO;
import com.udea.lab1arq.entity.Customer;
import com.udea.lab1arq.mapper.CustomerMapper;
import com.udea.lab1arq.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer(Long id, String account) {
        Customer c = new Customer();
        c.setId(id);
        c.setAccountNumber(account);
        c.setFirstName("Nombre");
        c.setLastName("Apellido");
        c.setBalance(100.0);
        return c;
    }

    private CustomerDTO dto(Long id, String account) {
        CustomerDTO d = new CustomerDTO();
        d.setId(id);
        d.setAccountNumber(account);
        d.setFirstName("Nombre");
        d.setLastName("Apellido");
        d.setBalance(100.0);
        return d;
    }

    @Test
    void getAllCustomer_devuelveListaMapeada() {
        Customer c1 = customer(1L, "111");
        Customer c2 = customer(2L, "222");
        when(customerRepository.findAll()).thenReturn(List.of(c1, c2));
        when(customerMapper.toDTO(c1)).thenReturn(dto(1L, "111"));
        when(customerMapper.toDTO(c2)).thenReturn(dto(2L, "222"));

        List<CustomerDTO> result = customerService.getAllCustomer();

        assertEquals(2, result.size());
        assertEquals("111", result.get(0).getAccountNumber());
        assertEquals("222", result.get(1).getAccountNumber());
    }

    @Test
    void getCustomerById_existente_devuelveDTO() {
        Customer c = customer(1L, "111");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(c));
        when(customerMapper.toDTO(c)).thenReturn(dto(1L, "111"));

        CustomerDTO result = customerService.getCustomerById(1L);

        assertEquals(1L, result.getId());
        assertEquals("111", result.getAccountNumber());
    }

    @Test
    void getCustomerById_inexistente_lanzaExcepcion() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> customerService.getCustomerById(99L));

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    @Test
    void createCustomer_guardaYDevuelveDTO() {
        CustomerDTO input = dto(null, "333");
        Customer entity = customer(null, "333");
        Customer saved = customer(3L, "333");
        when(customerMapper.toEntity(input)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(saved);
        when(customerMapper.toDTO(saved)).thenReturn(dto(3L, "333"));

        CustomerDTO result = customerService.createCustomer(input);

        assertEquals(3L, result.getId());
        assertEquals("333", result.getAccountNumber());
        verify(customerRepository).save(entity);
    }
}
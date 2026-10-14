package com.udea.lab1arq.dto;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private Double amount;
}
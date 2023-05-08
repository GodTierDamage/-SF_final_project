package com.myProject.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.myProject.finalProject.enums.OperationType;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "INCOME_BALANCE_TRANSACTION")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "transaction_id")
@JsonTypeName(value = "INCOME")
public class Income extends Transaction{

    @Override
    public OperationType operationType() {
        return OperationType.INCOME;
    }
}

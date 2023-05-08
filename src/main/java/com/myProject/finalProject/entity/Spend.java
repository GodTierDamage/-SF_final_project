package com.myProject.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.enums.SpendingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SPEND_BALANCE_TRANSACTION")
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "transaction_id")
@JsonTypeName(value = "SPEND")
public class Spend extends Transaction{

    @Column(name = "type_of_spending_operation")
    @Enumerated(EnumType.STRING)
    @JsonProperty("typeOfSpendingOperation")
    private SpendingType typeOfSpendingOperation;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Override
    public OperationType operationType() {
        return OperationType.SPEND;
    }

    public SpendingType typeOfSpendingOperation() {
        return typeOfSpendingOperation;
    }

    public Spend typeOfSpendingOperation(SpendingType typeOfSpendingOperation) {
        this.typeOfSpendingOperation = typeOfSpendingOperation;
        return this;
    }

    public Long receiverId() {
        return receiverId;
    }

    public Spend receiverId(Long receiverId) {
        this.receiverId = receiverId;
        return this;
    }
}

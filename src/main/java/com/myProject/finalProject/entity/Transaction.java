package com.myProject.finalProject.entity;

import com.fasterxml.jackson.annotation.*;
import com.myProject.finalProject.configuration.OperationTypeConverter;
import com.myProject.finalProject.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.Timestamp;

@SuperBuilder(toBuilder = true)
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "BALANCE_TRANSACTION")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "operationType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Spend.class, name = "2"),
        @JsonSubTypes.Type(value = Income.class, name = "1")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction+sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence")
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "balance_id")
    private Long balanceId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "operation_type")
    @Enumerated(EnumType.ORDINAL)
    @JsonProperty("operationType")
    protected OperationType operationType;

    @Column(name = "date_of_operation")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp dateOfOperation;

    @Column(name = "message")
    private String message;
}

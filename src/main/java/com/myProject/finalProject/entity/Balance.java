package com.myProject.finalProject.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Accessors(chain = true, fluent = true)
@Table(name = "BALANCE")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Balance {

    @Id
    @SequenceGenerator(
            name = "balance_sequence",
            sequenceName = "balance_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "balance_sequence")
    @Column(name = "balance_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    @ColumnDefault("0")
    private BigDecimal amount;

    public BigDecimal amount() {
        return amount == null? BigDecimal.ZERO:amount;
    }

    public void amount(BigDecimal amount) {
        this.amount = amount == null? BigDecimal.ZERO:amount;
    }
}

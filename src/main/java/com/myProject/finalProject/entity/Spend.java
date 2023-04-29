package com.myProject.finalProject.entity;

import com.myProject.finalProject.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.glassfish.grizzly.http.util.TimeStamp;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Accessors(chain = true)
@Table(name = "SPEND")
public class Spend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long balanceId;

    @Column
    private BigDecimal spend;

    @Column
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private final OperationType operationType = OperationType.SPEND;

    @Column
    private TimeStamp dateOfOperation;
}

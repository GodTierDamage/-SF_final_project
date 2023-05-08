package com.myProject.finalProject.configuration;

import com.myProject.finalProject.enums.OperationType;
import jakarta.persistence.AttributeConverter;

public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperationType attribute) {
        return attribute.getType();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer dbData) {
        return OperationType.fromType(dbData);
    }
}

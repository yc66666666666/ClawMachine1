package com.doll.dto;

import com.doll.entity.MailOrder;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderDto extends MailOrder {
    private String captureRecordIds;
}

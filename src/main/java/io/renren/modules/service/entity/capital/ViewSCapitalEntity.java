package io.renren.modules.service.entity.capital;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ViewSCapitalEntity {
    private Long userId;
    private Integer capitalType;
    private BigDecimal money;
}

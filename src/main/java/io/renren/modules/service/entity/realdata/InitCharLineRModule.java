package io.renren.modules.service.entity.realdata;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class InitCharLineRModule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private BigDecimal clicks;
    private BigDecimal exposures;
    private BigDecimal arrives;
    private Integer createYear;
    private Integer createMonth;
    private Integer createDay;
}

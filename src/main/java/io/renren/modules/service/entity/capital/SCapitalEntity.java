package io.renren.modules.service.entity.capital;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 资金流水表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 12:47:32
 */
@Data
@TableName("s_capital")
public class SCapitalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 账户ID
	 */
	private Long userId;
	private String userName;
	/**
	 * 金额
	 */
	private BigDecimal money;
	/**
	 * 资金类型
	 */
	private Integer capitalType;
	/**
	 * 客户端类型
	 */
	private Integer clientType;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人ID
	 */
	private Long createId;
	/**
	 * 创建人姓名
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Date createTime;

	private String createYear;
	private String createMonth;
	private String createDay;


}

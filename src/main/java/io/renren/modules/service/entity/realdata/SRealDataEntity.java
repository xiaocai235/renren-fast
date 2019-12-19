package io.renren.modules.service.entity.realdata;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 数据推广
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-04 09:56:34
 */
@Data
@TableName("s_real_data")
public class SRealDataEntity implements Serializable {
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
	/**
	 * 账户名称
	 */
	private String userName;
	/**
	 * 活动名称
	 */
	private String jobName;
	/**
	 * 开始时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	/**
	 * 停止时间
	 */
	private Date stopTime;
	/**
	 * CPC
	 */
	private BigDecimal cpc;
	/**
	 * CPM
	 */
	private BigDecimal cpm;
	/**
	 * 消耗金额
	 */
	private BigDecimal consumeMoney;
	/**
	 * 点击次数
	 */
	private BigDecimal clicks;
	/**
	 * 点击率
	 */
	private BigDecimal clicksRate;
	/**
	 * 曝光次数
	 */
	private BigDecimal exposures;
	/**
	 * 到达次数
	 */
	private BigDecimal arrives;
	/**
	 * 到达率
	 */
	private BigDecimal arrivesRate;
	/**
	 * 每次刷新点击次数
	 */
	private BigDecimal oneUpdateClicks;
	/**
	 * 每次刷新曝光次数
	 */
	private BigDecimal oneUpdateExposures;
	/**
	 * 每次刷新花费金额
	 */
	private BigDecimal oneUpdateConsumeMoney;
	/**
	 * 每次刷新到达次数
	 */
	private BigDecimal oneUpdateArrives;
	/**
	 * 创建年份
	 */
	private Integer createYear;
	/**
	 * 创建月份
	 */
	private Integer createMonth;
	/**
	 * 创建日期
	 */
	private Integer createDay;
	/**
	 * 创建人ID
	 */
	private Long createId;
	/**
	 * 创建人名称
	 */
	private String createName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 客户端类型
	 */
	private int clientType;

	private Long capitalId;

	private String jobId;

	private Integer statu;

	private Integer delFlag;

	private BigDecimal endMoney;

}

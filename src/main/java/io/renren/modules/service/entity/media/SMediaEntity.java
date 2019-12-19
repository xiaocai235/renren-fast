package io.renren.modules.service.entity.media;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 媒体
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-10 09:44:07
 */
@Data
@TableName("s_media")
public class SMediaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 客户端类型
	 */
	private Integer clientType;
	/**
	 * 广告平台
	 */
	private String advertisingPlatform;
	/**
	 * 媒体类型
	 */
	private String mediaType;
	/**
	 * 媒体名称
	 */
	private String mediaName;
	/**
	 * 广告位
	 */
	private String advertisingSpace;
	/**
	 * 尺寸
	 */
	private String advertisingSize;
	/**
	 * 尺寸要求
	 */
	private String sizeRequirement;
	/**
	 * 流量类型
	 */
	private Integer trafficType;
	/**
	 * 广告位类型
	 */
	private Integer advertisingSpaceType;
	/**
	 * 日点击量
	 */
	private Integer volumeOfExhibition;
	/**
	 * 是否可以重定向
	 */
	private Integer redirectType;
	/**
	 * 链接地址
	 */
	private String url;
	/**
	 * 下放 1表示通用，2表示不通用
	 */
	private Integer isComment;
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

}

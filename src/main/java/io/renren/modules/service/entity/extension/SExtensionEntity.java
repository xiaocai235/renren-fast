package io.renren.modules.service.entity.extension;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 推广页面表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 17:25:40
 */
@Data
@TableName("s_extension")
public class SExtensionEntity implements Serializable {
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
	 * 推广页名称
	 */
	private String extensionName;
	/**
	 * 推广页面URL
	 */
	private String extensionUrl;
	/**
	 * 推广页面类型
	 */
	private Integer extensionType;
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

}

package io.renren.modules.service.entity.webank;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-10-25 12:38:10
 */
@Data
@TableName("webank")
public class WebankEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String phone;
	/**
	 * 
	 */
	private String idcard;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String money;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private String ip;

	@TableField(exist = false)
	private String code;

}

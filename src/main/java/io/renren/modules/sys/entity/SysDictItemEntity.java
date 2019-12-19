package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统字典项表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:41
 */
@Data
@TableName("sys_dict_item")
public class SysDictItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 字典ID
	 */
	private String dictId;
	/**
	 * 字典项
	 */
	private String dictItemKey;
	/**
	 * 字典项
	 */
	private String dictItemValue;
	/**
	 * 
	 */
	private Date createTime;

}

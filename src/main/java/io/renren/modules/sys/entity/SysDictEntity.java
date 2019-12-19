package io.renren.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统字典表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-01 11:16:42
 */
@Data
@TableName("sys_dict")
public class SysDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 字典名字
	 */
	private String dictName;
	/**
	 * 字典值
	 */
	private String dictValue;
	/**
	 * 
	 */
	private Date createTime;

}

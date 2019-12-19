package io.renren.modules.service.entity.material;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

/**
 * 文件素材表，文件并不是真正存储到服务器当中，而是将文件上传到文件配置的云存储中
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-02 15:22:56
 */
@Data
@TableName("s_material")
public class SMaterialEntity implements Serializable {
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
	 * 账户姓名
	 */
	private String userName;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 文件URL
	 */
	private String fileUrl;
	/**
	 * 预先审核
	 */
	private Integer preAudit;
	/**
	 * 审核状态
	 */
	private Integer audit;
	/**
	 * 客户端类型
	 */
	private Integer clientType;
	/**
	 * 文件状态
	 */
	private Integer fileState;
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

	private String extensionUrl;

    @TableField(exist = false)
	private ArrayList<String> urls;

}

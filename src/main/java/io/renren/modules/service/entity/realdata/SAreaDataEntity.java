package io.renren.modules.service.entity.realdata;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实时数据附表 主要用来存储地区分布数据
 * 
 * @author chenshun
 * @email 2330016764@qq.com
 * @date 2019-07-19 10:15:22
 */
@Data
@TableName("s_area_data")
public class SAreaDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 实时数据ID
	 */
	private Long sRealDataId;
	/**
	 * 数据
	 */
	private String data;

}

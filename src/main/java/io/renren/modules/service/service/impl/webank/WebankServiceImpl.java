package io.renren.modules.service.service.impl.webank;

import io.renren.modules.service.dao.webank.WebankDao;
import io.renren.modules.service.entity.webank.WebankEntity;
import io.renren.modules.service.service.webank.WebankService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;



@Service("webankService")
public class WebankServiceImpl extends ServiceImpl<WebankDao, WebankEntity> implements WebankService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebankEntity> page = this.page(
                new Query<WebankEntity>().getPage(params),
                new QueryWrapper<WebankEntity>()
        );

        return new PageUtils(page);
    }

}
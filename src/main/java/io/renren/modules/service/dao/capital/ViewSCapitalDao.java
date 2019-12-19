package io.renren.modules.service.dao.capital;

import io.renren.modules.service.entity.capital.ViewSCapitalEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ViewSCapitalDao {
    List<ViewSCapitalEntity> summary(Long userId);
}

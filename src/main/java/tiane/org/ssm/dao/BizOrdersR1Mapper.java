package tiane.org.ssm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import tiane.org.ssm.entity.BizOrdersR1;

import java.util.List;
import java.util.Map;

@Repository
public interface BizOrdersR1Mapper extends BaseMapper<BizOrdersR1> {
    List<BizOrdersR1> selectMyPage(Map<String, Object> map);
}
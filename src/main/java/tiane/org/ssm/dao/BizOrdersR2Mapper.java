package tiane.org.ssm.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Repository;
import tiane.org.ssm.entity.BizOrdersR2;

import java.util.List;
import java.util.Map;

@Repository
public interface BizOrdersR2Mapper extends BaseMapper<BizOrdersR2> {

    List<BizOrdersR2> selectMyPage(Map map);
}
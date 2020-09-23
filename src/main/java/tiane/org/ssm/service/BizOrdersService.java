package tiane.org.ssm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizOrders;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.vo.BizOrderVO;
import tiane.org.ssm.vo.BizProductsVO;
import tiane.org.ssm.vo.BizRtnTicketVO;

import java.util.List;
import java.util.Map;

/**
 * 订单接口
 * @author dhl
 */
public interface BizOrdersService extends IService<BizOrders> {
    /**
     * 订单分页查询
     * @param page
     * @param map
     * @return
     */
    IPage<BizOrdersR2> queryPages(Page page, Map<String, String> map);


    /**
     * 保存订单 vo转化为实体并持久化
     * @param vo
     * @return
     */
    boolean saveOrder(BizOrderVO vo);

    /**
     * 退票
     * @param vo
     * @return
     */
    boolean rtnTicket(BizRtnTicketVO vo);

    /**
     * 改签
     *
     * @param orderCode
     * @param vo
     * @return
     */
    boolean updateTicket(String orderCode, BizProductsVO vo);

    /**
     * 根据订单编号查询具体订单s
     * @param orderCode
     * @return
     */
    List<BizOrdersR2> getListByCode(String orderCode);
}

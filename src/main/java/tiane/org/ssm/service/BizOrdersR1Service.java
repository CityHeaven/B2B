package tiane.org.ssm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizOrdersR1;
import tiane.org.ssm.vo.BizOrderR1DetailVO;
import tiane.org.ssm.vo.BizOrderVO;
import tiane.org.ssm.vo.BizRtnTicketVO;

import java.util.List;
import java.util.Map;

/**
 * 国际订单接口
 * @author dhl
 */
public interface BizOrdersR1Service extends IService<BizOrdersR1> {
    /**
     * 订单分页查询
     * @param page
     * @param map
     * @return
     */
    IPage<BizOrdersR1> queryPages(Page page, Map<String, Object> map);


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
     * @param vo
     * @return
     */
    boolean updateTicket(BizRtnTicketVO vo);

    /**
     * 根据订单编号查询具体订单s
     * @param orderCode
     * @return
     */
    List<BizOrdersR1> getListByCode(String orderCode);

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    BizOrderR1DetailVO getOrderDetail(String orderId);
}

package tiane.org.ssm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.vo.BizOrderR2DetailVO;
import tiane.org.ssm.vo.BizOrderVO;
import tiane.org.ssm.vo.BizRtnTicketVO;
import tiane.org.ssm.vo.xc.GetCoopRefundConfirmItem;
import tiane.org.ssm.vo.xc.OpenApiQueryExchangeItem;

import java.util.List;
import java.util.Map;

/**
 * 国内订单接口
 * @author dhl
 */
public interface BizOrdersR2Service extends IService<BizOrdersR2> {
    /**
     * 订单分页查询
     * @param page
     * @param map
     * @return
     */
    IPage<BizOrdersR2> queryPages(Page page, Map<String, Object> map);


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
    List<BizOrdersR2> getListByCode(String orderCode);

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    BizOrderR2DetailVO getOrderDetail(String orderId);

    /*------------ 携程 退票 改签 ------------*/
    void rtnXCTicket(GetCoopRefundConfirmItem getCoopRefundConfirmItem);

    void updateXCTicket(OpenApiQueryExchangeItem getCoopRefundConfirmItem, BizOrdersR2 bizOrdersR2);

    /*------------ 携程 退票 改签 ------------*/
}

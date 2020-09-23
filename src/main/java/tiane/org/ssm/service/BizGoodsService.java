package tiane.org.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tiane.org.ssm.entity.BizGoods;
import tiane.org.ssm.vo.BizProductsVO;
import tiane.org.ssm.vo.xc.AirShoppingRequestVO;

import java.util.List;

public interface BizGoodsService extends IService<BizGoods> {

    /**
     * 查询商品列表
     * @param agency 代理商id
     * @param salesTarget 销售目标
     * @param departure 初始地
     * @param arrival 目的地
     * @param startDate 开始时间
     * @param flightType 国际 国内
     * @return
     */
    List<BizProductsVO> queryOneWayAllTicketsByDate(String agency, String salesTarget,
                                                    String departure, String arrival, String startDate,String flightType);

    /**
     * 处理商品数量
     * @param productId 产品id
     */
    void dealProductNumById(String productId);

    //-------------------------- * XC 携程 * ------------------------------------------------------------------------------------
    /**
     * 查询商品
     * @param airShoppingRequestVO
     * @return
     */
    List<BizGoods> queryProducts4XC(AirShoppingRequestVO airShoppingRequestVO);


    //-------------------------- * XC 携程 * ------------------------------------------------------------------------------------

}

package tiane.org.ssm.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tiane.org.ssm.entity.BizOrdersR2;
import tiane.org.ssm.service.BizOrdersR2Service;
import tiane.org.ssm.vo.BizRtnTicketVO;
import tiane.org.ssm.vo.xc.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
@EnableScheduling   // 1.开启定时任务
public class ScheduleTask {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * JSON类型请求地址:https://flights.ws.ctrip.com/Flight.Order.SupplierOpenAPI/Api/GetCoopRefundConfirmListService.ashx
     * JSON类型请求地址:https://flights.ws.ctrip.com/Flight.Order.SupplierOpenAPI/Api/SearchExchangeList.ashx
     * 请求协议:Http  +  application/json
     */
    private final static String TP_URL = "https://flights.ws.ctrip.com/Flight.Order.SupplierOpenAPI/Api/GetCoopRefundConfirmListService.ashx";
    private final static String GQ_URL = "https://flights.ws.ctrip.com/Flight.Order.SupplierOpenAPI/Api/SearchExchangeList.ashx";


    /**
     * xc处理退票列表查询
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 1000*60*60)  //间隔1小时
    public void first(){
        BizOrdersR2Service bizOrdersR2Service = (BizOrdersR2Service)ApplicationContextUtil.getBean("bizOrdersR2Service");
        LOGGER.info("xc退票列表定时任务开始 : " + LocalDateTime.now().toLocalTime());
        // ping url
        XCTPResult<GetCoopRefundConfirmListRequestType> requestVO = new XCTPResult<>();
        GetCoopRefundConfirmListRequestType requestType = new GetCoopRefundConfirmListRequestType();
        // 拼装请求体
        Date date = new Date();
        Date date2 = new Date();
        requestType.setEndDate(date2);
        date.setHours(date.getHours() - 1);
        requestType.setStartDate(date);
        LOGGER.info("xc退票列表查询的开始时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestType.getStartDate()));
        LOGGER.info("xc退票列表查询的结束时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestType.getEndDate()));
        requestType.setIsInternational(0);// 国内
        requestType.setFeeType("已退票");
        requestType.setOrderBy(1);// 1按起飞时间，2按进单时间
        requestType.setOperateUser("天鹅航空");
        requestVO.setRequestBody(requestType);

        String reponseStr = this.post(JSON.toJSONString(requestVO), TP_URL);
        LOGGER.info("xc退票列表查询的Response: " + reponseStr);
        try{
            GetCoopRefundConfirmListResponseType reponseType = JSONObject.parseObject(reponseStr, GetCoopRefundConfirmListResponseType.class);
            if(reponseType == null){
                LOGGER.info("xc退票列表查询reponseType: null");
                return;
            }
            List<GetCoopRefundConfirmItem> getCoopRefundConfirmListItems = reponseType.getGetCoopRefundConfirmListItems();
            if(getCoopRefundConfirmListItems == null || getCoopRefundConfirmListItems.size() == 0){
                LOGGER.info("xc退票列表查询getCoopRefundConfirmListItems: null");
                return;
            }
            for(GetCoopRefundConfirmItem getCoopRefundConfirmItem:getCoopRefundConfirmListItems){
                // ? 订单号 是不是一开始创建订单传回去的订单号
                Long orderId = getCoopRefundConfirmItem.getOrderId();
                BizOrdersR2 ordersR2 = bizOrdersR2Service.getById(orderId);
                String b2b_orderId = ordersR2.getOrderId();

                QueryWrapper<BizOrdersR2> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_id",b2b_orderId);
//                queryWrapper.eq("is_void","否");
                queryWrapper.eq("order_type","退票");
                List<BizOrdersR2> r2List = bizOrdersR2Service.list(queryWrapper);
                if(r2List != null && r2List.size() > 0){
                    continue;
                }else{
                    // 建立退票
                    bizOrdersR2Service.rtnXCTicket(getCoopRefundConfirmItem);
                }
            }
        }catch (Exception e){
            LOGGER.info("xc退票列表查询Error: " + e.getMessage());
        }
    }


    /**
     * xc处理改签列表查询
     */
    @Scheduled(fixedDelay = 1000*60*60)  //间隔1小时
    public void second(){
        BizOrdersR2Service bizOrdersR2Service = (BizOrdersR2Service)ApplicationContextUtil.getBean("bizOrdersR2Service");
        LOGGER.info("xc改签列表定时任务开始 : " + LocalDateTime.now().toLocalTime());
        // ping url
        XCTPResult<OpenApiQueryExchangeRequest> requestVO = new XCTPResult<>();
        OpenApiQueryExchangeRequest requestType = new OpenApiQueryExchangeRequest();
        // 拼装请求体
        Date date = new Date();
        Date date2 = new Date();
        requestType.setToDateTime(date2);
        date.setHours(date.getHours() - 1);
        requestType.setFromDateTime(date);
        LOGGER.info("xc改签列表查询的开始时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestType.getFromDateTime()));
        LOGGER.info("xc改签列表查询的结束时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(requestType.getToDateTime()));
        requestVO.setRequestBody(requestType);

        String reponseStr = this.post(JSON.toJSONString(requestVO),GQ_URL);
        try{
            // todo 改签
            OpenApiQueryExchangeResponse reponseType = JSONObject.parseObject(reponseStr, OpenApiQueryExchangeResponse.class);
            if(reponseType == null){
                LOGGER.info("xc改签列表查询reponseType: null");
                return;
            }
            List<OpenApiQueryExchangeItem> openApiQueryExchangeItems = reponseType.getOpenApiQueryExchangeItems();
            if(openApiQueryExchangeItems == null || openApiQueryExchangeItems.size() == 0){
                LOGGER.info("xc改签列表查询getCoopRefundConfirmListItems: null");
                return;
            }
            for(OpenApiQueryExchangeItem openApiQueryExchangeItem:openApiQueryExchangeItems){
                // ? 订单号 是不是一开始创建订单传回去的订单号
//                Long orderId = openApiQueryExchangeItem.getRbkId();
//                BizOrdersR2 ordersR2 = bizOrdersR2Service.getById(orderId);
//                String b2b_orderId = ordersR2.getOrderId();
                String passengerName = openApiQueryExchangeItem.getPassengerName();
                String oldTicketNO = openApiQueryExchangeItem.getOldTicketNO();
                QueryWrapper<BizOrdersR2> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("passenger",passengerName);
                queryWrapper2.eq("ticket_no",oldTicketNO);
                queryWrapper2.eq("is_void","否");
                List<BizOrdersR2> list = bizOrdersR2Service.list(queryWrapper2);

                if(list != null && list.size() > 0){
                    BizOrdersR2 bizOrdersR2 = list.get(0);
                    if(!"改期".equals(bizOrdersR2.getOrderType())){
                        // 建立改签
                        bizOrdersR2Service.updateXCTicket(openApiQueryExchangeItem,bizOrdersR2);
                    }
                }else{
                    continue;
                }
            }
        }catch (Exception e){
            LOGGER.info("xc退票列表查询Error: " + e.getMessage());
        }
    }

    public static String post(String json, String url){
        String result = "";
        HttpPost post = new HttpPost(url);
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();

            post.setHeader("Content-Type","application/json;charset=utf-8");
//            post.addHeader("Authorization", "Basic YWRtaW46");
            StringEntity postingString = new StringEntity(json,"utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);

            InputStream in = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber= new StringBuilder();
            String line = null;
            while((line = br.readLine())!=null){
                strber.append(line+'\n');
            }
            br.close();
            in.close();
            result = strber.toString();
            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                result = "服务器异常";
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally{
            post.abort();
        }
        return result;
    }
}

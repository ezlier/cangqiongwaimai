package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;



    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理支付超时订单");

        List<Orders> ordersList = orderMapper.getByStatysAndOrderTImeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if (ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList){
                orderMapper.update(Orders.builder().id(orders.getId()).status(Orders.CANCELLED).cancelReason("支付超时").cancelTime(LocalDateTime.now()).build());
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("处理处于待派送状态的订单");

        List<Orders> ordersList = orderMapper.getByStatysAndOrderTImeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        if (ordersList != null && ordersList.size() > 0){
            for (Orders orders : ordersList){
                orderMapper.update(Orders.builder().id(orders.getId()).status(Orders.COMPLETED).build());
            }
        }
    }
}

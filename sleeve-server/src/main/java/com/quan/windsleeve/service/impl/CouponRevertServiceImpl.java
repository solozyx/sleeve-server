package com.quan.windsleeve.service.impl;

import com.quan.windsleeve.bo.OrderCancelBO;
import com.quan.windsleeve.repository.UserCouponRepository;
import com.quan.windsleeve.service.ICouponRevertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponRevertServiceImpl implements ICouponRevertService {

    private final Logger log = LoggerFactory.getLogger(CouponRevertServiceImpl.class);

    @Autowired
    private UserCouponRepository userCouponRepository;

    /**
     * 在Spring中实现事件的发布和订阅有三个模块：1.事件，2.事件发布器，3.事件监听器
     * 在当前业务场景中，事件：OrderCancelBO，事件发布器：TopicManagerListener类，
     * 事件监听器：使用@EventListener注解的所有方法
     * @EventListener 与方法中的参数 OrderCancelBO 可以唯一的确定一个发布/订阅的响应事件
     * @param orderCancelBO
     */
    @Transactional
    @EventListener
    @Override
    public void revertCoupon(OrderCancelBO orderCancelBO) {
        System.out.println("返还优惠券方法接收到了事件通知");
        Long couponId = orderCancelBO.getCouponId();
        if((couponId != null)) {
           //执行归还优惠券操作
            Long userId = orderCancelBO.getUserId();
            Long orderId = orderCancelBO.getOrderId();
            try {
                userCouponRepository.revertCoupon(userId,orderId,couponId);
                System.out.println("返还优惠券成功");
            } catch (Exception e) {
                log.error("返还优惠券时发生错误，couponId = "+couponId+" userId = "+userId+" orderId = "+orderId);
            }

        }
    }
}

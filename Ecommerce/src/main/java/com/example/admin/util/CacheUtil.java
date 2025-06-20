package com.example.admin.util;

import com.example.admin.entity.Order;
import com.example.admin.entity.OrderDelivery;
import com.example.admin.entity.OrderItem;
import com.example.admin.entity.OrderRefund;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.List;

@Component
@Slf4j
public class CacheUtil {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // 缓存前缀，避免缓存key冲突
    private static final String CATEGORY_PREFIX = "category:";
    private static final String PRODUCT_PREFIX = "product:";
    private static final String SKU_PREFIX = "sku:";
    private static final String ORDER_PREFIX = "order:";
    private static final String ORDER_ITEM_PREFIX = "order:item:";
    private static final String ORDER_DELIVERY_PREFIX = "order:delivery:";
    private static final String ORDER_REFUND_PREFIX = "order:refund:";
    private static final String ORDER_COUNT_PREFIX = "order:count:";

    // 缓存过期时间 (秒)
    private static final long CACHE_EXPIRE_TIME = 3600;

    public CacheUtil(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 将对象放入缓存
     */
    public <T> void setCache(String key, T value) {
        setCache(key, value, CACHE_EXPIRE_TIME);
    }

    /**
     * 将对象放入缓存并设置过期时间
     */
    public <T> void setCache(String key, T value, long timeout) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, timeout, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cache serialization failed", e);
        }
    }

    /**
     * 从缓存中获取对象
     */
    public <T> T getCache(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.readValue(value.toString(), clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cache deserialization failed", e);
        }
    }

    /**
     * 删除缓存
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除缓存 (模式匹配)
     */
    public void deleteCachePattern(String pattern) {
        redisTemplate.delete(redisTemplate.keys(pattern + "*"));
    }

    /**
     * 获取分类缓存key
     */
    public String getCategoryKey(Long categoryId) {
        return CATEGORY_PREFIX + categoryId;
    }

    /**
     * 获取商品缓存key
     */
    public String getProductKey(Long productId) {
        return PRODUCT_PREFIX + productId;
    }

    /**
     * 获取SKU缓存key
     */
    public String getSkuKey(Long skuId) {
        return SKU_PREFIX + skuId;
    }

    /**
     * 获取订单缓存key
     */
    public String getOrderKey(Long orderId) {
        return ORDER_PREFIX + orderId;
    }

    /**
     * 获取订单编号缓存key
     */
    public String getOrderByNoKey(String orderNo) {
        return ORDER_PREFIX + "no:" + orderNo;
    }

    /**
     * 获取订单项缓存key
     */
    public String getOrderItemKey(Long orderId) {
        return ORDER_ITEM_PREFIX + orderId;
    }

    /**
     * 获取订单物流缓存key
     */
    public String getOrderDeliveryKey(Long orderId) {
        return ORDER_DELIVERY_PREFIX + orderId;
    }

    /**
     * 获取订单退款缓存key
     */
    public String getOrderRefundKey(Long orderId) {
        return ORDER_REFUND_PREFIX + orderId;
    }

    /**
     * 获取订单统计缓存key
     */
    public String getOrderCountKey(Integer status) {
        return ORDER_COUNT_PREFIX + (status == null ? "all" : status);
    }

    /**
     * 清除商品相关的所有缓存
     */
    public void clearProductCaches(Long productId) {
        deleteCache(getProductKey(productId));
        deleteCachePattern(SKU_PREFIX + "product:" + productId + ":");
    }

    /**
     * 清除分类相关的所有缓存
     */
    public void clearCategoryCaches(Long categoryId) {
        deleteCache(getCategoryKey(categoryId));
    }

    /**
     * 清理订单相关缓存
     * @param orderId 订单ID
     */
    public void clearOrderCaches(Long orderId) {
        if (orderId == null) return;

        deleteCache(getOrderKey(orderId));
        deleteCache(getOrderItemKey(orderId));
        deleteCache(getOrderDeliveryKey(orderId));
        deleteCache(getOrderRefundKey(orderId));
        // 清理订单计数缓存
        deleteCachePattern(ORDER_COUNT_PREFIX);
    }

    /**
     * 增加订单计数缓存
     */
    public void incrementOrderCount(Integer status) {
        String key = getOrderCountKey(status);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            // 如果是第一次设置，添加过期时间
            redisTemplate.expire(key, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }

        // 同时增加总订单数
        if (status != null) {
            key = getOrderCountKey(null);
            count = redisTemplate.opsForValue().increment(key);
            if (count == 1) {
                redisTemplate.expire(key, CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 减少订单计数缓存
     */
    public void decrementOrderCount(Integer status) {
        String key = getOrderCountKey(status);
        Long count = redisTemplate.opsForValue().decrement(key);
        if (count == 0) {
            // 如果计数为0，删除缓存
            deleteCache(key);
        }

        // 同时减少总订单数
        if (status != null) {
            key = getOrderCountKey(null);
            redisTemplate.opsForValue().decrement(key);
        }
    }

    /**
     * 更新订单缓存
     * @param order 订单实体
     */
    public void updateOrderCache(Order order) {
        if (order == null) return;

        // 更新订单缓存
        setCache(getOrderKey(order.getId()), order);
        setCache(getOrderByNoKey(order.getOrderNo()), order.getId());
    }

    /**
     * 更新订单项缓存
     * @param orderId 订单ID
     * @param orderItems 订单项列表
     */
    public void updateOrderItemCache(Long orderId, List<? extends Object> orderItems) {
        if (orderId == null || orderItems == null || orderItems.isEmpty()) return;
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            setCache(getOrderItemKey(orderId), 
                objectMapper.writeValueAsString(orderItems));
        } catch (Exception e) {
            // 序列化异常，忽略
            log.error("更新订单项缓存失败", e);
        }
    }

    /**
     * 更新订单物流缓存
     * @param orderDelivery 订单物流实体
     */
    public void updateOrderDeliveryCache(OrderDelivery orderDelivery) {
        if (orderDelivery == null) return;

        setCache(getOrderDeliveryKey(orderDelivery.getOrderId()), orderDelivery);
    }

    /**
     * 更新订单退款缓存
     * @param orderRefund 订单退款实体
     */
    public void updateOrderRefundCache(OrderRefund orderRefund) {
        if (orderRefund == null) return;

        setCache(getOrderRefundKey(orderRefund.getOrderId()), orderRefund);
    }
}

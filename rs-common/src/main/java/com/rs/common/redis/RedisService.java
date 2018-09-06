package com.rs.common.redis;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * reids相关封装类
 * 以s开头的表示是opsForSet()下的set集合方法，以z开拓的表示是opsForZSet()下的SortedSet有序集合对应的方法
 * 以l开头的表示的是opsForList()下的list列表方法
 *
 * @author liegou
 * @date
 */
@Component
@Service
public class RedisService {

    private static Logger log = LoggerFactory.getLogger(RedisService.class);
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 设置 value 过期时间
     *
     * @param key
     * @param value
     * @param seconds 以秒为单位
     */
    public synchronized void set(String key, String value, Long seconds) {
        try {
            value = StringUtils.isBlank(value) ? "" : value;
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Set keyex error : " + e);
        }
    }

    /**
     * 设置 value
     *
     * @param key
     * @param value
     */
    public synchronized void set(String key, String value) {
        try {
            value = StringUtils.isBlank(value) ? "" : value;
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Set keyex error : " + e);
        }
    }

    /**
     * 获取value值
     *
     * @param key
     * @return value
     */
    public synchronized String get(String key) {
        String result = redisTemplate.opsForValue().get(key) == null ? null : redisTemplate.opsForValue().get(key).toString();
        return result;
    }

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * <p>
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * <p>
     * 当 key 不是集合类型时，返回一个错误。
     *
     * @param key
     * @return value
     */
    public synchronized void sadd(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 返回集合 key 的基数(集合中元素的数量)
     *
     * @param key
     */
    public synchronized Long ssize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size;
    }

    /**
     * 更新 key 有效时间
     *
     * @param key
     * @param timeOut 以秒为单位
     */
    public synchronized void expire(String key, Long timeOut) {
        try {
            redisTemplate.expire(key, timeOut, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Set keyex error : " + e);
        }
    }

    /**
     * 返回集合 key 的基数(集合中元素的数量)
     *
     * @param key
     */
    public synchronized void del(String key) {
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove("");
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * <p>
     * 当 key 不是集合类型，返回一个错误。
     *
     * @param key
     */
    public synchronized void srem(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    /**
     * 返回集合 key 中的所有成员。
     * <p>
     * 不存在的 key 被视为空集合。
     *
     * @param key
     */
    public synchronized Set smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * <p>
     * count 的值可以是以下几种：
     * <p>
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     *
     * @param key
     */
    public synchronized void lrem(String key, Long count, String value) {
        redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 返回列表 key 的长度。
     * <p>
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
     * <p>
     * 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     */
    public synchronized Long llen(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * <p>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <p>
     * 注意LRANGE命令和编程语言区间函数的区别
     * <p>
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
     * <p>
     * 超出范围的下标
     * <p>
     * 超出范围的下标值不会引起错误。
     * <p>
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * <p>
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     *
     * @param key
     */
    public synchronized List lrange(String key, Long start, Long stop) {
        return redisTemplate.opsForList().range(key, start, stop);
    }

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * <p>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
     * <p>
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * <p>
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key
     */
    public synchronized Long lpush(String key, String values) {
        try {
            return redisTemplate.opsForList().leftPush(key, values);
        } catch (Exception e) {
            log.error("lpush error : " + e);
        }
        return 0L;
    }
}

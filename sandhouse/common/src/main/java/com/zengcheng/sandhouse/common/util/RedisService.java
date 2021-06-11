package com.zengcheng.sandhouse.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redis基本工具类
 * @author zengcheng
 * @date 2019/07/02
 */
@Service
public  class RedisService {

    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisService.redisTemplate = redisTemplate;
    }

    private final static String APPEND_CHAR = "_";


    /***********************************************/
    /************** Hash 存储操作区 *****************/
    /***********************************************/

    public static class Hash {
        /**
         * Hash缓存对象
         * @param prefixKey HashTable的主键
         * @param value     要缓存的对象(最终对象会被转为JSON字符串存储)
         * @param keys      对象的唯一Key值,可以由多个参数组成
         */
        public static void cacheHashObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheHashObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <cacheHashObject> but found <value> is Null, cancel operate===");
                return;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <cacheHashObject> but found <keys...> is Null, please assign unique key===");
                return;
            }

            redisTemplate.opsForHash().put(prefixKey, assemblyUniqueKey("",keys), formatValue2String(value));
        }



        /**
         * 将Map中所有的数据都缓存到HashTable中
         * @param prefixKey
         * @param map
         */
        public static void chacheHashObject(String prefixKey, Map map){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <chacheHashObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (Objects.isNull(map)){
                logger.error("===Redis <chacheHashObject> but found <map> is Null, cancel operate===");
                return;
            }
            redisTemplate.opsForHash().putAll(prefixKey, map);
        }

        /**
         * 从HashTable中获取缓存对象
         * @param prefixKey HashTable的主键
         * @param cls       返回对象的类
         * @param <T>       返回对象类型
         * @return T
         */
        public static <T> T getHashObject(String prefixKey, Class<T> cls, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getHashObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <getHashObject> but found <keys...> is Null, please assign unique key===");
                return null;
            }

            Object cacheVal = redisTemplate.opsForHash().get(prefixKey, assemblyUniqueKey("", keys));
            if (Objects.isNull(cacheVal)){
                return null;
            } else {
                return Jackson2ObjectMapperBuilder.json().build().convertValue(cacheVal.toString(), cls);
            }
        }

        public static <T> T getHashObjectWithNoSplit(String prefixKey, Class<T> cls, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getHashObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <getHashObject> but found <keys...> is Null, please assign unique key===");
                return null;
            }

            Object cacheVal = redisTemplate.opsForHash().get(prefixKey, assemblyUniqueKeyWithNoSplit("", keys));
            if (Objects.isNull(cacheVal)){
                return null;
            } else {
                return Jackson2ObjectMapperBuilder.json().build().convertValue(cacheVal.toString(), cls);
            }
        }

        /**
         * 从HashTable中获取缓存所有的Value值
         * @param prefixKey HashTable的主键
         * @return T
         */
        public static java.util.List getHashAllValue(String prefixKey){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getHashObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForHash().values(prefixKey);
        }

        /**
         * 从HashTable中获取缓存所有的Value值,Key值集合
         * @param prefixKey HashTable的主键
         * @return T
         */
        public static java.util.Set getHashAllKey(String prefixKey){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getHashObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForHash().keys(prefixKey);
        }

        /**
         * 判断指定的HashTable中是否存在指定的Key值
         * @param prefixKey  HashTable的主键
         * @param keys       对象的唯一Key值,可以由多个参数组成
         * @return boolean
         */
        public static boolean containsHashObject(String prefixKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <containsHashObject> but found <prefixKey> is Empty, cancel operate===");
                return false;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <containsHashObject> but found <keys...> is Null, please assign unique key===");
                return false;
            }

            return redisTemplate.opsForHash().hasKey(prefixKey, assemblyUniqueKey("", keys));
        }

        /**
         * 删除HashTable中的key
         * @param prefixKey HashTable的主键
         * @param keys 对象的唯一Key值,可以由多个参数组成
         * @return
         */
        public static boolean deleteHashObject(String prefixKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <deleteHashObject> but found <prefixKey> is Empty, cancel operate===");
                return false;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <deleteHashObject> but found <keys...> is Null, please assign unique key===");
                return false;
            }
            redisTemplate.opsForHash().delete(prefixKey, assemblyUniqueKey("", keys));
            return true;
        }

        /**
         * HashTable中增加
         * @param prefixKey
         * @param increment
         * @param keys
         * @return
         */
        public static Long incrementHashObject(String prefixKey, Long increment, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <incrementHashObject> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <incrementHashObject> but found <keys...> is Null, please assign unique key===");
                return 0L;
            }
            if (Objects.isNull(increment)){
                logger.error("===Redis <incrementHashObject> but found <increment> is Null, please pass increment count===");
                return 0L;
            }
            /*if (redisTemplate.opsForHash().hasKey(prefixKey, assemblyUniqueKey("", keys))){*/
                return redisTemplate.opsForHash().increment(prefixKey, assemblyUniqueKey("", keys), increment.longValue());
            /*}else{
                redisTemplate.opsForHash().put(prefixKey, assemblyUniqueKey("", keys), increment.toString());
                return increment;
            }*/
        }

    }


    /***********************************************/
    /************** Value 存储操作区 *****************/
    /***********************************************/

    public static class Value{

        /**
         * 缓存Key-Value形式
         * @param prefixKey
         * @param value
         * @param keys
         */
        public static void cacheValueObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheValueObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <cacheValueObject> but found <value> is Null, cancel operate===");
                return;
            }
            redisTemplate.opsForValue().set(assemblyUniqueKey(prefixKey,keys), formatValue2String(value));
        }

        public static void cacheValueObjectWithNoSplit(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheValueObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <cacheValueObject> but found <value> is Null, cancel operate===");
                return;
            }
            redisTemplate.opsForValue().set(assemblyUniqueKeyWithNoSplit(prefixKey, keys), formatValue2String(value));
        }

        /**
         * 获取Key-Value缓存
         * @param prefixKey
         * @param cls
         * @param keys
         * @param <T>
         * @return
         */
        public static <T> T getValueObject(String prefixKey, Class<T> cls, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getValueObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            Object valueObj = redisTemplate.opsForValue().get(assemblyUniqueKey(prefixKey, keys));
            if (Objects.isNull(valueObj)){
                return null;
            }else{
                return Jackson2ObjectMapperBuilder.json().build().convertValue(valueObj.toString(), cls);
            }
        }

        /**
         * 获取Key-Value缓存 (拼接key时无下划线)
         * @param prefixKey
         * @param cls
         * @param keys
         * @param <T>
         * @return
         */
        public static <T> T getValueObjectWithSplit(String prefixKey, Class<T> cls, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getValueObject> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            Object valueObj = redisTemplate.opsForValue().get(assemblyUniqueKeyWithNoSplit(prefixKey, keys));
            if (Objects.isNull(valueObj)){
                return null;
            }else{
                return Jackson2ObjectMapperBuilder.json().build().convertValue(valueObj.toString(), cls);
            }
        }

        /**
         * Key-Value自增
         * @param prefixKey
         * @param increment
         * @param keys
         * @return
         */
        public static Long incrementValueObject(String prefixKey, Long increment, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <incrementValueObject> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <incrementValueObject> but found <keys...> is Null, please assign unique key===");
                return 0L;
            }
            if (Objects.isNull(increment)){
                logger.error("===Redis <incrementValueObject> but found <increment> is Null, please pass increment count===");
                return 0L;
            }
            return redisTemplate.opsForValue().increment(assemblyUniqueKey(prefixKey, keys), increment.longValue());
        }

        /**
         * Key-Value自增
         * @param prefixKey
         * @param increment
         * @param keys
         * @return
         */
        public static Long incrementValueObjectWithNoSplit(String prefixKey, Long increment, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <incrementValueObject> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (Objects.isNull(keys) || keys.length<=0){
                logger.error("===Redis <incrementValueObject> but found <keys...> is Null, please assign unique key===");
                return 0L;
            }
            if (Objects.isNull(increment)){
                logger.error("===Redis <incrementValueObject> but found <increment> is Null, please pass increment count===");
                return 0L;
            }
            return redisTemplate.opsForValue().increment(assemblyUniqueKeyWithNoSplit(prefixKey, keys), increment.longValue());
        }

    }

    /***********************************************/
    /************** List 存储操作区 *****************/
    /***********************************************/

    public static class List{

        /**
         * 缓存List形式的对象
         * @param prefixKey
         * @param value
         * @param keys
         */
        public static void cacheListObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheListObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <cacheListObject> but found <value> is Null, cancel operate===");
                return;
            }
            redisTemplate.opsForList().rightPush(assemblyUniqueKey(prefixKey,keys), formatValue2String(value));
        }

        /**
         * 缓存连表
         * @param prefixKey
         * @param collection
         * @param keys
         */
        public static void cacheListObject(String prefixKey, Collection collection, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheListObject> but found <prefixKey> is Empty, cancel operate===");
                return;
            }
            if (CollectionUtils.isEmpty(collection)){
                logger.error("===Redis <cacheListObject> but found <collection> is Null, cancel operate===");
                return;
            }

            redisTemplate.opsForList().rightPushAll(assemblyUniqueKey(prefixKey,keys), collection);
        }

        /**
         * 默认从列表的左端弹出数据
         * @param prefixKey
         * @param cls
         * @param keys
         * @param <T>
         * @return
         */
        public static <T> T getListObjectPop(String prefixKey, Class<T> cls, Object ...keys){
            return getListObjectPop(prefixKey, cls, true, keys);
        }

        /**
         * 自定义是从左端还是右端弹出数据,弹出数据代表删除
         * @param prefixKey
         * @param cls
         * @param isLeft
         * @param keys
         * @param <T>
         * @return
         */
        public static <T> T getListObjectPop(String prefixKey, Class<T> cls, boolean isLeft, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getListObjectAndDelete> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            Object valueObj = null;
            if (isLeft){
                valueObj = redisTemplate.opsForList().leftPop(assemblyUniqueKey(prefixKey, keys));
            } else {
                valueObj = redisTemplate.opsForList().rightPop(assemblyUniqueKey(prefixKey, keys));
            }
            if (Objects.isNull(valueObj)){
                return null;
            }else{
                return Jackson2ObjectMapperBuilder.json().build().convertValue(valueObj.toString(), cls);
            }
        }


        /**
         * 获取list的大小
         * @param prefixKey
         * @param keys
         * @return
         */
        public static long getListObjectSize(String prefixKey,  Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getListObjectSize> but found <prefixKey> is Empty, cancel operate===");
                return 0;
            }
            return redisTemplate.opsForList().size(assemblyUniqueKey(prefixKey, keys));
        }


        /**
         * 获取列表中的指定个数缓存对象
         * @param prefixKey
         * @param start
         * @param length
         * @param keys
         * @return
         */
        public static java.util.List getListObjectRange(String prefixKey, int start, int length, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getListObjectRange> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (length <=0 ){
                logger.error("===Redis <getListObjectRange> but found <length> is 0, cancel operate====");
                return null;
            }
            return redisTemplate.opsForList().range(assemblyUniqueKey(prefixKey, keys), start, length);
        }

    }

    /***********************************************/
    /************** Set 存储操作区 *****************/
    /***********************************************/

    public static class Set{

        /**
         * 向指定的Key中添加数据
         * @param prefixKey
         * @param value
         * @param keys
         */
        public static void cacheSetObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <cacheSetObject> but found <prefixKey> is Empty, cancel operate===");
                return ;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <cacheSetObject> but found <value> is 0, cancel operate====");
                return ;
            }
            redisTemplate.opsForSet().add(assemblyUniqueKey(prefixKey, keys), formatValue2String(value));
        }

        /**
         * 判断指定的对象是否存在于指定的Key中
         * @param prefixKey
         * @param value
         * @param keys
         * @return
         */
        public static boolean containsSetObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <containsSetObject> but found <prefixKey> is Empty, cancel operate===");
                return false;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <containsSetObject> but found <value> is 0, cancel operate====");
                return false;
            }
            return redisTemplate.opsForSet().isMember(assemblyUniqueKey(prefixKey, keys), formatValue2String(value));
        }

        /**
         * 删除Key中指定的值
         * @param prefixKey
         * @param value
         * @param keys
         * @return
         */
        public static boolean deleteSetObject(String prefixKey, Object value, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <deleteSetObject> but found <prefixKey> is Empty, cancel operate===");
                return false;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <deleteSetObject> but found <value> is 0, cancel operate====");
                return false;
            }
            return redisTemplate.opsForSet().remove(assemblyUniqueKey(prefixKey, keys), formatValue2String(value))>0;
        }

        /**
         * 返回指定Key中的所有数据
         * @param prefixKey
         * @param keys
         * @return
         */
        public static java.util.Set getSetAllValue(String prefixKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetAllValue> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().members(assemblyUniqueKey(prefixKey, keys));
        }

        /**
         * 返回指定Key中的数据个数
         * @param prefixKey
         * @param keys
         * @return
         */
        public static Long getSetSize(String prefixKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetSize> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().size(assemblyUniqueKey(prefixKey, keys));
        }

        /**
         * 将制定的Key移动到其他key中
         * @param prefixKey
         * @param value
         * @param destFullKey 目标Set
         * @param keys
         * @return
         */
        public static boolean moveSetValue2DestSet(String prefixKey, Object value, String destFullKey, Object ...keys) throws JsonProcessingException {
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <moveSetValue> but found <prefixKey> is Empty, cancel operate===");
                return false;
            }
            if (Objects.isNull(value)){
                logger.error("===Redis <moveSetValue> but found <value> is Empty, cancel operate===");
                return false;
            }
            if (StringUtils.isEmpty(destFullKey)){
                logger.error("===Redis <moveSetValue> but found <destFullKey> is Empty, cancel operate===");
                return false;
            }
            return redisTemplate.opsForSet().move(assemblyUniqueKey(prefixKey, keys), formatValue2String(value), destFullKey);
        }

        /**
         * 计算指定两个指定Set的数据的差集,得出前一个Set差集后的数据
         * @param prefixKey
         * @param otherFullKey
         * @param keys
         * @return
         */
        public static java.util.Set getSetDifference(String prefixKey, String otherFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetDifference> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetDifference> but found <otherFullKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().difference(assemblyUniqueKey(prefixKey, keys), otherFullKey);
        }

        /**
         * 计算指定Set与其他几个Set之间的差集,得出前一个Set差集后的数据
         * @param prefixKey
         * @param otherFullKeyList
         * @param keys
         * @return
         */
        public static java.util.Set getSetDifference(String prefixKey, Collection<String> otherFullKeyList, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetDifference> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetDifference> but found <otherFullKeyList> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().difference(assemblyUniqueKey(prefixKey, keys), otherFullKeyList);
        }

        /**
         * 计算指定两个指定Set的数据的差集,得出前一个Set差集后的数据,并存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKey
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetDifferenceAndStore(String prefixKey, String otherFullKey, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetDifference> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetDifference> but found <otherFullKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetDifference> but found <storeFullKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().differenceAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKey, storeFullKey);
        }

        /**
         * 计算指定Set与其他几个Set之间的差集,得出前一个Set差集后的数据,并存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKeyList
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetDifferenceAndStore(String prefixKey, Collection<String> otherFullKeyList, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetDifference> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetDifference> but found <otherFullKeyList> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetDifference> but found <storeFullKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().differenceAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKeyList, storeFullKey);
        }


        /**
         * 计算指定两个指定Set的数据的交集
         * @param prefixKey
         * @param otherFullKey
         * @param keys
         * @return
         */
        public static java.util.Set getSetIntersect(String prefixKey, String otherFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetIntersect> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetIntersect> but found <otherFullKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().intersect(assemblyUniqueKey(prefixKey, keys), otherFullKey);
        }

        /**
         * 计算指定Set与其他几个Set之间的交集
         * @param prefixKey
         * @param otherFullKeyList
         * @param keys
         * @return
         */
        public static java.util.Set getSetIntersect(String prefixKey, Collection<String> otherFullKeyList, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetIntersect> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetIntersect> but found <otherFullKeyList> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().intersect(assemblyUniqueKey(prefixKey, keys), otherFullKeyList);
        }

        /**
         * 计算指定Set与其他几个Set之间的交集并存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKeyList
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetIntersectAndStore(String prefixKey, Collection<String> otherFullKeyList, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetIntersect> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetIntersect> but found <otherFullKeyList> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetIntersect> but found <storeFullKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().intersectAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKeyList, storeFullKey);
        }

        /**
         * 计算指定两个指定Set的数据的交集并存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKey
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetIntersectAndStore(String prefixKey, String otherFullKey, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetIntersect> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetIntersect> but found <otherFullKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetIntersect> but found <storeFullKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().intersectAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKey, storeFullKey);
        }



        /**
         * 计算指定两个指定Set的数据的交集
         * @param prefixKey
         * @param otherFullKey
         * @param keys
         * @return
         */
        public static java.util.Set getSetUnion(String prefixKey, String otherFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetUnion> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetUnion> but found <otherFullKey> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().union(assemblyUniqueKey(prefixKey, keys), otherFullKey);
        }

        /**
         * 计算指定Set与其他几个Set之间的交集
         * @param prefixKey
         * @param otherFullKeyList
         * @param keys
         * @return
         */
        public static java.util.Set getSetUnion(String prefixKey, Collection<String> otherFullKeyList, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetUnion> but found <prefixKey> is Empty, cancel operate===");
                return null;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetUnion> but found <otherFullKeyList> is Empty, cancel operate===");
                return null;
            }
            return redisTemplate.opsForSet().union(assemblyUniqueKey(prefixKey, keys), otherFullKeyList);
        }

        /**
         * 计算指定两个指定Set的数据的交集存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKey
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetUnionAndStore(String prefixKey, String otherFullKey, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetUnion> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(otherFullKey)){
                logger.error("===Redis <getSetUnion> but found <otherFullKey> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetUnion> but found <storeFullKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().unionAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKey, storeFullKey);
        }

        /**
         * 计算指定Set与其他几个Set之间的交集存储在storeFullKey中
         * @param prefixKey
         * @param otherFullKeyList
         * @param storeFullKey
         * @param keys
         * @return
         */
        public static Long getSetUnionAndStore(String prefixKey, Collection<String> otherFullKeyList, String storeFullKey, Object ...keys){
            if (StringUtils.isEmpty(prefixKey)){
                logger.error("===Redis <getSetUnion> but found <prefixKey> is Empty, cancel operate===");
                return 0L;
            }
            if (CollectionUtils.isEmpty(otherFullKeyList)){
                logger.error("===Redis <getSetUnion> but found <otherFullKeyList> is Empty, cancel operate===");
                return 0L;
            }
            if (StringUtils.isEmpty(storeFullKey)){
                logger.error("===Redis <getSetUnion> but found <storeFullKey> is Empty, cancel operate===");
                return 0L;
            }
            return redisTemplate.opsForSet().unionAndStore(assemblyUniqueKey(prefixKey, keys), otherFullKeyList, storeFullKey);
        }
    }


    /***********************************************/
    /************** 通用 存储操作区 *****************/
    /***********************************************/

    /**
     * 是否存在指定的key
     * @param prefixKey
     * @param keys
     * @return
     */
    public static boolean containsKey(String prefixKey, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <containsKey> but found <prefixKey> is Empty, cancel operate===");
            return false;
        }
        return redisTemplate.hasKey(assemblyUniqueKey(prefixKey, keys));
    }

    /**
     * 是否存在指定的key
     * @param prefixKey
     * @param keys
     * @return
     */
    public static boolean containsKeyWithNoSplit(String prefixKey, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <containsKey> but found <prefixKey> is Empty, cancel operate===");
            return false;
        }
        return redisTemplate.hasKey(assemblyUniqueKeyWithNoSplit(prefixKey, keys));
    }

    /**
     * 删除指定的key,谨慎操作
     * @param prefixKey
     * @param keys
     */
    public static void deleteKey(String prefixKey, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <deleteKey> but found <prefixKey> is Empty, cancel operate===");
            return;
        }
        redisTemplate.delete(assemblyUniqueKey(prefixKey, keys));
    }
    /**
     * 根据key删除hash表,谨慎操作
     * @param redisKey
     */
    public static void delete(String redisKey){
        if (StringUtils.isEmpty(redisKey)){
            logger.error("===Redis <deleteKey> but found <prefixKey> is Empty, cancel operate===");
            return;
        }
        redisTemplate.delete(redisKey);
    }

    /**
     * 获取指定key值的剩余时间,单位是秒
     * @param prefixKey
     * @param keys
     * @return
     */
    public static long getTtlKey(String prefixKey, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <getTtlKey> but found <prefixKey> is Empty, cancel operate===");
            return 0L;
        }
        return redisTemplate.getExpire(assemblyUniqueKey(prefixKey, keys), TimeUnit.SECONDS);
    }

    /**
     * 获取指定Key值的剩余时间,按照指定单位
     * @param prefixKey
     * @param timeUnit
     * @param keys
     * @return
     */
    public static long getTtlKey(String prefixKey, TimeUnit timeUnit, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <getTtlKey> but found <prefixKey> is Empty, cancel operate===");
            return 0L;
        }
        return redisTemplate.getExpire(assemblyUniqueKey(prefixKey, keys), timeUnit);
    }

    /**
     * 设置指定的Key失效时间
     * @param prefixKey
     * @param timeout
     * @param timeUnit
     * @param keys
     */
    public static boolean expireKey(String prefixKey,long timeout, TimeUnit timeUnit, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <prefixKey> but found <prefixKey> is Empty, cancel operate===");
            return false;
        }
        return redisTemplate.expire(assemblyUniqueKey(prefixKey, keys), timeout, timeUnit);
    }

    /**
     * 设置指定的Key值失效时间点
     * @param prefixKey
     * @param expireDate
     * @param keys
     * @return
     */
    public static boolean expireAtKey(String prefixKey, Date expireDate, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <expireAtKey> but found <prefixKey> is Empty, cancel operate===");
            return false;
        }
        return redisTemplate.expireAt(assemblyUniqueKey(prefixKey, keys), expireDate);
    }

    /**
     * 获取匹配的Ke值Set集合
     * @param prefixKey
     * @param keys
     * @return
     */
    public static java.util.Set getKeysLike(String prefixKey, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <getKeysLike> but found <prefixKey> is Empty, cancel operate===");
            return null;
        }
        return getKeysLike(prefixKey, LIKE.LIKE, keys);
    }

    /**
     * 获取匹配的Ke值Set集合
     * @param prefixKey
     * @param keys
     * @return
     */
    public static java.util.Set getKeysLike(String prefixKey, Enum like, Object ...keys){
        if (StringUtils.isEmpty(prefixKey)){
            logger.error("===Redis <getKeysLike> but found <prefixKey> is Empty, cancel operate===");
            return null;
        }
        if (LIKE.LEFT_LIKE == like){
            return redisTemplate.keys(assemblyUniqueKey(prefixKey, "*", keys));
        } else if (LIKE.RIGHT_LIKE == like){
            return redisTemplate.keys(assemblyUniqueKey(prefixKey, keys, "*"));
        }else {
            return redisTemplate.keys(assemblyUniqueKey(prefixKey, "*", keys, "*"));
        }
    }

    enum LIKE{
        /**
         * 左匹配
         */
        LEFT_LIKE,
        /**
         * 友匹配
         */
        RIGHT_LIKE,
        /**
         * 全匹配
         */
        LIKE
    }

    /**
     * 将要缓存的对象全部转换为相应的JSON字符串
     * @param value
     * @return
     */
    private static String formatValue2String(Object value) throws JsonProcessingException {
        if(value instanceof Long
                || value instanceof Integer
                || value instanceof Double
                || value instanceof String) {
            return value.toString();
        } else if(value instanceof Collection) {
            return Jackson2ObjectMapperBuilder.json().build().writeValueAsString(value);
        } else {
            return Jackson2ObjectMapperBuilder.json().build().writeValueAsString(value);
        }
    }

    /**
     * 组装Key值
     * @param keys
     * @return
     */
    public static String assemblyUniqueKey(String prefixKey, Object ...keys){
        StringBuffer uniqueKey = new StringBuffer(APPEND_CHAR).append(String.valueOf(prefixKey)).append(APPEND_CHAR);
        for (int i=0; i<keys.length; i++){
            uniqueKey.append(keys[i]).append(APPEND_CHAR);
        }
        return uniqueKey.toString();
    }


    public static String assemblyUniqueKeyWithNoSplit(String prefixKey, Object ...keys){
        StringBuffer uniqueKey = new StringBuffer(prefixKey);
        for (int i=0; i<keys.length; i++){
            uniqueKey.append(keys[i]);
        }
        return uniqueKey.toString();
    }
}

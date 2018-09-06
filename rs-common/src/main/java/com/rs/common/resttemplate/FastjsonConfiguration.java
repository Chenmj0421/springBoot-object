package com.rs.common.resttemplate;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spring的json转换器默认使用的是Jackson，json字符串和对应的Entity如果有字段对不上就会报错
 *
 * 而FastJson则不会报错，所以用FastJSON替换默认的Jackson
 * @author liegou
 */
@Configuration
public class FastjsonConfiguration {


    @Bean
    public HttpMessageConverters fastjsonConverter() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //自定义格式化输出
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero);
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}

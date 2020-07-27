package org.v.component.springcloud.config;

import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;

import java.util.TimeZone;

/**
 * @author : daiguodong@viomi.com.cn
 * @createtime :  2020年07月27日 18:00
 * @description : Feign客户端配置
 */
@Configuration
public class FeignClientConfig implements FeignFormatterRegistrar
{
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss,SSS";
    
    @Override
    public void registerFormatters(FormatterRegistry registry)
    {
        DateFormatter dateFormatter = new DateFormatter(DATE_PATTERN);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        registry.addFormatter(dateFormatter);
    }
}

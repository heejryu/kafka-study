package co.kr.kafkastudy.common.config;

import co.kr.kafkastudy.common.utils.DateUtils;
import co.kr.kafkastudy.common.utils.FileUtils;
import co.kr.kafkastudy.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public RedisUtils redisUtils() { return new RedisUtils();}

    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }

    @Bean
    public FileUtils fileUtils() { return new FileUtils();}
}

package cn.schff.dyvlog.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.pagehelper.PageInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

/**
 * @Author：眭传洪
 * @Create：2023/4/28 0:09
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@SpringBootConfiguration
public class MybatisPlusConfig {

    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }

    @Bean
    PageInterceptor pageInterceptor(){
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "MysqL");
        pageInterceptor.setProperties(properties);  // 由此可进入源码，
        return pageInterceptor;
    }

}

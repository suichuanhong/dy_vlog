package cn.schff.dyvlog.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @Author：眭传洪
 * @Create：2023/4/30 10:53
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@SpringBootConfiguration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.schff.dyvlog.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("抖音Vlog接口")
                .description("用于测试返回界面的一些接口返回值")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}

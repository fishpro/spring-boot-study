package com.fishpro.swagger2;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class Swagger2Config {
    /**
     * 创建一个 Docket 组件 把自定义的 appInfo 装载到里面
     * */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())//指定 apiInfo初始化信息
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//只有 @api 的可以被识别为 swagger 文档
                .paths(PathSelectors.any())//指定路径
                .build();
    }

    /**
     * 定义 api 界面信息元素
     * title 定义了swagger-ui 显示的标题
     * description 定义了 swagger-ui 接口文档
     * version 定义版本号 1.0
     * */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring-Boot-Study-Swaager2 接口文档")
                .description("Spring-Boot-Study-Swaager2  相关接口的文档")
                .termsOfServiceUrl("http://www.fishpro.com.cn")
                .version("1.0")
                .build();
    }
}

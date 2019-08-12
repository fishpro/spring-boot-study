package com.fishpro.webservice.config;

import com.fishpro.webservice.service.UserService;
import com.fishpro.webservice.service.impl.UserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfWebServiceConfig {

    /**
     *
     * */
    @Bean("cxfServletRegistration")
    public ServletRegistrationBean dispatcherServlet(){

        return new ServletRegistrationBean(new CXFServlet(),"/ws/*");
    }
    /**
     * 申明业务处理类 当然也可以直接 在实现类上标注 @Service
     */
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    /*
     * 非必要项
     */
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        SpringBus springBus = new SpringBus();
        return springBus;
    }

    /*
     * 发布endpoint
     */
    @Bean
    public Endpoint endpoint( ) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService());
        endpoint.publish("/user");//发布地址
        return endpoint;
    }
}

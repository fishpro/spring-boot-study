package com.fishpro.resttest;

import com.fishpro.resttest.controller.UserController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

/**
 * 本示例针对 Restful API 风格接口做全面的测试用例
 * fishpro at 2019-07-20
 * 注意事项
 *    1.param(name,value) 只能用于 url 参数传递，form 表单传递
 *    2. @RequestBody 方法，对应使用 .contentType(MediaType.APPLICATION_JSON).content(json 字符串)
 *    3.大部分测试用例失败的原因是传递参数对应的contentType不正确
 * */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;//定义一个 MockMvc

    /**
     * 初始化 MockMvc 通过MockMvcBuilders.standaloneSetup 模拟一个 UserController 测试环境，通过build得到一个MockMvc
     * */
    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }
    /**
     * 测试 Hello World 方法
     * hello 方法是一个 get 方法，使用了 url 参数传递参数 所以使用了 .param 来传递参数
     * accept(MediaType.TEXT_HTML_VALUE) 来设置传递值接收类型
     * */
    @Test
    public void hello() throws  Exception{
       MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/api/user/hello")
                .param("name","fishpro")
                .accept(MediaType.TEXT_HTML_VALUE)) //perform 结束
                .andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
                .andExpect(MockMvcResultMatchers.content().string("Hello fishpro"))//添加断言
                .andDo(MockMvcResultHandlers.print()) //添加执行
                .andReturn();//添加返回

        //下面部分等等与 addExcept 部分
        //int status=mvcResult.getResponse().getStatus();                 //得到返回代码
        //String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
        //Assert.assertEquals(200,status);                        //等于 andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
        //Assert.assertEquals("Hello World",content);            //andExpect(MockMvcResultMatchers.content().string("Hello World"))//添加断言
    }

    /**
     * 测试用户列表获取 /users GET
     * */
    @Test
    public void getUsers() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/users")
                .accept(MediaType.APPLICATION_JSON)) //perform 结束
                .andExpect(MockMvcResultMatchers.status().isOk()) //andExpect
                .andDo(MockMvcResultHandlers.print()) //andDo
                .andReturn();//andReturn
    }

    /**
     * 获取单个用户信息 /users/3 GET
     * */
    @Test
    public void getUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/users/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 新增单个用户信息 /users/ POST
     * 注意 addUser 使用了 @RequestBody 方法，对应使用 .contentType(MediaType.APPLICATION_JSON).content(json 字符串)
     * */
    @Test
    public void addUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"userId\": 3,\"userName\": \"tom\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 编辑一个用户 /users/ PUT
     * */
    @Test
    public void editUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/users/3")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"userId\": 3,\"userName\": \"tom\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 删除一个用户 /users/ DELETE
     * */
    @Test
    public void deleteUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/users/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}

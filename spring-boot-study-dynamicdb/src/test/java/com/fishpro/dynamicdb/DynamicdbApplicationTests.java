package com.fishpro.dynamicdb;

import com.fishpro.dynamicdb.service.DynamicDataSourceTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicdbApplicationTests {

    @Autowired
    private DynamicDataSourceTestService dynamicDataSourceTestService;

    /**
     * 观察三个数据源中的数据是否正确
     * */
    @Test
    public void testDaynamicDataSource(){
        Long id = 1L;

        dynamicDataSourceTestService.updateDemoTest(id);
        dynamicDataSourceTestService.updateDemoTestBySlave1(id);
        dynamicDataSourceTestService.updateDemoTestBySlave2(id);
    }

}

package com.fishpro.dynamicdb.service;

import com.fishpro.dynamicdb.dao.DemoTestDao;
import com.fishpro.dynamicdb.datasource.annotation.DataSource;
import com.fishpro.dynamicdb.domain.DemoTestDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试多数据源
 *
 */
@Service
//@DataSource("slave1")
public class DynamicDataSourceTestService {
    @Autowired
    private DemoTestDao demoTestDao;

    @Transactional
    public void updateDemoTest(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000000");
        demoTestDao.update(user);
    }

    @Transactional
    @DataSource("slave1")
    public void updateDemoTestBySlave1(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000001");
        demoTestDao.update(user);
    }

    @DataSource("slave2")
    @Transactional
    public void updateDemoTestBySlave2(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000002");
        demoTestDao.update(user);

        //测试事物
//        int i = 1/0;
    }
}

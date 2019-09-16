package com.fishpro.dynamicdb.service.impl;

import com.fishpro.dynamicdb.dao.DemoTestDao;
import com.fishpro.dynamicdb.domain.DemoTestDO;
import com.fishpro.dynamicdb.service.DemoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service
public class DemoTestServiceImpl implements DemoTestService {
	@Autowired
	private DemoTestDao demoTestDao;
	
	@Override
	public DemoTestDO get(Long id){
		return demoTestDao.get(id);
	}
	
	@Override
	public List<DemoTestDO> list(Map<String, Object> map){
		return demoTestDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return demoTestDao.count(map);
	}
	
	@Override
	public int save(DemoTestDO demoTest){
		return demoTestDao.save(demoTest);
	}
	
	@Override
	public int update(DemoTestDO demoTest){
		return demoTestDao.update(demoTest);
	}
	
	@Override
	public int remove(Long id){
		return demoTestDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return demoTestDao.batchRemove(ids);
	}
	
}

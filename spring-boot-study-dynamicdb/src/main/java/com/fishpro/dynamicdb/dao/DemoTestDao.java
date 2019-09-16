package com.fishpro.dynamicdb.dao;


import java.util.List;
import java.util.Map;

import com.fishpro.dynamicdb.domain.DemoTestDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author fishpro
 * @email fishpro@vip.qq.com
 * @date 2019-09-07 09:51:38
 */
@Mapper
public interface DemoTestDao {

	DemoTestDO get(Long id);
	
	List<DemoTestDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DemoTestDO demoTest);
	
	int update(DemoTestDO demoTest);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}

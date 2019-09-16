package com.fishpro.dynamicdb.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author fishpro
 * @email fishpro@vip.qq.com
 * @date 2019-09-07 09:51:38
 */
public class DemoTestDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//
	private String name;
	//
	private Integer status;
	//
	private Integer isDeleted;
	//
	private Date createTime;
	//
	private Long createUserId;
	//
	private Long age;
	//
	private String content;
	//
	private String body;
	//
	private String title;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：
	 */
	public Long getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：
	 */
	public void setAge(Long age) {
		this.age = age;
	}
	/**
	 * 获取：
	 */
	public Long getAge() {
		return age;
	}
	/**
	 * 设置：
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * 获取：
	 */
	public String getBody() {
		return body;
	}
	/**
	 * 设置：
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：
	 */
	public String getTitle() {
		return title;
	}


}

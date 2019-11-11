package com.augurit.aplanmis.integration.license.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @author Administrator
 */
@Data
public class LicenseUserInfoDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 账户名称。标识属性。
	 */
	private String account;
	
	/**
	 * 用户姓名
	 */
	private String name;
	
	/**
	 * 身份证件号码
	 */
	private String identity_num;
	
	/**
	 * 角色。如“窗口受理人员”
	 */ 
	private String role;
	
	/**
	 * 行政区划名称。只读属性。
	 */
	private String division;
	
	
	/**
	 * 行政区划代码。只读属性。
	 */
	private String division_code;
	
	/**
	 * 所属机构。只读属性。(组织机构)
	 */
	private String service_org;

    private String service_org_code;


}

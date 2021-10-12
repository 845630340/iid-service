package com.inspur.cloud.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author mysterious guest
 * */
@Getter
@Setter
@ToString
public class ErrorResponseBean {

	public static final String SUCCESS_CODE = "0";

	public static final String DEFAULT_SUCCESS_MESSAGE = "操作成功！";

	public static final String DEFAULT_RESULT = "";

	/**
	 * 返回码 0成功，其他失败
	 */
	private String code;

	/**
	 * 描述信息
	 */
	private String message;


	private String requestId;

	public ErrorResponseBean(String code, String message, String requestId) {
		this.code = code;
		this.message = message;
		this.requestId = requestId;
	}

	public ErrorResponseBean() {}

}

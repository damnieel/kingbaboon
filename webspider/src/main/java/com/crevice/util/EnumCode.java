package com.crevice.util;

/**
 * 枚举
 * BaseBusinessCode.java
 * <BR>
 * 创建人:xiaohui<BR>
 * 时间：2018年12月1日<BR>
 * 状态码：3000起
 * @version 1.0.0
 *
 */
public enum EnumCode {
	
	/**登录相关**/
	NOT_LOGIN(2000,"未登录"),
	ACCOUNT_NOT_EXIT(2001,"帐号不存在"),
	CODE_ERROR(2002,"验证码错误"),
	PHONE_CODE_ERROR(2003,"手机校验码错误"),
	PHONE_SEND_ERROR(2004,"发送手机校验码失败"),
	ACCOUNT_FORBIDDEN(2005,"帐号禁止登录系统"),
	USER_DETAIL_NOT_COMPLETE(2006,"用户信息不完整"),
	PHONE_NUM_INVALID(2007,"手机号码已失效"),
	SEND_PHONE_CODE_MUCH(2008,"手机发送次数过多"),
	ILLEGAL_REQUEST(2009,"非法访问"),
	ACCOUNT_NOT_LEGAL(2010,"帐号不合法"),
	PWD_IS_ERROR(2011,"密码错误"),
	OTHER_LOGININ(2012,"其他地点登录"),
	PASSWORD_CHANGED(2013,"密码已修改"),
	
	/**公共**/
	PARAM_IS_NULL(3001,"参数为空"),
	CACHE_ERROR(3002,"缓存错误"),
	PARAM_IS_ERROR(3003,"参数错误"),
	OPERATION_ERROR(3004,"操作失败"),
	
	/** 上传 **/
	IS_NOT_IMAGE(6001,"上传不是图片"),
	IMAGE_TOO_BIG(6002,"上传图片太大"),
	UPLOAD_IMAGE_SUCCESS(6003,"图片上传成功"),
	UPLOAD_IMAGE_FAIL(6004,"图片大小超过1M"),
	UPDATE_IMAGE_SUCCESS(6005,"图片修改成功"),
	UPDATE_IMAGE_FAIL(6006,"图片修改失败"),
	THE_LAST_ONE(6007,"最后一张图片不能删除"),
	OLD_IMG_NOT_EXIT(6008,"原图片不存在"),
	IMG_NUM_OVER_STEP(6009,"上传图片数量超出规定"),
	/**数据库操作相关**/
	INSERT_ORGANIZATION_ERROR(7001,"添加版块错误"),
	SEARCH_NOT_FOUND(7002,"暂无数据"),
	UPDATE_FAIL(7003,"更新失败"),
	INSERT_FAIL(7004,"添加失败"),
	UPDATE_ORGANIZATION_ERROR(7005,"修改版块错误"),
	
	/**版块操作**/
	SECTION_TOPNUMOVER(8001,"首页版块数超出"),
	ADD_CATEGORY_SUCCESS(8002,"添加版块分类成功"),
	MODIFY_CATEGORY_SUCCESS(8003,"修改版块分类成功"),
	REMOVE_CATEGORY_SUCCESS(8004,"删除版块分类成功"),
	MODIFY_SECTION_SUCCESS(8005,"版块修改成功"),
	CATEGORY_NOT_FOUND(8006,"版块分类丢失"),
	LIMIT_SECTION(8007,"用户在封禁状态"),
	NUM_MGR_OVER(8008,"版块版主数超出"),
	MODERATOR_SET_SUCCESS(8009,"设置版主成功"),
	MODERATOR_SET_FAIL(8010,"设置版主失败"),
	SYSADMIN_SET_SUCCESS(8011,"平台管理员设置成功"),
	SYSADMIN_SET_FAIL(8012,"平台管理员设置失败"),
	ADD_SECTION_SUCCESS(8013,"添加版块成功"),
	REMOVE_SECTION_SUCCESS(8014,"删除版块成功"),
	MODERATOR_UNSET_SUCCESS(8015,"设置版主成功"),
	CATEGORY_REMOVE_FAIL(8016,"板块分类下存在版块，不可删除"),
	TOP_SECTION_SUCCESS(8017,"推荐版块至首页成功"),
	UNTOP_SECTION_SUCCESS(8018,"取消首页版块成功"),
	MODIFY_SECTION_FAIL(8019,"版块修改失败"),
	MODIFY_SECTION_SAME(8020,"版块修改内容一样"),
	SECTION_VIP_FAIL(8021,"非普通用户"),
	DELECT_ALL_VIP(8022,"删除该用户在该版区下所有vip"),
	INSERT_VIP_SUCCESS(8023,"设置vip成功"),
	/**删帖操作**/
	RECOVER_NOTERC_SUCCESS(11001,"恢复帖子成功"),
	REMOVE_NOTERC_FOREVER_SUCCESS(11002,"永久删除成功"),
	
	/**解封id**/
	ENABLE_USERID_SUCCESS(12001,"解封用户id成功"),
	ENABLE_USERSECTION_SUCCESS(12002,"解封用户版块成功"),
	STOP_USERID_SUCCESS(12003,"封禁用户id成功"),
	STOP_USERSECTION_SUCCESS(12004,"封禁用户版块成功"),
	STOP_USERID_FAIL(12005,"封禁用户id失败"),
	STOP_USERSECTION_FAIL(12006,"封禁用户版块失败"),
	ENABLE_USERID_FAIL(12007,"解封用户id失败"),
	ENABLE_USERSECTION_FAIL(12007,"解封用户版块失败"),

	/**banner**/
	BANNER_PUBLISH_SUCCESS(13001,"banner发布成功"),
	BANNER_PUBLISH_FAIL(13002,"banner发布失败"),
	BANNER_CANCE_PUBLISH_SUCCESS(13003,"取消banner发布成功"),
	BANNER_CANCE_PUBLISH_FAIL(13004,"取消banner发布失败"),
	BANNER_REMOVE_SUCCESS(13005,"banner删除成功"),
	BANNER_REMOVE_FAIL(13006,"banner删除失败"),
	BANNER_SAVE_SUCCESS(13007,"保存成功"),
	BANNER_SAVE_FAIL(13008,"保存失败"),
	BANNER_PUBLISH_NUMOVER(13009,"发布banner的数量超出限制"),
	
	/**防灌水设置**/
	LIMIT_NOT_NUMBER(9001,"时间段帖子数不是数字"),
	ADD_SILENCE_ERROR(9002,"添加防灌水设置失败"),
	NOT_CONTENT(9003,"暂无内容"),
	MODIFI_SILENCE_ERROR(9004,"修改失败"),
	IDENTIFY_ON(9005,"开启验证码"),
	FORBIDDEN_ON(9006,"开启禁言"),
	
	/**用户信息修改操作**/
	MODIFLY_NICK_FAIL(10001,"亲，不可以再修改了哦"),
	/**已签到*/
	SIGNED_TODAY(11001,"您今天已经签到过了哦！"),
	
	/**权限相关**/
	NO_OPERATION_PERMISSION(12000,"无操作权限！"),
	/****/
	VEST_SET_SUCCESS(13000,"马甲添加成功"),
	VEST_SET_FAIL(13001,"马甲添加失败"),
	VEST_UNSET_SUCCESS(13002,"取消马甲成功"),
	VEST_UNSET_FAIL(13003,"取消马甲失败"),	
	VEST_SET_NOTALLOW(13004,"非普通用户"),
	CATEGORY_SET_SUCCESS(13005,"设置版区管理员成功"),
	CATEGORY_SET_FAIL(13006,"设置版区管理员失败"),
	CATEGORY_UNSET_SUCCESS(13007,"取消版区管理员成功"),
	CATEGORY_UNSET_FAIL(13008,"取消版区管理员失败"),
	SECTION_CLASS_LIMIT(14000,"版块目录数超出限制");
	/**值 **/
    private int code;
    /**描述**/
    private String desc;
    
    EnumCode(int code,String desc) {
    	this.code = code;
        this.desc = desc;
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

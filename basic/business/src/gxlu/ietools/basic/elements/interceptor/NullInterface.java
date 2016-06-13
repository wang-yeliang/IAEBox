package gxlu.ietools.basic.elements.interceptor;

import java.util.List;

public interface NullInterface{
	/**
	 * 元素为空性验证
	 * @param params --应用参数	  
	 * 		param0 --List*--  B类集合
	 * 		param1 --String*-- 目标对象
	 * 		param2 --String*-- 列表头
	 * @return
	 * 		ArrayList*-- 过滤后的B类集合
	 */
	public abstract Object elementNullVerification(List iParam);
}

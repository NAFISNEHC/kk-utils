package com.kk.common.utils;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapUtil {
	/**
	 *将Map转换为实体对象
	 *@date 2019.6.6
	 *@author hao.xu
	 */
	public static <T> T mapToBean(Map<String, Object> map, Class<T> obj) throws Exception {
		if (map == null) {
			return null;
		}
		//获取Entry键值对
		Set<Map.Entry<String, Object>> sets = map.entrySet();
		T object = obj.newInstance();
		//获取定义方法
		Method[] methods = obj.getDeclaredMethods();
		for (Map.Entry<String, Object> entry : sets) {
			String str = entry.getKey();
			String setMethod = "set" + str.substring(0, 1).toUpperCase() + str.substring(1);
			for (Method method : methods) {
				if (method.getName().equals(setMethod)) {
					method.invoke(object, entry.getValue());
				}
			}
		}
		return object;
	}

	/**
	 * 将实体对象转化为Map
	 * @date 2019.6.6
	 * @author hao.xu
	 */
	public static Map<String,Object> objectToMap(Object obj) {
		Map<String,Object> map = new HashMap<String,Object>();
		Class<?> c = null;
		try {
			c = Class.forName(obj.getClass().getName());
			Method[] m = c.getMethods();
			for (int i = 0; i < m.length; i++) {
				String method = m[i].getName();
				if (method.startsWith("get") && !method.equals("getClass")) {
					try {
						Object value = m[i].invoke(obj);
						if (value != null) {
							String key = method.substring(3);
							key = key.substring(0, 1).toLowerCase() + key.substring(1);
							map.put(key, value);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}

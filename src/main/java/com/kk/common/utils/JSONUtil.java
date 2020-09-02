package com.kk.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

/**
 * json操作工具类
 * 
 * @author tony
 */
public final class JSONUtil {

	/**
	 * 默认json格式化方式
	 */
	public static final SerializerFeature[] DEFAULT_FORMAT = { SerializerFeature.WriteDateUseDateFormat,
			SerializerFeature.WriteEnumUsingToString, SerializerFeature.WriteNonStringKeyAsString,
			SerializerFeature.QuoteFieldNames, SerializerFeature.SkipTransientField, SerializerFeature.SortField,
			SerializerFeature.PrettyFormat };

	private JSONUtil() {
	}

	/**
	 * 从json获取指定key的字符串
	 * 
	 * @param json
	 *        json字符串
	 * @param key
	 *        字符串的key
	 * @return 指定key的值
	 */
	public static Object getStringFromJSONObject(final String json, final String key) {
		requireNonNull(json, "json is null");
		return JSON.parseObject(json).getString(key);
	}

	/**
	 * 将字符串转换成JSON字符串
	 * 
	 * @param jsonString
	 *        json字符串
	 * @return 转换成的json对象
	 */
	public static JSONObject getJSONFromString(final String jsonString) {
		if (isBlank(jsonString)) {
			return new JSONObject();
		}
		return JSON.parseObject(jsonString);
	}

	/**
	 * 将json字符串，转换成指定java bean
	 * 
	 * @param jsonStr
	 *        json串对象
	 * @param beanClass
	 *        指定的bean
	 * @param <T>
	 *        任意bean的类型
	 * @return 转换后的java bean对象
	 */
	public static <T> T toBean(String jsonStr, Class<T> beanClass) {
		requireNonNull(jsonStr, "jsonStr is null");
		JSONObject jo = JSON.parseObject(jsonStr);
		jo.put(JSON.DEFAULT_TYPE_KEY, beanClass.getName());
		return JSON.parseObject(jo.toJSONString(), beanClass);
	}

	/**
	 * @param obj
	 *        需要转换的java bean
	 * @param <T>
	 *        入参对象类型泛型
	 * @return 对应的json字符串
	 */
	public static <T> String toJson(T obj) {
		requireNonNull(obj, "obj is null");
		return JSON.toJSONString(obj, DEFAULT_FORMAT);
	}

	/**
	 * 通过Map生成一个json字符串
	 * 
	 * @param map
	 *        需要转换的map
	 * @return json串
	 */
	public static String toJson(Map<String, Object> map) {
		requireNonNull(map, "map is null");
		return JSON.toJSONString(map, DEFAULT_FORMAT);
	}

	/**
	 * 美化传入的json,使得该json字符串容易查看
	 * 
	 * @param jsonString
	 *        需要处理的json串
	 * @return 美化后的json串
	 */
	public static String prettyFormatJson(String jsonString) {
		requireNonNull(jsonString, "jsonString is null");
		return JSON.toJSONString(getJSONFromString(jsonString), true);
	}

	/**
	 * 将传入的json字符串转换成Map
	 * 
	 * @param jsonString
	 *        需要处理的json串
	 * @return 对应的map
	 */
	public static Map<String, Object> toMap(String jsonString) {
		requireNonNull(jsonString, "jsonString is null");
		return getJSONFromString(jsonString);
	}

	/**
	 * 将传入的json字符串转换成Map
	 * 
	 * @param jsonString
	 *        需要处理的json串
	 * @return 对应的map
	 */
	public static List toList(String jsonString) {
		if (null == jsonString) {
			return null;
		}
		JSONArray json = JSONArray.parseArray(jsonString);
		return json;
	}

	/**
	 * 判断对象是否为空，如果为空，直接抛出异常
	 * 
	 * @param object
	 *        需要检查的对象
	 * @param errorMessage
	 *        异常信息
	 */
	public static void requireNonNull(Object object, String errorMessage) {
		if (null == object) {
			throw new NullPointerException(errorMessage);
		}
	}

	/**
	 * 判断一个字符串是否为空，null也会返回true
	 * 
	 * @param str
	 *        需要判断的字符串
	 * @return 是否为空，null也会返回true
	 */
	public static boolean isBlank(String str) {
		return null == str || "".equals(str.trim());
	}

	/**
	 * @param object
	 *        任意对象
	 * @return java.lang.String
	 */
	public static String objectToJson(Object object) {
		StringBuilder json = new StringBuilder();
		if (object == null) {
			json.append("\"\"");
		} else if (object instanceof String || object instanceof Integer || object instanceof Boolean) {
			json.append("\"").append(object.toString()).append("\"");
		} else if (object instanceof Map) {
			json.append(toJson(object));
		} else {
			json.append(beanToJson(object));
		}
		return json.toString();
	}

	/**
	 * 功能描述:传入任意一个 javabean 对象生成一个指定规格的字符串
	 * 
	 * @param bean
	 *        bean对象
	 * @return String
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException ignored) {
		}
		if (props != null) {
			for (PropertyDescriptor prop : props) {
				try {
					String name = objectToJson(prop.getName());
					String value = objectToJson(prop.getReadMethod().invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception ignored) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * 功能描述:通过传入一个列表对象,调用指定方法将列表中的数据生成一个JSON规格指定字符串
	 * 
	 * @param list
	 *        列表对象
	 * @return java.lang.String
	 */
	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * 将object对象转成json对象
	 * @param object
	 * @return
	 */
	public static JSONObject objToJSONObj(Object object){
		//先转字符串
		String jsonStr = JSON.toJSONString(object);
		//再转json
		return JSONObject.parseObject(jsonStr);
	}
}

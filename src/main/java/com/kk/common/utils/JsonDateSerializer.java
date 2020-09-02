package com.kk.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.util.Date;

public class JsonDateSerializer  extends JsonSerializer<Date>{
	@Override  
	public void serialize(Date date, JsonGenerator gen, SerializerProvider provider)  {
		try {
			String value = String.valueOf(date.getTime()); 
			gen.writeString(value);  
		} catch (Exception e) {
			e.printStackTrace();
		}

	}  
}

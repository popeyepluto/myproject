package com.homework.converter.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.base.Enums;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.homework.converter.po.Condition;
import com.homework.converter.service.JsonToSoqlService;
import com.homework.converter.util.CustomException;
import com.homework.converter.po.OperatorType;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
@Service
public class JsonToSoqlServiceImpl implements JsonToSoqlService {
	
	private static final String jsonString ="{\"conditions\":[{\"conditions\":[{\"conditions\":[{\"conditions\":[{\"field\":\"Country_vod__c\",\"operator\":\"like\",\"values\":[\"'%a%'\"]},{\"field\":\"Email_vod__c\",\"operator\":\"notLike\",\"values\":[\"'%b%'\"]}],\"conjunction\":\"and\"},{\"conditions\":[{\"field\":\"City_vod__c\",\"operator\":\"equals\",\"values\":[\"'c'\"]},{\"field\":\"Name\",\"operator\":\"notEquals\",\"values\":[\"'d'\"]}],\"conjunction\":\"and\"}],\"conjunction\":\"or\"},{\"field\":\"Address_Line_1_vod__c\",\"operator\":\"lessThan\",\"values\":[\"'e'\"]}],\"conjunction\":\"and\"},{\"conditions\":[{\"field\":\"Address_Line_1_vod__c\",\"operator\":\"greaterThan\",\"values\":[\"'f'\"]},{\"field\":\"Address_Line_2_vod__c\",\"operator\":\"lessThanOrEqualTo\",\"values\":[\"'g'\"]}],\"conjunction\":\"and\"},{\"conditions\":[{\"field\":\"Call2_vod__r.Name\",\"operator\":\"greaterThanOrEqualTo\",\"values\":[\"'h'\"]},{\"field\":\"City_vod__c\",\"operator\":\"like\",\"values\":[\"'i%'\"]}],\"conjunction\":\"and\"}],\"conjunction\":\"or\"}";
	//private static final String jsonString ="{\"conditions\":[{\"field\":\"Country_vod__c\",\"operator\":\"notLike\",\"values\":[\"'%a%'\"]},{\"field\":\"Account_vod__r.Name\",\"operator\":\"like\",\"values\":[\"'%b%'\"]},{\"field\":\"QA_Field_04__c\",\"operator\":\"greaterThanOrEqualTo\",\"values\":[\"2020-09-18T16:00:00.000Z\"]},{\"field\":\"QA_Field_02__c\",\"operator\":\"equals\",\"values\":[\"CNY1\"]}],\"conjunction\":\"and\"}";
	
	private static final String NOTLIKE_OPERATOR_PREFIX = "NOT";
	private static final Map<String,String> CONJUNCTION = new HashMap<String,String>();
	static {
		CONJUNCTION.put("or", "OR");
		CONJUNCTION.put("and", "AND");
    }

	
	@SuppressWarnings("unchecked")
	public String convert(String jsonStr) {
		// TODO Auto-generated method stub
		try {
			Gson gson = new Gson();
			if(jsonStr.isEmpty()) {
				jsonStr = jsonString;
			}
			Map<String,Object> map = gson.fromJson(jsonStr, new TypeToken<Map<String,Object>>(){}.getType());
			String soql = soqlBuild(getValue(map,"conditions",List.class),CONJUNCTION.get(getValue(map,"conjunction",String.class)),new StringBuilder() );
			return soql;
		}catch(JsonSyntaxException e) {
			return "Json is invalid!" +"---------"+ e.getMessage();
		}catch(CustomException e1) {
			return e1.getMessage();
		}
	}
	
	
	private <T> T getValue(Map<String,Object> map, String path, Class<T> clazz) {
		
		try {
			OgnlContext context = new OgnlContext();
			context.setRoot(map);
			@SuppressWarnings("unchecked")
			T value = (T)Ognl.getValue(path, context,context.getRoot());
			return value;
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private String soqlBuild(List<Map<String,Object>> mapList, String conjunction,StringBuilder soql) throws CustomException {
		
			if(mapList.size()==0) {
				return "";
			}
			
			soql.append("(");
			for(Map map:mapList) {
				
				if(map.containsKey("conditions")) {
					soqlBuild(getValue(map,"conditions",List.class),CONJUNCTION.get(getValue(map,"conjunction",String.class)),soql );
				}
				if(map.containsKey("field")) {
					Condition c = mapToCondition(map);
					if (!OperatorType.notLike.equals(c.getOperator())) {
						soql.append(" "+ c.getField()+" "+c.getOperator().getOperator()+" "+c.getValues().get(0));
					}else {
						soql.append(" ("+NOTLIKE_OPERATOR_PREFIX+" "+ c.getField()+" "+c.getOperator().getOperator()+" "+c.getValues().get(0)+")");
					}
					
				}
				
				soql.append(" "+conjunction+" ");
			}
			soql.delete(soql.lastIndexOf(conjunction),soql.length());
			soql.append(")");
	
			return soql.toString().substring(1,soql.length()-1);
		

	}
	
	
	private Condition mapToCondition(Map<String, Object> map) throws CustomException{
		
		Condition c = new Condition();
		c.setField((String)map.get("field"));
		if(Enums.getIfPresent(OperatorType.class, (String)map.get("operator")).isPresent()) {
			c.setOperator(Enums.getIfPresent(OperatorType.class, (String)map.get("operator")).get());
		}else {
			throw new CustomException("Invalid operator!"+"---"+(String)map.get("operator"));
		}
		
		List<?> values = (List<?>)map.get("values");
		if(values==null || values.size()!=1) {
			throw new CustomException("Invalid values---"+values.toString()+", the count of element is wrong! ");
		}
		c.setValues(values);
		return c;
	}
	
	

}

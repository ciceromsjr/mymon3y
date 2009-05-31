package com.google.code.mymon3y.jsf.converters;

import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class MoedaConverter implements Converter {

	public static final Pattern pMoeda = Pattern.compile("\\d+,\\d+");

	public static boolean matches(String valor) {

		return pMoeda.matcher(valor).matches();
	}
	
	public static final Object FLAG = new Object();
	
	public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String value) {

		if(value == null || value.trim().equals("") || !matches(value)){
			return FLAG;
		}
		
		String[] strings = value.split(",");
		return Integer.parseInt(strings[0]) * 100 + Integer.parseInt(strings[1]);
	}

	public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {

		Integer valor = (Integer) object;
		return String.valueOf(valor / 100) + "," + (valor % 100 < 10 ? "0" + String.valueOf(valor % 100) : String.valueOf(valor % 100));
	}

}

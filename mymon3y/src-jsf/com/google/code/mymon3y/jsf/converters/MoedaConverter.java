package com.google.code.mymon3y.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.google.code.mymon3y.jsf.validators.MoedaValidator;

public class MoedaConverter implements Converter {

	public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String value) {

		if(value == null || value.trim().equals("") || !MoedaValidator.matches(value)){
			return "0";
		}
		
		String[] strings = value.split(",");
		return Integer.parseInt(strings[0]) * 100 + Integer.parseInt(strings[1]);
	}

	public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {

		Integer valor = (Integer) object;
		return String.valueOf(valor / 100) + "," + (valor % 100 < 10 ? "0" + String.valueOf(valor % 100) : String.valueOf(valor % 100));
	}

}

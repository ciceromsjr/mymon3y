package com.google.code.mymon3y.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class CreditoDebitoConverter implements Converter {

	public static String CREDITO = "Crédito";
	
	public static String DEBITO = "Débito";
	
	public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String value) {

		Boolean credito = (value == null || value.equals(CREDITO)) ? true : false;

		return credito;
	}

	public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {

		Boolean credito = (Boolean) object;
		return credito == null || credito.booleanValue() ? CREDITO : DEBITO;
	}

}

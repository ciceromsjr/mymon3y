package com.google.code.mymon3y.jsf.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.google.code.mymon3y.util.Util;

public class DataConverter implements Converter {

	private static DateFormat df = null;

	{
		df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
	}

	public Object getAsObject(FacesContext facesContext, UIComponent uIComponent, String value) {

		Date data = null;

		try {
			data = df.parse(value);
			data = Util.getDataNormalizada(data);
		} catch (ParseException e) {
			e.printStackTrace();

			FacesMessage message = new FacesMessage();
			message.setDetail("Data inválida.");
			message.setSummary("Data inválida.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(message);

		}

		return data;
	}

	public String getAsString(FacesContext facesContext, UIComponent uIComponent, Object object) {

		Date data = (Date) object;
		return df.format(data);
	}

}

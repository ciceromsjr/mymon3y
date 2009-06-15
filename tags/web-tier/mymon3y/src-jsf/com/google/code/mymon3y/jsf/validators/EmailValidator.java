/*
 * Copyright (C) 2009
 * 
 * Jaindson Valentim Santana (jaindsonvs [at] gmail [dot] com)
 * Matheus Gaudencio do Rêgo (matheusgr [at] gmail [dot] com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 */
package com.google.code.mymon3y.jsf.validators;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@Deprecated
/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class EmailValidator implements Validator {

	public static final Pattern pEmail = Pattern.compile(".+@.+\\.[a-z]+");

	public static boolean matches(String email) {

		return pEmail.matcher(email).matches();
	}

	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
		
//		ClassValidator<T> cv = new ClassValidator<T>((Class<T>) obj.getClass());
		//TODO Reusar código do Hibernate
		
		String email = (String) object;
		if (!matches(email)) {
			FacesMessage message = new FacesMessage();
			message.setDetail("Endereço de email inválido.");
			message.setSummary("Endereço de email inválido.");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}

	}
}
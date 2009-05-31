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

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class MoedaValidator implements Validator {

	public static final Pattern pMoeda = Pattern.compile("\\d+,\\d+");

	public static boolean matches(String valor) {

		return pMoeda.matcher(valor).matches();
	}
	
	public void validate(FacesContext facesContext, UIComponent uIComponent, Object object) throws ValidatorException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>.  CHAMANDO VALIDATOR DE MOEDA");
		String moeda = (String) object;
		if (!matches(moeda)) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Valor inválido.");
			message.setDetail("Valor inválido. Exemplo: 125,0 ou 125,50");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}

	}
}
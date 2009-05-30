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
package com.google.code.mymon3y.persistencia;

import org.hibernate.validator.InvalidValue;

/**
 * Encapsula os erros de validação das entidades.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class InvalidPropertiesException extends PersistenciaMyMon3yException {

	/**
	 * Versão da classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Valores inválidos dos atributos das entidades.
	 */
	private InvalidValue[] invalidValues;

	/**
	 * Construtor com mensagem de erro.
	 * 
	 * @param mensagem
	 *            mensagem de erro.
	 */
	public InvalidPropertiesException(String mensagem) {
		super(mensagem);
	}

	/**
	 * Construtor com os valores inválidos.
	 * 
	 * @param invalidValues
	 *            Valores inválidos.
	 */
	public InvalidPropertiesException(InvalidValue[] invalidValues) {
		super(invalidValues[0].getMessage());
		this.invalidValues = invalidValues;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {

		if (invalidValues == null || invalidValues.length == 0) {
			return this.getMessage();
		}

		return this.invalidValues[0].getMessage();
	}

}

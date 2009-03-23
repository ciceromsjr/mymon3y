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
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class InvalidPropertiesException extends PersistenciaMyMon3yException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private InvalidValue[] invalidValues;

	/**
	 * @param mensagem
	 */
	public InvalidPropertiesException(String mensagem) {
		super(mensagem);
	}
	
	public InvalidPropertiesException(InvalidValue[] invalidValues) {
		super(invalidValues[0].getMessage());
		this.invalidValues = invalidValues;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		
		if(invalidValues == null || invalidValues.length == 0){
			return this.getMessage();
		}
		
		return this.invalidValues[0].getMessage();
	}
	
}

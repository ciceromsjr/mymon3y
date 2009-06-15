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

import com.google.code.mymon3y.MyMon3yException;

/**
 * Representa uma exceção ocorrida na camada de persistência.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class PersistenciaMyMon3yException extends MyMon3yException {

	/**
	 * Versão da classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see MyMon3yException#MyMon3yException(String)
	 */
	public PersistenciaMyMon3yException(String mensagem) {
		super(mensagem);
	}

	/**
	 * @see MyMon3yException#MyMon3yException(Throwable)
	 */
	public PersistenciaMyMon3yException(Throwable t) {
		super(t);
	}

	/**
	 * @see MyMon3yException#MyMon3yException()
	 */
	public PersistenciaMyMon3yException() {
		super();
	}
}

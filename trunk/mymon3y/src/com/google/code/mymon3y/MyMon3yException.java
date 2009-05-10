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
package com.google.code.mymon3y;

/**
 * Exceção geral da aplicação MyMon3y.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class MyMon3yException extends Exception {

	/**
	 * Versão da classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construtor que recebe a mensagem de erro.
	 * 
	 * @param mensagem
	 *            Mensagem de erro.
	 */
	public MyMon3yException(String mensagem) {

		super(mensagem);
	}

	/**
	 * Construtor que recebe um {@link Throwable} associado com o erro.
	 * 
	 * @param t
	 *            {@link Throwable} associado com o erro.
	 */
	public MyMon3yException(Throwable t) {

		super(t);
	}

	/**
	 * Construtor que recebe a mensagem de erro e o {@link Throwable} associado com o ele.
	 * 
	 * @param mensagem
	 *            Mensagem de erro.
	 * @param t
	 *            {@link Throwable} associado com o erro.
	 */
	public MyMon3yException(String mensagem, Throwable t) {

		super(mensagem, t);
	}

	/**
	 * Construtor vazio.
	 */
	public MyMon3yException() {
		super();
	}

}

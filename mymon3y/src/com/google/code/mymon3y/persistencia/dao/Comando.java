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
package com.google.code.mymon3y.persistencia.dao;

/**
 * Representa um comando qualquer a ser executado. A lógica a ser executada depende do contexto.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public interface Comando {

	/**
	 * Executa uma operação.
	 * 
	 * @return Objeto de retorno da operação.
	 */
	public Object executar();

}

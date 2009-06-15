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

import java.util.List;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;

/**
 * Interface do DAO de {@link Categoria}.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public interface CategoriaDAO extends GenericDAO<Categoria, Long> {

	/**
	 * Encontra uma {@link Categoria} com base no seu nome no login do Usuário dono.
	 * 
	 * @param nome
	 *            Nome da Categoria.
	 * @param login
	 *            Login do Úsuário dono.
	 * @return Categoria buscada ou null caso não seja encontrada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	Categoria findByNomeELoginDoUsuario(String nome, String login) throws PersistenciaMyMon3yException;

	/**
	 * Encontra Categorias com base no seu nome e no login do Usuário dono.
	 * 
	 * @param nome
	 *            Nome da Categoria.
	 * @param login
	 *            Login do Úsuário dono.
	 * @return Categorias buscada ou null caso não seja encontrada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	List<Categoria> findsByNomeELoginDoUsuario(String nome, String login) throws PersistenciaMyMon3yException;

}

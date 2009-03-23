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

import java.io.Serializable;
import java.util.List;

import com.google.code.mymon3y.model.Identificavel;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public interface GenericDAO<T extends Identificavel, ID extends Serializable> {

	/**
	 * Busca uma entidade baseando-se no seu ID.
	 * 
	 * @param id
	 *            Identificador único da entidade a ser buscada
	 * @return Entidade cujo ID é o mesmo que o passado como parâmetro
	 */
	T findById(ID id) throws PersistenciaMyMon3yException;

	/**
	 * Retorna Todas as entidades do tipo{@link T} do banco.
	 * 
	 * @return Todas as entidades do tipo{@link T} do banco.
	 */
	List<T> findAll() throws PersistenciaMyMon3yException;

	/**
	 * Persiste uma entidade. Caso esta já exista é feita uma atualização.
	 * 
	 * @param entidade
	 *            Entidade a ser persistida.
	 * @return Entidade a ser persistida com o atributo {@link ID} atualizado.
	 */
	T makePersistent(T entidade) throws PersistenciaMyMon3yException;

	/**
	 * Persiste uma lista de entidades. Caso uma das entidades já exista é feita uma atualização dela.
	 * 
	 * @param entidades
	 *            Entidades a serem persistidas.
	 * @return Entidades a serem persistidas com os atributos {@link ID} atualizados, em caso de atualização.
	 */
	List<T> makePersistent(List<T> entidades) throws PersistenciaMyMon3yException;

	/**
	 * Faz com que uma entidade fique no estado transiente. Para maiores informações ver documentação oficial
	 * {@link http://www.hibernate.org/hib_docs/v3/reference/en/html/objectstate.html}
	 * 
	 * @param entidade
	 *            Entidade a ser modificado o estado
	 */
	void makeTransient(T entidade) throws PersistenciaMyMon3yException;

	/**
	 * @param entidades
	 */
	void makeTransient(List<T> entidades) throws PersistenciaMyMon3yException;

	/**
	 * Mesma semântica que o {@link GenericDAO#makeTransient(Identificavel)} mas utiliza ao invés da entidade em si, um
	 * atributo identificador
	 * 
	 * @param id
	 *            Atributo identificador
	 */
	void makeTransient(ID id) throws PersistenciaMyMon3yException;

	/**
	 * Elimina do banco todos os objetos do tipo {@link T}.
	 * 
	 */
	void makeTransient() throws PersistenciaMyMon3yException;

}

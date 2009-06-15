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
package com.google.code.mymon3y.persistencia.dao.hibernate;

import java.util.List;

import org.hibernate.Query;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;
import com.google.code.mymon3y.persistencia.dao.CategoriaDAO;
import com.google.code.mymon3y.persistencia.dao.Comando;

/**
 * Implementação do DAO Hibernate da entidade {@link Categoria}.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class CategoriaDAOHibernate extends AbstractGenericHibernateDAO<Categoria, Long> implements CategoriaDAO {

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.CategoriaDAO#findByNomeELoginDoUsuario(java.lang.String,
	 *      java.lang.String)
	 */
	public Categoria findByNomeELoginDoUsuario(final String nome, final String login)
			throws PersistenciaMyMon3yException {
		Categoria result = null;

		result = (Categoria) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("categoria.nomeCategoriaLoginDoUsuario");
				q.setString("nome", nome);
				q.setString("loginDoUsuario", login);

				return q.list().size() > 0 ? q.list().get(0) : null;
			}

		});
		fazerAntesDoLoadSessaoFechada(result);
		return result;
	}

	@SuppressWarnings("unchecked")
	/* (non-Javadoc)
	 * @see com.google.code.mymon3y.persistencia.dao.CategoriaDAO#findsByNomeELoginDoUsuario(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Categoria> findsByNomeELoginDoUsuario(final String nome, final String login) throws PersistenciaMyMon3yException {
		List<Categoria> result = null;

		result = (List<Categoria>) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("categorias.nomeCategoriaLoginDoUsuario");
				q.setString("nome", "%" + nome + "%");
				q.setString("loginDoUsuario", login);

				return q.list();
			}

		});

		return result;
	}

}

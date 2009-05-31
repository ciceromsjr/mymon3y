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

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;
import com.google.code.mymon3y.persistencia.dao.Comando;
import com.google.code.mymon3y.persistencia.dao.TransacaoDAO;

/**
 * Implementação do DAO Hibernate da entidade {@link Transacao}.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class TransacaoDAOHibernate extends AbstractGenericHibernateDAO<Transacao, Long> implements TransacaoDAO {

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.TransacaoDAO#getTransacoes(java.lang.String, java.util.Date,
	 *      java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<Transacao> getTransacoes(final String login, final Date inicio, final Date fim)
			throws PersistenciaMyMon3yException {
		List<Transacao> result = null;

		result = (List<Transacao>) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("transacao.transacoes");
				q.setString("loginDoUsuario", login);
				q.setDate("dataInicio", inicio);
				q.setDate("dataFim", fim);
				return q.list();
			}

		});
		return result;
	}

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.TransacaoDAO#getNumeroDeTransacoes(java.lang.String)
	 */
	public Long getNumeroDeTransacoes(final String login) throws PersistenciaMyMon3yException {
		Long result = null;

		result = (Long) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("transacao.loginDoUsuario");
				q.setString("loginDoUsuario", login);

				return q.uniqueResult();
			}

		});
		return result;
	}

	@SuppressWarnings("unchecked")
	/* (non-Javadoc)
	 * @see com.google.code.mymon3y.persistencia.dao.TransacaoDAO#getTransacoes(java.lang.String)
	 */
	@Override
	public List<Transacao> getTransacoes(final String login) throws PersistenciaMyMon3yException {
		List<Transacao> result = null;

					
		result = (List<Transacao>) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("transacao.todasTransacoesLoginDoUsuario");
				q.setString("loginDoUsuario", login);

				return q.list();
			}

		});
		return result;
	}
	
	@SuppressWarnings("unchecked")
	/* (non-Javadoc)
	 * @see com.google.code.mymon3y.persistencia.dao.TransacaoDAO#getTransacoes(java.lang.String, java.lang.Long)
	 */
	@Override
	public List<Transacao> getTransacoes(final String login, final Long idCategoria) throws PersistenciaMyMon3yException {
		List<Transacao> result = null;

		
		result = (List<Transacao>) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("transacao.todasTransacoesLoginDoUsuarioIdCategoria");
				q.setString("loginDoUsuario", login);
				q.setLong("idCategoria", idCategoria);

				return q.list();
			}

		});
		return result;
	}
	
	/**
	 * @see com.google.code.mymon3y.persistencia.dao.TransacaoDAO#getNotificacoes(java.lang.Long, java.util.Date)
	 */
	@Override
	public Long getNotificacoes(final Long idDoUsuario, final Date data) throws PersistenciaMyMon3yException {
		Long result = null;

		result = (Long) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("transacao.porUsuarioEData");
				q.setLong("idDoUsuario", idDoUsuario);
				q.setDate("data", data);

				return q.uniqueResult();
			}

		});
		return result;
	}

}

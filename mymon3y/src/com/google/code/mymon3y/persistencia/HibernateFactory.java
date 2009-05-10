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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * Fábrica de sessões do Hibernate.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class HibernateFactory {

	/**
	 * Log da classe {@link HibernateFactory}.
	 */
	private static Logger log = Logger.getLogger(HibernateFactory.class);

	/**
	 * Fábrica de Sessões.
	 */
	private static SessionFactory sessionFactory;

	/**
	 * Configuração do Hibernate.
	 */
	private static Configuration configuration;

	/**
	 * Constrói um novo singleton SessionFactory.
	 * 
	 * @return uma SessionFactory.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	public static SessionFactory buildSessionFactory() throws PersistenciaMyMon3yException {

		if (sessionFactory != null) {
			closeFactory();
		}
		return configureSessionFactory();
	}

	/**
	 * Cria uma SessionFactory caso necessário.
	 * 
	 * @return uma SessionFactory.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	public static SessionFactory buildSeNecessario() {

		if (sessionFactory != null) {
			return sessionFactory;
		}
		return configureSessionFactory();
	}

	/**
	 * Retorna a Fábrica de Sessões sendo utilizada no momento.
	 * 
	 * @return a Fábrica de Sessões.
	 */
	public static SessionFactory getSessionFactory() {

		return sessionFactory;
	}

	/**
	 * Retorna a Configuração sendo utilizada pelo Hibernate.
	 * 
	 * @return a Configuração sendo utilizada pelo Hibernate.
	 */
	public static Configuration getConfiguration() {

		return configuration;
	}

	/**
	 * Abre uma nova Sessão.
	 * 
	 * @return uma Sessão.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	public static Session openSession() throws PersistenciaMyMon3yException {

		buildSeNecessario();
		return sessionFactory.openSession();
	}

	/**
	 * Fecha a Fábrica de Sessões.
	 */
	public static void closeFactory() {

		if (sessionFactory != null) {
			try {
				sessionFactory.close();
			} catch (HibernateException ignored) {
				log.error("Não foi possível fechar a Fábrica de Sessões!", ignored);
			}
		}
	}

	/**
	 * Fecha uma determinada Sessão.
	 * 
	 * @param session
	 *            Sessão a ser fechada.
	 */
	public static void close(Session session) {

		if (session != null) {
			try {
				if (session != null && session.isOpen()) {
					session.close();
				}
			} catch (HibernateException ignored) {
				log.error("Não foi possível fechar a Sessão!", ignored);
			}
		}
	}

	/**
	 * Faz o <i>rollback</i> de uma transação.
	 * 
	 * @param tx
	 *            Transação a ser utilizada.
	 */
	public static void rollback(Transaction tx) {

		try {
			if (tx != null) {
				tx.rollback();
			}
		} catch (HibernateException ignored) {
			log.error("Não foi possível realizar o rollback da Transação!", ignored);
		}
	}

	/**
	 * Configura a Fábrica de Sessões.
	 * 
	 * @return a Fábrica de Sessões.
	 */
	private static SessionFactory configureSessionFactory() {

		configuration = new AnnotationConfiguration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
		return sessionFactory;
	}

}

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

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.validator.InvalidStateException;

import com.google.code.mymon3y.model.Identificavel;
import com.google.code.mymon3y.persistencia.HibernateFactory;
import com.google.code.mymon3y.persistencia.InvalidPropertiesException;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;
import com.google.code.mymon3y.persistencia.dao.Comando;
import com.google.code.mymon3y.persistencia.dao.GenericDAO;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public abstract class AbstractGenericHibernateDAO<T extends Identificavel, ID extends Serializable> implements
		GenericDAO<T, ID> {

	/**
	 * Sessão utilizada pela Conexão.
	 */
	protected Session session;

	/**
	 * Uma transação a ser efetuada em cada interação com o banco.
	 */
	protected Transaction tx;

	/**
	 * Classe do Objeto estendendo a classe abstrata.
	 */
	protected Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	/*
	 * Construtor simples da classe abstrata
	 */
	public AbstractGenericHibernateDAO() {

		HibernateFactory.buildSeNecessario();
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/**
	 * Retorna a seção atualmente em uso.
	 * 
	 * @return A seção atualmente em uso.
	 */
	protected Session getSession() {

		return session;
	}

	/**
	 * Retorna a transação atualmente em uso.
	 * 
	 * @return A transação atualmente em uso.
	 */
	protected Transaction getTransaction() {

		return tx;
	}

	/**
	 * Retorna a instancia da classe que estende a classe abstrata.
	 * 
	 * @return A instancia da classe que estende a classe abstrata.
	 */
	public Class<T> getPersistentClass() {

		return persistentClass;
	}

	protected void fazerAntesDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

		verificarInconsistenciasAntesDeApagarSessaoAberta(entity);
	}

	protected void fazerDepoisDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	protected void fazerAntesDoLoadSessaoFechada(T entity) throws PersistenciaMyMon3yException {

	}

	protected void fazerAntesDePersistirSessaoAberta(T entity) throws PersistenciaMyMon3yException {

		verificarInconsistenciasAntesDePersistirSessaoAberta(entity);

	}

	protected void verificarInconsistenciasAntesDePersistirSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	protected void verificarInconsistenciasAntesDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#makeTransient()
	 */
	public void makeTransient() throws PersistenciaMyMon3yException {

		try {
			startOperation();
			getSession().createQuery("delete " + getPersistentClass().getSimpleName()).executeUpdate();
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#makePersistent(java.lang.Object)
	 */
	public T makePersistent(T entity) throws PersistenciaMyMon3yException {

		try {
			startOperation();
			fazerAntesDePersistirSessaoAberta(entity);
			getSession().saveOrUpdate(entity);
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return entity;
	}


	public List<T> makePersistent(List<T> entidades) throws PersistenciaMyMon3yException {

		List<T> result = entidades;
		try {
			startOperation();
			for (T entity : entidades) {
				fazerAntesDePersistirSessaoAberta(entity);
				getSession().saveOrUpdate(entity);
			}
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return result;
	}

	public List<T> makePersistent(T... entidades) throws PersistenciaMyMon3yException {

		List<T> lista = new ArrayList<T>(entidades.length);
		CollectionUtils.addAll(lista, entidades);
		return makePersistent(lista);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#makeTransient(java.lang.Object)
	 */
	public void makeTransient(T entity) throws PersistenciaMyMon3yException {

		try {
			startOperation();
			fazerAntesDeApagarSessaoAberta(entity);
			getSession().delete(entity);
			fazerDepoisDeApagarSessaoAberta(entity);
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
	}

	public void makeTransient(List<T> entidades) throws PersistenciaMyMon3yException {

		try {
			startOperation();
			for (T entity : entidades) {
				fazerAntesDeApagarSessaoAberta(entity);
				getSession().delete(entity);
				fazerDepoisDeApagarSessaoAberta(entity);
			}
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#makeTransient(java.io.Serializable)
	 */
	public void makeTransient(ID id) throws PersistenciaMyMon3yException {

		T t = findById(id);
		makeTransient(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#findById(java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	public T findById(ID id) throws PersistenciaMyMon3yException {

		T entity = null;
		try {
			startOperation();
			entity = (T) getSession().get(getPersistentClass(), id);
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		fazerAntesDoLoadSessaoFechada(entity);
		return entity;
	}

	public Object executarOperacao(Comando comando) throws PersistenciaMyMon3yException {

		Object result = null;
		try {
			startOperation();

			result = comando.executar();

			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistencia.GenericDAO#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() throws PersistenciaMyMon3yException {

		List<T> result = null;
		try {
			startOperation();
			result = getSession().createQuery("from " + getPersistentClass().getSimpleName()).list();
			getSession().flush();
			getTransaction().commit();
		} catch (HibernateException e) {
			handleException(e);
		} catch (RuntimeException e) {
			handleException(e);
		} finally {
			HibernateFactory.close(session);
		}
		for (T t : result) {
			fazerAntesDoLoadSessaoFechada(t);
		}
		return result;

	}

	/**
	 * Faz o rollback da transação sendo utilizada no momento em que houve o lançamento da exceção e relança como uma
	 * Exceção do tipo {@link PersistenciaMyMon3yException} conhecida pela Aplicação.
	 * 
	 * @param e
	 *            Exceção a ser tratada
	 * @throws CamadaBDException
	 *             Exceção a ser lançada encapsulando a exceção de mais baixo nível que ocorreu.
	 */
	protected void handleException(HibernateException e) throws PersistenciaMyMon3yException {

		HibernateFactory.rollback(tx);
		throw getException(e);
	}

	private void handleException(RuntimeException e) throws InvalidPropertiesException {
		HibernateFactory.rollback(tx);
		throw getException(e);
	}
	
	
	protected PersistenciaMyMon3yException getException(HibernateException e) {
		
		return new PersistenciaMyMon3yException(e);
	}
	
	private InvalidPropertiesException getException(RuntimeException e) {
		if(e instanceof InvalidStateException){
			InvalidStateException ise = (InvalidStateException)e;
			return new InvalidPropertiesException(ise.getInvalidValues());
		}
		throw e;
	}

	/**
	 * Inicializa as entidades necessárias para se fazer uma operação no banco de dados.
	 * 
	 * @throws HibernateException
	 *             Exceção lançada caso haja algum erro de persistência.
	 */
	protected void startOperation() throws PersistenciaMyMon3yException {

		if (session == null || !session.isOpen()) {
			session = HibernateFactory.openSession();
		}
		tx = session.beginTransaction();
	}

}

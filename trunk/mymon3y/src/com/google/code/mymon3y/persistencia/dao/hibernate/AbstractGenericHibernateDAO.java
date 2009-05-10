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
 * DAO genérico do Hibernate.
 * 
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

	/**
	 * Construtor simples da classe abstrata
	 */
	@SuppressWarnings("unchecked")
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

	/**
	 * Operações a serem realizadas com a entidade antes dela ser apagada e com a sessão ainda aberta.
	 * 
	 * @param entity
	 *            Entidade a ser apagada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void fazerAntesDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

		verificarInconsistenciasAntesDeApagarSessaoAberta(entity);
	}

	/**
	 * Operações a serem realizadas com a entidade depois dela ser apagada e com a sessão ainda aberta.
	 * 
	 * @param entity
	 *            Entidade a ser apagada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void fazerDepoisDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	/**
	 * Operações a serem realizadas antes de carregar uma entidade e com a sessão já aberta.
	 * 
	 * @param entity
	 *            Entidade a ser carregada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void fazerAntesDoLoadSessaoFechada(T entity) throws PersistenciaMyMon3yException {

	}

	/**
	 * Operações a serem realizadas antes de persistir uma entidade e com a sessão ainda aberta.
	 * 
	 * @param entity
	 *            Entidade a ser persistida.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void fazerAntesDePersistirSessaoAberta(T entity) throws PersistenciaMyMon3yException {

		verificarInconsistenciasAntesDePersistirSessaoAberta(entity);

	}

	/**
	 * Verificar consistência da entidade antes de persistir e com a sessão ainda aberta.
	 * 
	 * @param entity
	 *            Entidade a ser persistida.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void verificarInconsistenciasAntesDePersistirSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	/**
	 * Verificar consistência da entidade antes de apagar e com a sessão ainda aberta.
	 * 
	 * @param entity
	 *            Entidade a ser apagada.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	protected void verificarInconsistenciasAntesDeApagarSessaoAberta(T entity) throws PersistenciaMyMon3yException {

	}

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makeTransient()
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

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makePersistent(com.google.code.mymon3y.model.Identificavel)
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

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makePersistent(java.util.List)
	 */
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

	/**
	 * @see #makePersistent(List)
	 */
	public List<T> makePersistent(T... entidades) throws PersistenciaMyMon3yException {

		List<T> lista = new ArrayList<T>(entidades.length);
		CollectionUtils.addAll(lista, entidades);
		return makePersistent(lista);
	}

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makeTransient(com.google.code.mymon3y.model.Identificavel)
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

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makeTransient(java.util.List)
	 */
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

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#makeTransient(java.io.Serializable)
	 */
	public void makeTransient(ID id) throws PersistenciaMyMon3yException {

		T t = findById(id);
		makeTransient(t);
	}

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#findById(java.io.Serializable)
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

	/**
	 * Executa uma operação envolvendo o banco de dados de forma segura (sessão aberta, controle de transação, etc.).
	 * 
	 * @param comando
	 *            Comando a ser executado.
	 * @return Objeto de retorno do Comando executado.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
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

	/**
	 * @see com.google.code.mymon3y.persistencia.dao.GenericDAO#findAll()
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
	 *            Exceção a ser tratada.
	 * @throws PersistenciaMyMon3yException
	 *             Exceção a ser lançada encapsulando a exceção de mais baixo nível que ocorreu.
	 */
	protected void handleException(HibernateException e) throws PersistenciaMyMon3yException {

		HibernateFactory.rollback(tx);
		throw getException(e);
	}

	/**
	 * Faz o rollback da transação sendo utilizada no momento em que houve o lançamento da exceção e relança como uma
	 * Exceção do tipo {@link PersistenciaMyMon3yException} conhecida pela Aplicação.
	 * 
	 * @param e
	 *            Exceção a ser tratada.
	 * @throws PersistenciaMyMon3yException
	 *             Exceção a ser lançada encapsulando a exceção de mais baixo nível que ocorreu.
	 */
	private void handleException(RuntimeException e) throws InvalidPropertiesException {
		HibernateFactory.rollback(tx);
		throw getException(e);
	}

	/**
	 * Retorna uma exceção do tipo {@link PersistenciaMyMon3yException}.
	 * 
	 * @param e
	 *            Exceção {@link HibernateException} que ocorreu.
	 * @return Uma exceção do tipo {@link PersistenciaMyMon3yException}.
	 */
	protected PersistenciaMyMon3yException getException(HibernateException e) {

		return new PersistenciaMyMon3yException(e);
	}

	/**
	 * Retorna uma exceção do tipo {@link InvalidPropertiesException} caso a exceção {@link RuntimeException} lançada
	 * seja do tipo {@link InvalidStateException}.
	 * 
	 * @param e
	 *            Exceção {@link RuntimeException} que ocorreu.
	 * @return Uma exceção do tipo {@link InvalidPropertiesException} caso a exceção {@link RuntimeException} lançada
	 *         seja do tipo {@link InvalidStateException}.
	 */
	private InvalidPropertiesException getException(RuntimeException e) {
		if (e instanceof InvalidStateException) {
			InvalidStateException ise = (InvalidStateException) e;
			return new InvalidPropertiesException(ise.getInvalidValues());
		}
		throw e;
	}

	/**
	 * Inicializa as entidades necessárias para se fazer uma operação no banco de dados.
	 * 
	 * @throws PersistenciaMyMon3yException
	 *             Exceção lançada caso haja algum erro de persistência.
	 */
	protected void startOperation() throws PersistenciaMyMon3yException {

		if (session == null || !session.isOpen()) {
			session = HibernateFactory.openSession();
		}
		tx = session.beginTransaction();
	}

}

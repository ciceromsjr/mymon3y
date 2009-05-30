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

import java.util.Date;
import java.util.List;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;
import com.google.code.mymon3y.persistencia.dao.CategoriaDAO;
import com.google.code.mymon3y.persistencia.dao.GenericDAO;
import com.google.code.mymon3y.persistencia.dao.TransacaoDAO;
import com.google.code.mymon3y.persistencia.dao.UsuarioDAO;
import com.google.code.mymon3y.persistencia.dao.hibernate.CategoriaDAOHibernate;
import com.google.code.mymon3y.persistencia.dao.hibernate.TransacaoDAOHibernate;
import com.google.code.mymon3y.persistencia.dao.hibernate.UsuarioDAOHibernate;

/**
 * Responsável por lidar com a persistência das entidades ou qualquer operação que envolva o banco de dados.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class GerenciadorDePersistencia {

	/**
	 * DAO da entidade Usuário.
	 */
	private UsuarioDAO usuarioDAO;

	/**
	 * DAO da entidade Categoria.
	 */
	private CategoriaDAO categoriaDAO;

	/**
	 * DAO da entidade Transação.
	 */
	private TransacaoDAO transacaoDAO;

	/**
	 * Construtor vazio.
	 */
	public GerenciadorDePersistencia() {
		this.usuarioDAO = new UsuarioDAOHibernate();
		this.categoriaDAO = new CategoriaDAOHibernate();
		this.transacaoDAO = new TransacaoDAOHibernate();
	}

	/**
	 * Elimina todas as entidades do sistema.
	 * 
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	public void zerarSistema() throws PersistenciaMyMon3yException {
		this.transacaoDAO.makeTransient();
		this.categoriaDAO.makeTransient();
		this.usuarioDAO.makeTransient();
	}

	/**
	 * @see GenericDAO#makePersistent(com.google.code.mymon3y.model.Identificavel)
	 */
	public void makePersistent(Usuario usuario) throws PersistenciaMyMon3yException {
		this.usuarioDAO.makePersistent(usuario);
	}

	/**
	 * @see UsuarioDAO#findByLogin(String)
	 */
	public Usuario getUsuarioByLogin(String login) throws PersistenciaMyMon3yException {
		return this.usuarioDAO.findByLogin(login);
	}

	/**
	 * @see GenericDAO#makePersistent(com.google.code.mymon3y.model.Identificavel)
	 */
	public void atualizar(Usuario usuario) throws PersistenciaMyMon3yException {
		this.usuarioDAO.makePersistent(usuario);
	}

	/**
	 * @see CategoriaDAO#findByNomeELoginDoUsuario(String, String)
	 * 
	 */
	public Categoria getCategoriaByNomeELoginDoUsuario(String login, String nome) throws PersistenciaMyMon3yException {
		return this.categoriaDAO.findByNomeELoginDoUsuario(nome, login);
	}

	/**
	 * @see GenericDAO#makePersistent(com.google.code.mymon3y.model.Identificavel)
	 */
	public void atualizar(Categoria categoria) throws PersistenciaMyMon3yException {
		this.categoriaDAO.makePersistent(categoria);
	}

	/**
	 * @see GenericDAO#makePersistent(com.google.code.mymon3y.model.Identificavel)
	 */
	public void atualizar(Transacao transacao) throws PersistenciaMyMon3yException {
		this.transacaoDAO.makePersistent(transacao);
	}

	/**
	 * @see GenericDAO#findById(java.io.Serializable)
	 */
	public Categoria getCategoriaById(Long idCategoria) throws PersistenciaMyMon3yException {
		return this.categoriaDAO.findById(idCategoria);
	}

	/**
	 * @see GenericDAO#findById(java.io.Serializable)
	 */
	public Transacao getTransacaoById(Long idTransacao) throws PersistenciaMyMon3yException {
		return this.transacaoDAO.findById(idTransacao);
	}

	/**
	 * @see TransacaoDAO#getTransacoes(String, Date, Date)
	 */
	public List<Transacao> getTransacoesByLogin(String login, Date inicio, Date fim)
			throws PersistenciaMyMon3yException {
		return this.transacaoDAO.getTransacoes(login, inicio, fim);
	}

	/**
	 * @see GenericDAO#makeTransient(com.google.code.mymon3y.model.Identificavel)
	 */
	public void removerTransacao(Transacao transacao) throws PersistenciaMyMon3yException {
		this.transacaoDAO.makeTransient(transacao);
	}

	/**
	 * @see TransacaoDAO#getNumeroDeTransacoes(String)
	 */
	public Long getNumeroDeTransacoes(String login) throws PersistenciaMyMon3yException {
		return this.transacaoDAO.getNumeroDeTransacoes(login);
	}

	/**
	 * @see GenericDAO#makeTransient(com.google.code.mymon3y.model.Identificavel)
	 */
	public void removerCategoria(Categoria categoria) throws PersistenciaMyMon3yException {
		this.categoriaDAO.makeTransient(categoria);
	}

	/**
	 * @see GenericDAO#makeTransient(com.google.code.mymon3y.model.Identificavel)
	 */
	public void removerUsuario(Usuario usuario) throws PersistenciaMyMon3yException {
		this.usuarioDAO.makeTransient(usuario);
	}

	/**
	 * @see TransacaoDAO#getNotificacoes(Long, Date)
	 */
	public Long getNotificacoes(Long idDoUsuario, Date data) throws PersistenciaMyMon3yException {
		return this.transacaoDAO.getNotificacoes(idDoUsuario, data);
	}

}

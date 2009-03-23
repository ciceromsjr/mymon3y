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

import com.google.code.mymon3y.MyMon3yException;
import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;
import com.google.code.mymon3y.persistencia.dao.CategoriaDAO;
import com.google.code.mymon3y.persistencia.dao.TransacaoDAO;
import com.google.code.mymon3y.persistencia.dao.UsuarioDAO;
import com.google.code.mymon3y.persistencia.dao.hibernate.CategoriaDAOHibernate;
import com.google.code.mymon3y.persistencia.dao.hibernate.TransacaoDAOHibernate;
import com.google.code.mymon3y.persistencia.dao.hibernate.UsuarioDAOHibernate;


/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 *
 */
public class GerenciadorDePersistencia {

	private UsuarioDAO usuarioDAO;

	private CategoriaDAO categoriaDAO;

	private TransacaoDAO transacaoDAO;
	
	public GerenciadorDePersistencia(){
		this.usuarioDAO = new UsuarioDAOHibernate();
		this.categoriaDAO = new CategoriaDAOHibernate();
		this.transacaoDAO = new TransacaoDAOHibernate();
	}
	
	/**
	 * @throws MyMon3yException
	 */
	public void zerarSistema() throws MyMon3yException {
		this.transacaoDAO.makeTransient();
		this.categoriaDAO.makeTransient();
		this.usuarioDAO.makeTransient();
	}

	/**
	 * @param usuario
	 * @throws MyMon3yException 
	 */
	public void makePersistent(Usuario usuario) throws MyMon3yException {
		this.usuarioDAO.makePersistent(usuario);
	}

	/**
	 * @param login
	 * @return
	 * @throws PersistenciaMyMon3yException 
	 */
	public Usuario findUsuarioByLogin(String login) throws PersistenciaMyMon3yException {
		return this.usuarioDAO.findByLogin(login);
	}

	/**
	 * @param login
	 * @return
	 * @throws PersistenciaMyMon3yException 
	 */
	public Usuario getUsuarioByLogin(String login) throws PersistenciaMyMon3yException {
		return this.usuarioDAO.findByLogin(login);
	}

	/**
	 * @param usuario
	 * @throws MyMon3yException 
	 */
	public void atualizar(Usuario usuario) throws MyMon3yException {
		this.usuarioDAO.makePersistent(usuario);
	}

	/**
	 * @param login
	 * @param nome
	 * @return
	 * @throws MyMon3yException 
	 */
	public Categoria getCategoriaByNomeELoginDoUsuario(String login, String nome) throws PersistenciaMyMon3yException {
		return this.categoriaDAO.findByNomeELoginDoUsuario(nome, login);
	}

	/**
	 * @param categoria
	 * @throws MyMon3yException 
	 */
	public void atualizar(Categoria categoria) throws MyMon3yException {
		this.categoriaDAO.makePersistent(categoria);
	}

	/**
	 * @param transacao
	 * @throws MyMon3yException 
	 */
	public void atualizar(Transacao transacao) throws MyMon3yException {
		this.transacaoDAO.makePersistent(transacao);
	}
	
	/**
	 * @param idCategoria
	 * @return
	 * @throws MyMon3yException 
	 */
	public Categoria getCategoriaById(Long idCategoria) throws MyMon3yException {
		return this.categoriaDAO.findById(idCategoria);
	}

	/**
	 * @param idTransacao
	 * @return
	 * @throws MyMon3yException 
	 */
	public Transacao getTransacaoById(Long idTransacao) throws MyMon3yException {
		return this.transacaoDAO.findById(idTransacao);
	}

	/**
	 * @param transacao
	 * @throws MyMon3yException 
	 */
	public void removerTransacao(Transacao transacao) throws MyMon3yException {
		this.transacaoDAO.makeTransient(transacao);
	}

	/**
	 * @param login
	 * @return
	 * @throws MyMon3yException 
	 */
	public Long getNumeroDeTransacoes(String login) throws MyMon3yException {
		return this.transacaoDAO.getNumeroDeTransacoes(login);
	}

	/**
	 * @param idCategoria
	 * @throws MyMon3yException 
	 */
	public void removerCategoria(Categoria categoria) throws MyMon3yException {
		this.categoriaDAO.makeTransient(categoria);
	}

	/**
	 * @param login
	 * @throws PersistenciaMyMon3yException 
	 */
	public void removerUsuario(Usuario usuario) throws PersistenciaMyMon3yException {
		this.usuarioDAO.makeTransient(usuario);
	}

}

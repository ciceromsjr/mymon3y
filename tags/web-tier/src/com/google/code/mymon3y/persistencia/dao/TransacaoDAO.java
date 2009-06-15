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

import java.util.Date;
import java.util.List;

import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;

/**
 * Interface do DAO de {@link Transacao}.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public interface TransacaoDAO extends GenericDAO<Transacao, Long> {

	/**
	 * Retorna a quantidade de Transações do Usuário.
	 * 
	 * @param login
	 *            Login do Usuário.
	 * @return A quantidade de Transações do Usuário.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	Long getNumeroDeTransacoes(String login) throws PersistenciaMyMon3yException;
	
	List<Transacao> getTransacoes(String login) throws PersistenciaMyMon3yException;

	List<Transacao> getTransacoes(String login, Long idCategoria) throws PersistenciaMyMon3yException;

	/**
	 * Retorna a quantidade de notificações que um Usuário possui numa determinada data.
	 * 
	 * @param idDoUsuario
	 *            Identificador único do Usuário.
	 * @param data
	 *            Data da notificação.
	 * @return A quantidade de notificações que um Usuário possui numa determinada data.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	Long getNotificacoes(Long idDoUsuario, Date data) throws PersistenciaMyMon3yException;

	/**
	 * Retorna todas as notificaçõesp endentes no dia definido.
	 * 
	 * @param dataAvisoPrevio
	 *            Data da notificação.
	 * @return Lista das transações a serem notificadas.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	List<Transacao> getNotificacoes(final Date dataAvisoPrevio) throws PersistenciaMyMon3yException;
	
	/**
	 * Retorna as Transações de um Usuário que estão num dado intervalo.
	 * 
	 * @param login
	 *            Login do Usuário.
	 * @param inicio
	 *            Data inicial.
	 * @param fim
	 *            Data final.
	 * @return As Transações de um Usuário que estão num dado intervalo.
	 * @throws PersistenciaMyMon3yException
	 *             Caso algum erro de persistência ocorra.
	 */
	List<Transacao> getTransacoes(String login, Date inicio, Date fim) throws PersistenciaMyMon3yException;

}

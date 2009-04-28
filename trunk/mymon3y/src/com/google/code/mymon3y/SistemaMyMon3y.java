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
package com.google.code.mymon3y;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.xml.sax.SAXException;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Identificavel;
import com.google.code.mymon3y.model.Relatorio;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;
import com.google.code.mymon3y.persistencia.GerenciadorDePersistencia;
import com.google.code.mymon3y.persistencia.InvalidPropertiesException;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;
import com.google.code.mymon3y.util.OFXImport;
import com.google.code.mymon3y.util.PasswordHasher;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class SistemaMyMon3y {

	private GerenciadorDePersistencia gdp;
	
	private Calendar calendar;

	public SistemaMyMon3y() {
		this.gdp = new GerenciadorDePersistencia();
		this.calendar = Calendar.getInstance();
	}

	/**
	 * @throws MyMon3yException
	 * 
	 */
	public void zerarSistema() throws MyMon3yException {
		this.gdp.zerarSistema();
	}

	public void criarUsuario(String login, String senha) throws MyMon3yException {

		Usuario usuarioExistente = this.gdp.findUsuarioByLogin(login);
		if (usuarioExistente != null) {
			throw new MyMon3yException("Login não disponível.");
		}

		Usuario usuario = new Usuario(login, senha);
		validar(usuario);
		this.gdp.makePersistent(usuario);
	}

	/**
	 * @param login
	 * @return
	 */
	public Usuario getUsuario(String login) throws MyMon3yException {
		Usuario usuario = this.gdp.getUsuarioByLogin(login);
		if (usuario == null) {
			throw new MyMon3yException("Login inexistente.");
		}
		return usuario;
	}

	/**
	 * @param usuario
	 * @throws MyMon3yException
	 */
	public void atualizar(Usuario usuario) throws MyMon3yException {
		validar(usuario);
		this.gdp.atualizar(usuario);
	}

	public void validar(Usuario usuario) throws MyMon3yException {
		validar((Identificavel) usuario);
		if (!usuario.isCriptografada()) {
			usuario.criptografarSenha();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Identificavel> void validar(T obj) throws MyMon3yException {
		ClassValidator<T> cv = new ClassValidator<T>((Class<T>) obj.getClass());
		InvalidValue[] invalidValues = cv.getInvalidValues(obj);
		if (invalidValues.length > 0) {
			throw new InvalidPropertiesException(invalidValues);
		}
	}

	/**
	 * @param login
	 * @param nome
	 * @return
	 * @throws MyMon3yException 
	 */
	public Categoria getCategoria(String login, String nome) throws MyMon3yException {
		validarLogin(login);
		Categoria categoria = getCategoriaByNomeELoginDoUsuario(login, nome);
		if(categoria == null){
			throw new MyMon3yException("Categoria inexistente.");
		}
		return categoria;
	}

	/**
	 * @param login
	 * @throws MyMon3yException 
	 */
	private void validarLogin(String login) throws MyMon3yException {
		getUsuario(login);
	}

	private Categoria getCategoriaByNomeELoginDoUsuario(String login, String nome) throws MyMon3yException {
		return this.gdp.getCategoriaByNomeELoginDoUsuario(login, nome);
	}
	
	/**
	 * @param login
	 * @param valueOf
	 * @return
	 * @throws MyMon3yException 
	 */
	public Categoria getCategoria(String login, Long idCategoria) throws MyMon3yException {
		validarLogin(login);
		Categoria categoria = this.gdp.getCategoriaById(idCategoria);
		if(categoria == null){
			throw new MyMon3yException("Categoria inexistente.");
		}
		return categoria;
	}
	
	/**
	 * @param login
	 * @param nome
	 * @return
	 * @throws MyMon3yException 
	 */
	public Long adicionarCategoria(String login, Categoria categoria) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		validar(categoria);
		
		Categoria categoriaExistente = getCategoriaByNomeELoginDoUsuario(login, categoria.getNome());
		
		if(categoriaExistente != null){
			throw new MyMon3yException("Esta Categoria já existe.");
		}
		
		usuario.addCategorias(categoria);
		categoria.setUsuario(usuario);
		
		this.gdp.atualizar(usuario);
		
		return categoria.getId();
	}

	/**
	 * @param categoria
	 * @throws MyMon3yException 
	 */
	public void atualizar(Categoria categoria) throws MyMon3yException {
		validar(categoria);
		this.gdp.atualizar(categoria);
	}
	
	/**
	 * @param transacao
	 * @throws MyMon3yException 
	 */
	public void atualizar(Transacao transacao) throws MyMon3yException {
		validar(transacao);
		this.gdp.atualizar(transacao);
	}

	/**
	 * @param login
	 * @param valueOf
	 * @param transacao
	 * @return
	 * @throws MyMon3yException 
	 */
	public Long adicionarTransacao(String login, Long idCategoria, Transacao transacao) throws MyMon3yException {
		Categoria categoria = getCategoria(login, idCategoria);
		
		validar(transacao);
		
		transacao.setData(getDataNormalizada(transacao.getData()));
		transacao.setDataAvisoPrevio(getDataNormalizada(transacao.getDataAvisoPrevio()));
		
		categoria.addTransacao(transacao);
		transacao.setCategoria(categoria);
		
		this.gdp.atualizar(categoria);
		
		return transacao.getId();
	}

	/**
	 * @param data
	 * @return
	 */
	private Date getDataNormalizada(Date data) {
		
		if(data == null){
			return null;
		}
		
		this.calendar.setTime(data);
		this.calendar.set(Calendar.HOUR_OF_DAY, 0);
		this.calendar.set(Calendar.MINUTE, 0);
		this.calendar.set(Calendar.SECOND, 0);
		this.calendar.set(Calendar.MILLISECOND, 0);

		
		return this.calendar.getTime();
	}

	/**
	 * @param login
	 * @param valueOf
	 * @return
	 * @throws MyMon3yException 
	 */
	public Transacao getTransacao(String login, Long idTransacao) throws MyMon3yException {
		validarLogin(login);
		Transacao transacao = this.gdp.getTransacaoById(idTransacao);
		if(transacao == null){
			throw new MyMon3yException("Transação Inexistente.");
		}
		return transacao;
	}

	/**
	 * @param login
	 * @param valueOf
	 * @throws MyMon3yException 
	 */
	public void removerTransacao(String login, Long idTransacao) throws MyMon3yException {
		Transacao transacao = getTransacao(login, idTransacao);
		if(transacao == null){
			throw new MyMon3yException("Transação Inexistente.");
		}
		this.gdp.removerTransacao(transacao);
	}

	/**
	 * @param login
	 * @return
	 * @throws MyMon3yException 
	 */
	public Long getNumeroDeTransacoes(String login) throws MyMon3yException {
		validarLogin(login);
		return this.gdp.getNumeroDeTransacoes(login);
	}

	/**
	 * @param login
	 * @param valueOf
	 * @param valueOf2
	 * @throws MyMon3yException 
	 */
	public void editarCategoriaDaTransacao(String login, Long idTransacao, Long idCategoria) throws MyMon3yException {
		validarLogin(login);
		
		Transacao transacao = getTransacao(login, idTransacao);
		Categoria categoria = getCategoria(login, idCategoria);
		
		Categoria antigaCategoria = transacao.getCategoria();
		antigaCategoria.removerTransacao(transacao);
		
		transacao.setCategoria(categoria);
		categoria.addTransacao(transacao);
		
		atualizar(antigaCategoria);
		atualizar(categoria);
	}

	/**
	 * @param login
	 * @param valueOf
	 * @throws MyMon3yException 
	 */
	public void removerCategoria(String login, Long idCategoria) throws MyMon3yException {
		Categoria categoria = getCategoria(login, idCategoria);
		if(categoria.getTransacoes().size() > 0){
			throw new MyMon3yException("Esta Categoria possui transações associadas.");
		}
		this.gdp.removerCategoria(categoria);
	}

	/**
	 * @param login
	 * @param senha
	 * @throws MyMon3yException 
	 */
	public void removerUsuario(String login, String senha) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		String senhaVerdadeira = usuario.getSenha();
		String senhaInformada = PasswordHasher.getSha256(senha);
		if(!senhaVerdadeira.equals(senhaInformada)){
			throw new MyMon3yException("Senha errada.");
		}
		this.gdp.removerUsuario(usuario);
	}

	/**
	 * @param login
	 * @param dataFormatada
	 * @return
	 * @throws MyMon3yException 
	 */
	public Long getNotificacoes(String login, Date data) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		return this.gdp.getNotificacoes(usuario.getId(), getDataNormalizada(data));
	}

	
	public Relatorio criarRelatorio(String login, Date inicio, Date fim) throws MyMon3yException {
		validarLogin(login);
		List transacoes = this.gdp.getTransacoesByLogin(login, inicio, fim);
		Relatorio r = new Relatorio(this.getUsuario(login), inicio, fim, transacoes);
		return r;
	}

	public void importarOFX(String login, String arquivo) throws MyMon3yException, SAXException, IOException, ParserConfigurationException {
		List<Transacao> list = OFXImport.readOFX(arquivo);
		for (Transacao t : list) {
			adicionarTransacao(login, getCategoriaByNomeELoginDoUsuario(login, "Outro").getId(), t);
		}
	}
	
}

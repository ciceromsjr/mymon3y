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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Relatorio;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;

/**
 * Fachada dos testes de aceitação EasyAccept.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class FacadeEasyAccept {

	/**
	 * Fachada do sistema.
	 */
	private SistemaMyMon3y sistema;

	/**
	 * Formatador das datas das Transações.
	 */
	private DateFormat dateFormatTransacao;

	/**
	 * Formatador do valor monetário das Transações.
	 */
	private Pattern numberFormatTransacao;

	/**
	 * Calendário usado na manipulação com datas.
	 */
	private Calendar calendar;

	/**
	 * Armazena o último relatório extraído do sistema.
	 */
	private Relatorio r;

	/**
	 * Construtor vazio.
	 */
	public FacadeEasyAccept() {
		this.sistema = new SistemaMyMon3y();
		this.dateFormatTransacao = new SimpleDateFormat("dd/MM/yyyy");
		this.numberFormatTransacao = Pattern.compile("\\d+,\\d\\d");
		this.calendar = new GregorianCalendar();
	}

	/**
	 * @see SistemaMyMon3y#zerarSistema()
	 */
	public void zerarSistema() throws MyMon3yException {
		this.sistema.zerarSistema();
	}

	/**
	 * @see SistemaMyMon3y#criarUsuario(String, String)
	 */
	public void criarUsuario(String login, String senha) throws MyMon3yException {
		this.sistema.criarUsuario(login, senha);
	}

	/**
	 * @see SistemaMyMon3y#atualizar(Usuario)
	 */
	public void editarAtributoUsuario(String login, String atributo, String valor) throws MyMon3yException {
		Usuario usuario = this.sistema.getUsuario(login);
		if (atributo.equals("senha")) {
			usuario.setSenha(valor);
		} else {
			throw new MyMon3yException("Atributo inválido.");
		}
		this.sistema.atualizar(usuario);
	}

	/**
	 * Retorna o valor de um atributo do Usuário.
	 * 
	 * @param login
	 *            Login do Usuário.
	 * @param atributo
	 *            Nome do atributo a ser retornado.
	 * @return Valor de um atributo do Usuário.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public String getAtributoUsuario(String login, String atributo) throws MyMon3yException {
		Usuario usuario = this.sistema.getUsuario(login);
		if (atributo.equals("login")) {
			return usuario.getLogin();
		} else if (atributo.equals("senha")) {
			return usuario.getSenha();
		}
		return null;
	}

	/**
	 * @see SistemaMyMon3y#adicionarCategoria(String, Categoria)
	 */
	public Long criarCategoria(String login, String nome) throws MyMon3yException {
		return this.sistema.adicionarCategoria(login, new Categoria(nome));
	}

	/**
	 * @see SistemaMyMon3y#getCategoria(String, Long)
	 */
	public Long getIdCategoria(String login, String nome) throws MyMon3yException {
		Categoria categoria = this.sistema.getCategoria(login, nome);
		return categoria.getId();
	}

	/**
	 * @see SistemaMyMon3y#atualizar(Categoria)
	 */
	public void editarCategoria(String login, String idCategoria, String atributo, String valor)
			throws MyMon3yException {
		Categoria categoria = this.sistema.getCategoria(login, Long.valueOf(idCategoria));
		if (atributo.equals("nome")) {
			categoria.setNome(valor);
		}
		this.sistema.atualizar(categoria);
	}

	/**
	 * @see SistemaMyMon3y#adicionarTransacao(String, Long, Transacao)
	 */
	public Long criarTransacao(String login, String descricao, String data, String valor, String idCategoria,
			String comentario, String ndias, String credito) throws MyMon3yException {
		Date dataFormatada = validarDataTransacao(data);
		Integer valorFormatado = validarValorTransacao(valor);
		Date dataAvisoPrevioFormatada = validarDataAvisoPrevioTransacao(dataFormatada, ndias);

		return this.sistema.adicionarTransacao(login, Long.valueOf(idCategoria), new Transacao(descricao,
				dataFormatada, valorFormatado, comentario, dataAvisoPrevioFormatada, Boolean.valueOf(credito)));
	}

	/**
	 * Faz a validação de data de aviso prévio de uma Transação.
	 * 
	 * @param data
	 *            Data a ser validada.
	 * @param ndias
	 *            Número de dias, antes da data que a Transação ocorreu/ocorrerá, do aviso prévio.
	 * @return Data validada.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	private Date validarDataAvisoPrevioTransacao(Date data, String ndias) throws MyMon3yException {

		if (ndias == null || ndias.trim().equals("")) {
			return null;
		}

		Integer n = Integer.valueOf(ndias);
		if (n < 0) {
			throw new MyMon3yException("Número de Dias Inválido.");
		}
		this.calendar.setTime(data);
		this.calendar.add(Calendar.DAY_OF_MONTH, -n);
		return this.calendar.getTime();
	}

	/**
	 * Faz a validação do valor monetário de uma Transação.
	 * 
	 * @param valor
	 *            Valor monetário a ser validado.
	 * @return {@link Integer} representando o valor monetário.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	private Integer validarValorTransacao(String valor) throws MyMon3yException {
		Integer valorFormatado = null;
		if (!this.numberFormatTransacao.matcher(valor).matches()) {
			throw new MyMon3yException("Valor Inválido.");
		}
		valorFormatado = Integer.valueOf(valor.replace(",", ""));
		return valorFormatado;
	}

	/**
	 * Faz a validação da data de ocorrência de uma Transação.
	 * 
	 * @param data
	 *            Data a ser validada.
	 * @return Um {@link Date} representando a data de ocorrência da Transação.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	private Date validarDataTransacao(String data) throws MyMon3yException {
		Date dataFormatada = null;
		try {
			dataFormatada = this.dateFormatTransacao.parse(data);
		} catch (ParseException e) {
			throw new MyMon3yException("Data Inválida.");
		}
		return dataFormatada;
	}

	/**
	 * {@link #getDataAvisoPrevio(Date, Date)

	 */
	private Long getDataAvisoPrevio(Transacao transacao) {
		return getDataAvisoPrevio(transacao.getData(), transacao.getDataAvisoPrevio());
	}

	/**
	 * Retorna a diferença em dias da data de aviso prévio e a data de ocorrência da Transação.
	 * 
	 * @param dataTransacao
	 *            Data da Transação.
	 * @param dataAvisoPrevio
	 *            Data de aviso prévio da Transação.
	 * @return Diferença em dias da data de aviso prévio e a data de ocorrência da Transação.
	 */
	private Long getDataAvisoPrevio(Date dataTransacao, Date dataAvisoPrevio) {
		if (dataAvisoPrevio == null) {
			return null;
		}

		return getDiferenca(dataTransacao, dataAvisoPrevio);
	}

	/**
	 * Retorna a diferença, em dias, entre duas datas.
	 * 
	 * @param data1
	 *            Primeira data.
	 * @param data2
	 *            Segunda data.
	 * @return Diferença, em dias, entre duas datas.
	 */
	private long getDiferenca(Date data1, Date data2) {
		if (data1.before(data2)) {
			Date tmp = data1;
			data1 = data2;
			data2 = tmp;
		}
		return TimeUnit.DAYS.convert(data1.getTime() - data2.getTime(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Retorna o valor de um atributo de uma Transação.
	 * 
	 * @param login
	 *            Login do Usuário dono da Transação.
	 * @param idTransacao
	 *            Identificador único da Transação.
	 * @param atributo
	 *            Atributo da Transação a ser retornado.
	 * @return O valor de um atributo de uma Transação.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public String getAtributoTransacao(String login, String idTransacao, String atributo) throws MyMon3yException {
		Transacao transacao = this.sistema.getTransacao(login, Long.valueOf(idTransacao));
		if (atributo.equals("descricao")) {
			return transacao.getDescricao();
		} else if (atributo.equals("data")) {
			return this.dateFormatTransacao.format(transacao.getData());
		} else if (atributo.equals("valor")) {
			return formatarValor(transacao.getValor());
		} else if (atributo.equals("categoria")) {
			return transacao.getCategoria().getNome();
		} else if (atributo.equals("comentario")) {
			return transacao.getComentario();
		} else if (atributo.equals("ndias")) {
			return String.valueOf(getDataAvisoPrevio(transacao));
		} else if (atributo.equals("credito")) {
			return String.valueOf(transacao.getCredito());
		}
		return null;
	}

	/**
	 * Formata, em string, o valor monetário de uma Transação.
	 * 
	 * @param valor
	 *            Valor monetário.
	 * @return O valor monetário de uma Transação no formato string.
	 */
	private String formatarValor(Integer valor) {
		String decimal = String.valueOf((valor / 100));
		String centavos = String.valueOf((valor % 100));
		if (centavos.length() < 2) {
			centavos = "0" + centavos;
		}
		return decimal + "," + centavos;
	}

	/**
	 * Edita o atributo de uma Transação.
	 * 
	 * @param login
	 *            Login do Usuário dono da Transação.
	 * @param idTransacao
	 *            Identificador único da Transação.
	 * @param atributo
	 *            Atributo da Transação a ser editado.
	 * @param valor
	 *            Novo valor do atributo.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public void editarAtributoTransacao(String login, String idTransacao, String atributo, String valor)
			throws MyMon3yException {
		Transacao transacao = this.sistema.getTransacao(login, Long.valueOf(idTransacao));
		if (atributo.equals("descricao")) {
			transacao.setDescricao(valor);
		} else if (atributo.equals("data")) {
			transacao.setData(validarDataTransacao(valor));
		} else if (atributo.equals("valor")) {
			transacao.setValor(validarValorTransacao(valor));
		} else if (atributo.equals("comentario")) {
			transacao.setComentario(valor);
		} else if (atributo.equals("ndias")) {
			transacao.setDataAvisoPrevio(validarDataAvisoPrevioTransacao(transacao.getData(), valor));
		} else if (atributo.equals("credito")) {
			transacao.setCredito(Boolean.valueOf(valor));
		}
		this.sistema.atualizar(transacao);
	}

	/**
	 * @see SistemaMyMon3y#editarCategoriaDaTransacao(String, Long, Long)
	 */
	public void editarCategoriaDaTransacao(String login, String idTransacao, String idCategoria)
			throws NumberFormatException, MyMon3yException {
		this.sistema.editarCategoriaDaTransacao(login, Long.valueOf(idTransacao), Long.valueOf(idCategoria));
	}

	/**
	 * @see SistemaMyMon3y#removerTransacao(String, Long)
	 */
	public void removerTransacao(String login, String idTransacao) throws MyMon3yException {
		this.sistema.removerTransacao(login, Long.valueOf(idTransacao));
	}

	/**
	 * @see SistemaMyMon3y#getNumeroDeTransacoes(String)
	 */
	public Long getNumeroDeTransacoes(String login) throws MyMon3yException {
		return this.sistema.getNumeroDeTransacoes(login);
	}

	/**
	 * @see SistemaMyMon3y#removerCategoria(String, Long)
	 */
	public void removerCategoria(String login, String idCategoria) throws MyMon3yException {
		this.sistema.removerCategoria(login, Long.valueOf(idCategoria));
	}

	/**
	 * @see SistemaMyMon3y#removerUsuario(String, String)
	 */
	public void removerUsuario(String login, String senha) throws MyMon3yException {
		this.sistema.removerUsuario(login, senha);
	}

	/**
	 * @see SistemaMyMon3y#getNotificacoes(String, Date)
	 */
	public Long getNotificacoes(String login, String data) throws MyMon3yException {
		Date dataFormatada = null;
		try {
			dataFormatada = this.dateFormatTransacao.parse(data);
		} catch (ParseException e) {
			throw new MyMon3yException("Data Inválida.");
		}
		return this.sistema.getNotificacoes(login, dataFormatada);
	}

	/**
	 * @see SistemaMyMon3y#criarRelatorio(String, Date, Date)
	 */
	public void criarRelatorio(String login, String inicio, String fim) throws MyMon3yException {
		Date inicioData = null;
		Date fimData = null;
		try {
			inicioData = this.dateFormatTransacao.parse(inicio);
			fimData = this.dateFormatTransacao.parse(fim);
		} catch (ParseException e) {
			throw new MyMon3yException("Data Inválida.");
		}
		r = this.sistema.criarRelatorio(login, inicioData, fimData);
	}

	public String getRelatorioLinha(String linha) {
		return r.getTransacoesCSV().get(Integer.parseInt(linha));
	}
	
	/**
	 * @see Relatorio#getNumeroDeTransacoesDebito()
	 */
	public int getRelatorioNumeroDeTransacoesDebito() {
		return r.getNumeroDeTransacoesDebito();
	}

	/**
	 * @see Relatorio#getNumeroDeTransacoesCredito()
	 */
	public int getRelatorioNumeroDeTransacoesCredito() {
		return r.getNumeroDeTransacoesCredito();
	}

	/**
	 * @see SistemaMyMon3y#importarOFX(String, String)
	 */
	public void importarOFX(String login, String arquivo) throws MyMon3yException, SAXException, IOException,
			ParserConfigurationException {
		this.sistema.importarOFX(login, arquivo);
	}

}
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class FacadeEasyAccept {

	private SistemaMyMon3y sistema;

	private DateFormat dateFormatTransacao;

	private Pattern numberFormatTransacao;

	public FacadeEasyAccept() {
		this.sistema = new SistemaMyMon3y();
		this.dateFormatTransacao = new SimpleDateFormat("dd/MM/yyyy");
		this.numberFormatTransacao = Pattern.compile("\\d+,\\d\\d");
	}

	public void zerarSistema() throws MyMon3yException {
		this.sistema.zerarSistema();
	}

	public void criarUsuario(String login, String senha) throws MyMon3yException {
		this.sistema.criarUsuario(login, senha);
	}

	public void editarAtributoUsuario(String login, String atributo, String valor) throws MyMon3yException {
		Usuario usuario = this.sistema.getUsuario(login);
		if (atributo.equals("senha")) {
			usuario.setSenha(valor);
		} else {
			throw new MyMon3yException("Atributo inválido.");
		}
		this.sistema.atualizar(usuario);
	}

	public String getAtributoUsuario(String login, String atributo) throws MyMon3yException {
		Usuario usuario = this.sistema.getUsuario(login);
		if (atributo.equals("login")) {
			return usuario.getLogin();
		} else if (atributo.equals("senha")) {
			return usuario.getSenha();
		}
		return null;
	}

	public Long criarCategoria(String login, String nome) throws MyMon3yException {
		return this.sistema.adicionarCategoria(login, new Categoria(nome));
	}

	public Long getIdCategoria(String login, String nome) throws MyMon3yException {
		Categoria categoria = this.sistema.getCategoria(login, nome);
		return categoria.getId();
	}

	public void editarCategoria(String login, String idCategoria, String atributo, String valor)
			throws MyMon3yException {
		Categoria categoria = this.sistema.getCategoria(login, Long.valueOf(idCategoria));
		if (atributo.equals("nome")) {
			categoria.setNome(valor);
		}
		this.sistema.atualizar(categoria);
	}

	public Long criarTransacao(String login, String descricao, String data, String valor, String idCategoria,
			String comentario, String ndias, String credito) throws MyMon3yException {
		Date dataFormatada = validarDataTransacao(data);
		Integer valorFormatado = validarValorTransacao(valor);

		return this.sistema.adicionarTransacao(login, Long.valueOf(idCategoria), new Transacao(descricao,
				dataFormatada, valorFormatado, comentario, Integer.valueOf(ndias), Boolean.valueOf(credito)));
	}

	/**
	 * @param valor
	 * @return
	 * @throws MyMon3yException
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
	 * @param data
	 * @return
	 * @throws MyMon3yException
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
			return String.valueOf(transacao.getNDiasAntes());
		} else if (atributo.equals("credito")) {
			return String.valueOf(transacao.getCredito());
		}
		return null;
	}

	/**
	 * @param valor
	 * @return
	 */
	private String formatarValor(Integer valor) {
		String decimal = String.valueOf((valor / 100));
		String centavos = String.valueOf((valor % 100));
		if (centavos.length() < 2) {
			centavos = "0" + centavos;
		}
		return decimal + "," + centavos;
	}

	public void editarAtributoTransacao(String login, String idTransacao, String atributo, String valor) throws MyMon3yException {
		Transacao transacao = this.sistema.getTransacao(login, Long.valueOf(idTransacao));
		if(atributo.equals("descricao")){
			transacao.setDescricao(valor);
		} else if(atributo.equals("data")){
			transacao.setData(validarDataTransacao(valor));
		} else if(atributo.equals("valor")){
			transacao.setValor(validarValorTransacao(valor));
		} else if(atributo.equals("comentario")){
			transacao.setComentario(valor);
		} else if(atributo.equals("ndias")){
			transacao.setNDiasAntes(Integer.valueOf(valor));
		} else if(atributo.equals("credito")){
			transacao.setCredito(Boolean.valueOf(valor));
		}
		this.sistema.atualizar(transacao);
	}

	public void editarCategoriaDaTransacao(String login, String idTransacao, String idCategoria) throws NumberFormatException, MyMon3yException{
		this.sistema.editarCategoriaDaTransacao(login, Long.valueOf(idTransacao), Long.valueOf(idCategoria));
	}
	
	public void removerTransacao(String login, String idTransacao) throws MyMon3yException {
		this.sistema.removerTransacao(login, Long.valueOf(idTransacao));
	}

	public Long getNumeroDeTransacoes(String login) throws MyMon3yException {
		return this.sistema.getNumeroDeTransacoes(login);
	}

	public void removerCategoria(String login, String idCategoria) throws MyMon3yException {
		this.sistema.removerCategoria(login, Long.valueOf(idCategoria));
	}

	public void removerUsuario(String login, String senha) throws MyMon3yException {
		this.sistema.removerUsuario(login, senha);
	}

}

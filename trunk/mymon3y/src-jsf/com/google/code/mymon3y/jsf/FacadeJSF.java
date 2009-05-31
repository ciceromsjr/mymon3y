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
package com.google.code.mymon3y.jsf;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.google.code.mymon3y.MyMon3yException;
import com.google.code.mymon3y.jsf.converters.CreditoDebitoConverter;
import com.google.code.mymon3y.jsf.util.ConstantesJSF;
import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;

/**
 * 
 * TODO: Limpar os debugs
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class FacadeJSF extends ManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String confirmacaoDeSenha;

	/**
	 * Usuário da Sessão, caso esse exista. Caso contrário um novo usuário.
	 */
	private Usuario usuario;

	private Usuario usuarioAtualizar;

	private Categoria categoria;

	private List<Categoria> categorias;

	private List<SelectItem> categoriasSelectItems;

	private Long idCategoriaSelectedItem;

	private List<SelectItem> novaCategoriasSelectItems;

	private Long idNovaCategoriaSelectedItem;

	private Transacao transacao;

	private List<Transacao> transacoes;

	private String ehCredito;

	private List<SelectItem> ehCreditoSelectItems;

	@SuppressWarnings("serial")
	private List<SelectItem> EH_CREDITO_SELECT_ITEMS = new ArrayList<SelectItem>(){{
		add(new SelectItem(CreditoDebitoConverter.CREDITO));
		add(new SelectItem(CreditoDebitoConverter.DEBITO));
	}};

	public FacadeJSF() {
		this.usuario = getUsuarioDaSessao();
		this.usuario = this.usuario != null ? this.usuario : new Usuario();

		this.usuarioAtualizar = new Usuario();

		this.categoria = new Categoria();

		this.transacao = new Transacao();
		this.ehCreditoSelectItems = EH_CREDITO_SELECT_ITEMS;

	}

	public String getConfirmacaoDeSenha() {
		return confirmacaoDeSenha;
	}

	public void setConfirmacaoDeSenha(String confirmacaoDeSenha) {
		this.confirmacaoDeSenha = confirmacaoDeSenha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<SelectItem> getEhCreditoSelectItems() {
		return ehCreditoSelectItems;
	}

	public void setEhCreditoSelectItems(List<SelectItem> ehCreditoSelectItems) {
		this.ehCreditoSelectItems = ehCreditoSelectItems;
	}

	public String getEhCredito() {
		debug("getEhCredito FOI CHAMADO - this.transacao != null && this.transacao.getCredito() != null?");
		if (this.transacao != null && this.transacao.getCredito() != null) {
			debug("VERDADE");
			if (this.transacao.getCredito()) {
				debug(CreditoDebitoConverter.CREDITO);
				this.ehCredito = CreditoDebitoConverter.CREDITO;
			} else {
				debug(CreditoDebitoConverter.DEBITO);
				this.ehCredito = CreditoDebitoConverter.DEBITO;
			}
		} else {
			debug("FALSO");
		}
		return this.ehCredito;
	}

	public void setEhCredito(String ehCredito) {
		this.ehCredito = ehCredito;
		if (ehCredito.equals(CreditoDebitoConverter.CREDITO)) {
			this.transacao.setCredito(true);
		} else {
			this.transacao.setCredito(false);
		}
	}

	public List<SelectItem> getCategoriasSelectItems() {
		if (this.categoriasSelectItems == null || ehFase6RenderResponse()) {
			this.categoriasSelectItems = getCategoriasSelectItemsDoBanco();
			this.categoriasSelectItems.add(0, new SelectItem(null, ""));
		}
		return categoriasSelectItems;
	}

	private List<SelectItem> getCategoriasSelectItemsDoBanco() {

		List<SelectItem> categoriasSelectItems = null;

		this.categoria.setNome("");
		List<Categoria> categorias = getCategorias();

		categoriasSelectItems = new ArrayList<SelectItem>(categorias.size());
		for (Categoria c : categorias) {
			categoriasSelectItems.add(new SelectItem(c.getId(), c.getNome()));
		}

		return categoriasSelectItems;
	}

	public void setCategoriasSelectItems(List<SelectItem> categoriasSelectItems) {
		this.categoriasSelectItems = categoriasSelectItems;
	}

	public List<SelectItem> getNovaCategoriasSelectItems() {
		if (this.novaCategoriasSelectItems == null || ehFase6RenderResponse()) {
			this.novaCategoriasSelectItems = getCategoriasSelectItemsDoBanco();
		}
		return novaCategoriasSelectItems;
	}

	public void setNovaCategoriasSelectItems(List<SelectItem> novaCategoriasSelectItems) {
		this.novaCategoriasSelectItems = novaCategoriasSelectItems;
	}

	public Long getIdNovaCategoriaSelectedItem() {
		if (this.transacao != null && this.transacao.getCategoria() != null) {
			this.idNovaCategoriaSelectedItem = this.transacao.getCategoria().getId();
		}
		return idNovaCategoriaSelectedItem;
	}

	public void setIdNovaCategoriaSelectedItem(Long idNovaCategoriaSelectedItem) {
		this.idNovaCategoriaSelectedItem = idNovaCategoriaSelectedItem;
	}

	public Long getIdCategoriaSelectedItem() {
		return idCategoriaSelectedItem;
	}

	public void setIdCategoriaSelectedItem(Long idCategoriaSelectedItem) {
		debug("Setado valor para categoriaSelectedItem:");
		debug(idCategoriaSelectedItem);
		this.idCategoriaSelectedItem = idCategoriaSelectedItem;
	}

	public Transacao getTransacao() {
		return transacao;
	}

	public void setTransacao(Transacao transacao) {
		this.transacao = transacao;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public List<Categoria> getCategorias() {
		if (this.categorias == null || ehFase6RenderResponse()) {
			try {
				debug("Pesquisando Categorias com o seguinte nome: " + this.categoria.getNome());
				this.categorias = getFacade().pesquisarCategorias(getLoginUsuarioNaSessao(), this.categoria.getNome());
			} catch (MyMon3yException e) {
				e.printStackTrace();
				debug("Não foi possível recuperar as Categorias cadastradas!");
				this.categorias = new ArrayList<Categoria>();
			}
		}

		return this.categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Transacao> getTransacoes() {
		debug("\"getTransacoes\" foi chamado!!! this.idCategoriaSelectedItem:");
		debug(this.idCategoriaSelectedItem);

		if (this.transacoes == null || ehFase6RenderResponse()) {
			debug("Pesquisando Transacoes da seguinte Categoria (id): " + this.idCategoriaSelectedItem);

			if (this.idCategoriaSelectedItem == null) {
				// Buscar todas as Transações do Usuário
				try {
					this.transacoes = getFacade().pesquisarTransacao(getLoginUsuarioNaSessao());
				} catch (MyMon3yException e) {
					e.printStackTrace();
					debug("Não foi possível recuperar as Transacoes cadastradas (busca por TODAS)!");
					this.transacoes = new ArrayList<Transacao>();
				}

			} else {
				// Buscar apenas as Transações que estão associadas com a Categoria informada

				try {
					this.transacoes = getFacade().pesquisarTransacao(getLoginUsuarioNaSessao(),
							this.idCategoriaSelectedItem);
				} catch (MyMon3yException e) {
					e.printStackTrace();
					debug("Não foi possível recuperar as Transacoes cadastradas (buscar Transacoes associadas com Categoria "
							+ this.idCategoriaSelectedItem + ")!");
					this.transacoes = new ArrayList<Transacao>();
				}

			}
		}

		return transacoes;
	}

	public void setTransacoes(List<Transacao> transacoes) {
		this.transacoes = transacoes;
	}

	public Usuario getUsuarioAtualizar() {
		return usuarioAtualizar;
	}

	public void setUsuarioAtualizar(Usuario usuarioAtualizar) {
		this.usuarioAtualizar = usuarioAtualizar;
	}

	/**
	 * @return
	 */
	private String getLoginUsuarioNaSessao() {
		return getIdentificadorDoUsuarioNaSessao();
	}

	// ================================== MÉTODOS DE AÇÃO ==================================

	public String registrarUsuario() {
		if (!this.usuario.getSenha().equals(this.confirmacaoDeSenha)) {
			String mensagemDeErro = "Senha e confirmação não são iguais. O mesmo valor deve ser inserido nos dois campos.";
			addMensagemErro(mensagemDeErro);
			return ConstantesJSF.FALHA;
		}

		try {
			getFacade().criarUsuario(usuario);
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		String mensagemBemVindo = "Olá \"" + usuario.getLogin() + "\", sua conta foi criada com sucesso.";
		addMensagemSucesso(mensagemBemVindo);

		return ConstantesJSF.SUCESSO;
	}

	public String atualizarUsuario() {
		Usuario usuarioDaSessao = getUsuarioDaSessao();

		this.usuario.criptografarSenha();
		// Usuário informou a senha atual correta?
		if (!this.usuario.getSenha().equals(usuarioDaSessao.getSenha())) {
			String mensagem = "Senha Atual Incorreta";
			addMensagemErro(mensagem);
			return ConstantesJSF.FALHA;
		}

		// Usuário digitou a senha nova corretamente duas vezes?
		if (!this.usuarioAtualizar.getSenha().equals(this.confirmacaoDeSenha)) {
			String mensagem = "Senha e confirmação não são iguais. O mesmo valor deve ser inserido nos dois campos.";
			addMensagemErro(mensagem);
			return ConstantesJSF.FALHA;
		}

		this.usuario.setSenha(this.usuarioAtualizar.getSenha());
		try {
			getFacade().atualizar(usuario);
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		String mensagem = "Os dados do Usuário \"" + usuario.getLogin() + "\" foram atualizados.";
		addMensagemSucesso(mensagem);

		return ConstantesJSF.SUCESSO;
	}

	public String excluirConta() {
		try {
			getFacade().removerUsuario(this.usuario.getLogin(), this.usuario.getSenha());
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		return ConstantesJSF.SUCESSO;
	}

	public String criarCategoria() {

		try {
			getFacade().adicionarCategoria(getLoginUsuarioNaSessao(), this.categoria);
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		addMensagemSucesso("A Categoria \"" + this.categoria.getNome() + "\" foi inserida com sucesso.");
		this.categoria = new Categoria();

		return ConstantesJSF.SUCESSO;
	}

	public String apagarCategoria() {
		try {
			getFacade().removerCategoria(getIdentificadorDoUsuarioNaSessao(), this.categoria.getId());
			this.categorias = null;
		} catch (MyMon3yException e) {
			e.printStackTrace();
			addMensagemErro(e.getMessage());
		}

		return ConstantesJSF.BRANCO;
	}

	public String atualizarCategoria() {

		try {
			getFacade().atualizar(this.categoria);
			this.categoria = new Categoria();
			this.categoria.setNome("");
			this.categorias = null;
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		return ConstantesJSF.SUCESSO;
	}

	public String criarTransacao() {

		try {
			getFacade().adicionarTransacao(getLoginUsuarioNaSessao(), this.idNovaCategoriaSelectedItem, this.transacao);
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}

		addMensagemSucesso("A Transação foi inserida com sucesso.");
		this.transacao = new Transacao();

		return ConstantesJSF.SUCESSO;
	}

	public String apagarTransacao() {
		try {
			getFacade().removerTransacao(getIdentificadorDoUsuarioNaSessao(), this.transacao.getId());
			this.transacoes = null;
		} catch (MyMon3yException e) {
			e.printStackTrace();
			addMensagemErro(e.getMessage());
		}

		return ConstantesJSF.BRANCO;
	}

	public String atualizarTransacao(){
		try {
			if(!this.transacao.getCategoria().getId().equals(this.idNovaCategoriaSelectedItem)){
				debug("Atualizando transação mudando a Categoria");
				getFacade().editarTransacao(getLoginUsuarioNaSessao(), this.transacao, this.idNovaCategoriaSelectedItem);
			} else {
				debug("Atualizando transação NÃO mudando a Categoria");
				getFacade().atualizar(this.transacao);
			}
			this.transacao = new Transacao();
			this.transacoes = null;
			this.novaCategoriasSelectItems = null;
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}
		
		return ConstantesJSF.SUCESSO;
	}
	
	// ================================== MÉTODOS QUE RETORNAM CONSTANTES ==================================

	public String pesquisarCategoria() {

		return "listar_categorias";
	}

	public String pesquisarTransacao() {

		return "listar_transacoes";
	}

	public String novaPesquisaCategoria() {

		this.categoria = new Categoria();
		return "gerenciar_categoria";
	}

	public String novaPesquisaTransacao() {

		return "gerenciar_transacao";
	}

	// "atualizar_categoria"
	// ================================== MÉTODOS QUE PREPARAM O CENÁRIO ==================================

	public String prepararAtualizarCategoria() {

		try {
			this.categoria = getFacade().getCategoria(getLoginUsuarioNaSessao(), this.categoria.getId());
		} catch (MyMon3yException e) {
			e.printStackTrace();
			debug("Não foi possível recuperar do banco a Categoria que deveria ser atualizada! Estado do bean com as informações:");
			debug(this.categoria);
		}

		return "atualizar_categoria";
	}

	public String prepararAtualizarTransacao() {

		try {
			this.transacao = getFacade().getTransacao(getLoginUsuarioNaSessao(), this.transacao.getId());
		} catch (MyMon3yException e) {
			e.printStackTrace();
			debug("Não foi possível recuperar do banco a Transação que deveria ser atualizada! Estado do bean com as informações:");
			debug(this.transacao);
		}

		return "atualizar_transacao";
	}
}

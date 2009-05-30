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

import com.google.code.mymon3y.MyMon3yException;
import com.google.code.mymon3y.jsf.util.ConstantesJSF;
import com.google.code.mymon3y.model.Usuario;

/**
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

	public FacadeJSF() {
		this.usuario = getUsuarioDaSessao();
		this.usuario = this.usuario != null ? this.usuario : new Usuario();

		this.usuarioAtualizar = new Usuario();
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

	public Usuario getUsuarioAtualizar() {
		return usuarioAtualizar;
	}

	public void setUsuarioAtualizar(Usuario usuarioAtualizar) {
		this.usuarioAtualizar = usuarioAtualizar;
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
	
	public String excluirConta(){
		try {
			getFacade().removerUsuario(this.usuario.getLogin(), this.usuario.getSenha());
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
			return ConstantesJSF.FALHA;
		}
		
		return ConstantesJSF.SUCESSO;
	}

}

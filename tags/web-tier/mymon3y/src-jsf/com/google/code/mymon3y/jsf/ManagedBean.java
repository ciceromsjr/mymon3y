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

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import com.google.code.mymon3y.MyMon3yException;
import com.google.code.mymon3y.SistemaMyMon3y;
import com.google.code.mymon3y.model.Usuario;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class ManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ID_USUARIO = "ID_USUARIO";
	
	public ManagedBean() {
	}

	protected boolean ehFase6RenderResponse(){
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getRenderResponse();
	}
	
	@SuppressWarnings("unchecked")
	protected void guardarNaSessao(String chave, Object valor) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put(chave, valor);
	}

	protected void retirarDaSessao(String chave) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove(chave);
	}

	protected Object acessarNaSessao(String chave) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext().getSessionMap().get(chave);
	}

	protected void addMensagemErro(String mensagem) {
		addMensagem(FacesMessage.SEVERITY_ERROR, mensagem);
	}

	protected void addMensagemSucesso(String mensagem) {
		addMensagem(FacesMessage.SEVERITY_INFO, mensagem);
	}

	private void addMensagem(Severity severity, String mensagem) {
		addMensagem(severity, "form", mensagem);
	}
	
	private void addMensagem(Severity severity, String id, String mensagem) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(severity, mensagem, mensagem);
		context.addMessage(id, message);

	}

	protected SistemaMyMon3y getFacade() {
		return new SistemaMyMon3y();
	}

	protected void setIdentificadorDoUsuarioNaSessao(Usuario usuario){
		guardarNaSessao(ID_USUARIO, usuario.getLogin());
	}
	
	protected String getIdentificadorDoUsuarioNaSessao(){
		return (String) acessarNaSessao(ID_USUARIO);
	}
	
	protected void retirarIdentificadorDoUsuarioDaSessao(){
		retirarDaSessao(ID_USUARIO);
	}
	
	protected Usuario getUsuarioDaSessao() {

		String loginUsuarioDaSessao = (String) acessarNaSessao(ID_USUARIO);
		Usuario usuarioDaSessao = null;

		try {
			usuarioDaSessao = this.getFacade().getUsuario(loginUsuarioDaSessao);
		} catch (MyMon3yException e) {
			debug("TENTEI PEGAR USUÁRIO DA SESSÃO MAS NÃO TEM NENHUM!");
		}

		return usuarioDaSessao;
	}

	@Deprecated
	protected void debug(Object o) {
		if (o != null) {
			debug(o.toString());
		} else {
			debug("O objeto a ser impresso era null.");
		}
	}

	private void debug(String mensagem) {
		System.out.println("===========================================================================");
		System.out.println("=");
		System.out.println("=");
		System.out.println(mensagem);
		System.out.println("=");
		System.out.println("=");
		System.out.println("===========================================================================");
	}
}

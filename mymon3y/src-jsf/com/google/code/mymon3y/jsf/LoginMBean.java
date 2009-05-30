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

import javax.faces.event.PhaseEvent;

import com.google.code.mymon3y.MyMon3yException;
import com.google.code.mymon3y.SistemaMyMon3y;
import com.google.code.mymon3y.jsf.util.ConstantesJSF;
import com.google.code.mymon3y.model.Usuario;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class LoginMBean extends ManagedBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String login;

	private String senha;

	public static final String ID_USUARIO = "ID_USUARIO";

	public LoginMBean() {

	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String logar() {
		SistemaMyMon3y sm = new SistemaMyMon3y();
		Usuario usuario = null;
		
		try {
			usuario = sm.getUsuario(login);
		} catch (MyMon3yException e) {
			addMensagemErro(e.getMessage());
		}
		
		boolean loginValido = sm.validoLoginESenha(usuario, senha); 
		if(loginValido){
			guardarNaSessao(ID_USUARIO, usuario.getLogin());
		}
		
		return loginValido ? ConstantesJSF.SUCESSO_LOGIN : ConstantesJSF.FALHA_LOGIN;
	}

	public String logoff(PhaseEvent pe){
		return logoff();
	}
	
	public String logoff() {
		Object idUsuario = acessarNaSessao(ID_USUARIO);
		
		if(idUsuario != null){
			retirarDaSessao(ID_USUARIO);
		}
		
		return ConstantesJSF.SUCESSO;
	}

}

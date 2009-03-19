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

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class FacadeEasyAccept {

	public void zerarSistema() {
		// TODO
	}

	public void criarUsuario(String login, String senha) {
		// TODO
	}

	public void editarAtributoUsuario(String login, String atributo, String valor) {
		// TODO
	}

	public String getAtributoUsuario(String login, String atributo) {
		// TODO
		return null;
	}
	
	public Long criarCategoria(String login, String nome){
		//TODO
		return null;
	}
	
	public Long getIdCategoria(String login, String nome){
		//TODO
		return null;
	}
	
	public void editarCategoria(String login, String idCategoria, String atributo, String valor){
		//TODO
	}
	
	public Long criarTransacao(String login, String descricao, String data, String valor, String idCategoria, String comentario, String ndias, String credito){
		//TODO
		return null;
	}
	
	public void getAtributoTransacao(String login, String id){
		//TODO
	}
	
	public void getAtributoTransacao(String login, String id, String atributo){
		//TODO
	}
	
	public void editarAtributoTransacao(String login, String id, String atributo, String valor){
		//TODO
	}
	
	public void removerTransacao(String login, String id){
		//TODO
	}
	
	public void getNumeroDeTransacoes(String login){
		//TODO
	}
	
	public void removerCategoria(String login, String idCategoria){
		//TODO
	}
	
}

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
package com.google.code.mymon3y.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

import com.google.code.mymon3y.util.Hasher;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Entidade que representa um Usuário do sistema.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
@Entity
@Table(name = "tb_usuario")
@NamedQueries( { @NamedQuery(name = "usuario.login", query = "select u from Usuario u where u.login like :login") })
public class Usuario implements Identificavel {

	/**
	 * Versão da classe.
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	/**
	 * Tamanho mínimo da senha do Usuário.
	 */
	@Transient
	public static final int MIN_PASSWORD_LENGTH = 5;

	/**
	 * True caso a senha já tenha sido criptografada. False caso contrário.
	 */
	@Transient
	private boolean criptografada;

	/**
	 * Identificador único da entidade.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Login de acesso ao sistema (e-mail).
	 */
	@NotEmpty(message = "Login inválido.")
	@Column(nullable = false, unique = true)
	@Email(message = "Login inválido.")
	private String login;

	/**
	 * Senha do Usuário.
	 */
	@NotEmpty(message = "A senha deve ter no mínimo 5 caracteres.")
	@Length(min = MIN_PASSWORD_LENGTH, message = "A senha deve ter no mínimo 5 caracteres.")
	@Column(nullable = false)
	private String senha;

	/**
	 * Categorias que o Usuário possui.
	 */
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "usuario")
	private Set<Categoria> categorias;

	/**
	 * Construtor vazio.
	 */
	public Usuario() {
		this(null, null);
	}

	/**
	 * Construtor de Usuário.
	 * 
	 * @param login
	 *            Login de acesso ao sistema (e-mail).
	 * @param senha
	 *            Senha do Usuário.
	 */
	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = senha;
		this.criptografada = false;
		inicializarCategorias();
	}

	/**
	 * Inicialização das Categorias padrões.
	 */
	private void inicializarCategorias() {
		this.categorias = new HashSet<Categoria>();
		this.categorias.add(new Categoria(this, "Outro"));
		this.categorias.add(new Categoria(this, "Automóvel"));
		this.categorias.add(new Categoria(this, "Despesas Bancárias"));
		this.categorias.add(new Categoria(this, "Faturas"));
		this.categorias.add(new Categoria(this, "Retirada de Dinheiro"));
		this.categorias.add(new Categoria(this, "Doações"));
		this.categorias.add(new Categoria(this, "Babá"));
		this.categorias.add(new Categoria(this, "Roupas"));
		this.categorias.add(new Categoria(this, "Cartão de Crédito"));
		this.categorias.add(new Categoria(this, "Depósito"));
		this.categorias.add(new Categoria(this, "Jantar Fora"));
		this.categorias.add(new Categoria(this, "Educação"));
		this.categorias.add(new Categoria(this, "Eletrônicos"));
		this.categorias.add(new Categoria(this, "Entretenimento"));
		this.categorias.add(new Categoria(this, "Família"));
		this.categorias.add(new Categoria(this, "Alimentação"));
		this.categorias.add(new Categoria(this, "Combustível"));
		this.categorias.add(new Categoria(this, "Presentes"));
		this.categorias.add(new Categoria(this, "Mercearia"));
		this.categorias.add(new Categoria(this, "Casa"));
		this.categorias.add(new Categoria(this, "Seguros"));
		this.categorias.add(new Categoria(this, "Despesas do Trabalho"));
		this.categorias.add(new Categoria(this, "Lazer"));
		this.categorias.add(new Categoria(this, "Empréstimos"));
		this.categorias.add(new Categoria(this, "Saúde"));
		this.categorias.add(new Categoria(this, "Pessoal"));
		this.categorias.add(new Categoria(this, "Animal de Estimação"));
		this.categorias.add(new Categoria(this, "Telefone"));
		this.categorias.add(new Categoria(this, "Poupança"));
		this.categorias.add(new Categoria(this, "Impostos"));
		this.categorias.add(new Categoria(this, "Transporte"));
		this.categorias.add(new Categoria(this, "Utilitários"));
		this.categorias.add(new Categoria(this, "Férias"));
	}

	/**
	 * Retorna True caso a senha já tenha sido criptografada, False caso contrário.
	 * 
	 * @return True caso a senha já tenha sido criptografada, False caso contrário.
	 */
	public boolean isCriptografada() {
		return criptografada;
	}

	/**
	 * Atribui um novo estado à senha (criptografada ou não) do Usuário.
	 * 
	 * @param criptografada
	 *            Novo estado.
	 */
	public void setCriptografada(boolean criptografada) {
		this.criptografada = criptografada;
	}

	/**
	 * @see com.google.code.mymon3y.model.Identificavel#getId()
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribui um novo id à Categoria.
	 * 
	 * @param id
	 *            Novo id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retorna o login do Usuário.
	 * 
	 * @return Login do Usuário.
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Atribui um novo login ao Usuário.
	 * 
	 * @param login
	 *            Novo login.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Retorna a senha do Usuário.
	 * 
	 * @return A senha do Usuário.
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * Atribui uma nova senha ao Usuário.
	 * 
	 * @param senha
	 *            Nova senha.
	 */
	public void setSenha(String senha) {
		this.senha = senha;
		this.criptografada = false;
	}

	/**
	 * Retorna as Categorias de Transação que o Usuário possui.
	 * 
	 * @return Categorias que o Usuário possui.
	 */
	public Set<Categoria> getCategorias() {
		return categorias;
	}

	/**
	 * Atribui novas Categorias de Transação ao Usuário.
	 * 
	 * @param categorias
	 *            Novas Categorias de Transação.
	 */
	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	/**
	 * Adiciona uma nova Categoria de Transação ao Usuário.
	 * 
	 * @param categoria
	 *            Nova Categoria de Transação.
	 */
	public void addCategorias(Categoria categoria) {
		this.categorias.add(categoria);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		return true;
	}

	/**
	 * Criptografa a senha do Usuário.
	 */
	public void criptografarSenha() {
		if(!isCriptografada()){
			this.senha = Hasher.getSha256(this.senha);
			this.setCriptografada(true);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id).append("login", login)
				.append("senha", senha).append("categorias", categorias).toString();
	}

}

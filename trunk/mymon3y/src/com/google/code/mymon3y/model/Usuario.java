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

import com.google.code.mymon3y.util.PasswordHasher;

@Entity
@Table(name = "tb_usuario")
@NamedQueries( { @NamedQuery(name = "usuario.login", query = "select u from Usuario u where u.login like :login") })
/*
 * *
 * 
 * @author Jaindson Valentim Santana
 * 
 * @author Matheus Gaudencio do Rêgo
 */
public class Usuario implements Identificavel {

	@Transient
	/*
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public static final int MIN_PASSWORD_LENGTH = 5;

	@Transient
	private boolean criptografada;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Login inválido.")
	@Column(nullable = false, unique = true)
	@Email(message = "Login inválido.")
	private String login;

	@NotEmpty(message = "A senha deve ter no mínimo 5 caracteres.")
	@Length(min = MIN_PASSWORD_LENGTH, message = "A senha deve ter no mínimo 5 caracteres.")
	@Column(nullable = false)
	private String senha;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "usuario")
	private Set<Categoria> categorias;

	public Usuario() {
		super();
		this.categorias = new HashSet<Categoria>();
	}

	public Usuario(String login, String senha) {
		this();
		this.login = login;
		this.senha = senha;
		this.criptografada = false;
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

	public boolean isCriptografada() {
		return criptografada;
	}

	public void setCriptografada(boolean criptografada) {
		this.criptografada = criptografada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		this.criptografada = false;
	}

	public Set<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(Set<Categoria> categorias) {
		this.categorias = categorias;
	}

	/**
	 * @param categoria
	 */
	public void addCategorias(Categoria categoria) {
		this.categorias.add(categoria);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

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
	 * 
	 */
	public void criptografarSenha() {
		this.senha = PasswordHasher.getSha256(this.senha);
	}

}

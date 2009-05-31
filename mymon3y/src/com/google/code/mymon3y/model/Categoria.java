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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Entidade que representa uma Categoria. As transações podem ser classificadas numa Categoria. Uma Categoria pode ter
 * várias Transações associadas com ela.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
@Entity
@Table(name = "tb_categoria", uniqueConstraints = { @UniqueConstraint(columnNames = { "nome", "fk_usuario" }) })
@NamedQueries( { @NamedQuery(name = "categoria.nomeCategoriaLoginDoUsuario", query = "select c from Categoria c join c.usuario u where lower(c.nome) = lower(:nome) and u.login like :loginDoUsuario"),
			     @NamedQuery(name = "categorias.nomeCategoriaLoginDoUsuario", query = "select c from Categoria c join c.usuario u where lower(c.nome) like lower(:nome) and u.login like :loginDoUsuario") })
public class Categoria implements Identificavel {

	/**
	 * Versão da classe.
	 */
	@Transient
	private static final long serialVersionUID = 1L;

	/**
	 * Identificador único da entidade.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Nome da Categoria.
	 */
	@NotNull(message = "Nome inválido.")
	@NotEmpty(message = "Nome inválido.")
	@Column(nullable = false, unique = true)
	private String nome;

	/**
	 * Usuário dono da Categoria.
	 */
	@ManyToOne
	@ForeignKey(name = "fk_usuario_constraint")
	@JoinColumn(name = "fk_usuario", nullable = false)
	private Usuario usuario;

	/**
	 * Transações associadas com esta Categoria.
	 */
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER, mappedBy = "categoria")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
	private Set<Transacao> transacoes;

	/**
	 * Construtor vazio.
	 */
	public Categoria() {
		super();
		this.transacoes = new HashSet<Transacao>();
	}

	/**
	 * Construtor que recebe o usuário dono da Categoria e o nome da Categoria.
	 * 
	 * @param usuario
	 *            Usuário dono da Categoria.
	 * @param nome
	 *            Nome da Categoria.
	 */
	public Categoria(Usuario usuario, String nome) {
		this();
		this.usuario = usuario;
		this.nome = nome;
	}

	/**
	 * Construtor que recebe o nome da Categoria.
	 * 
	 * @param nome
	 *            Nome da Categoria.
	 */
	public Categoria(String nome) {
		this(null, nome);
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
	 * Retorna o nome da Categoria.
	 * 
	 * @return Nome da Categoria.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Atribui um novo nome à Categoria.
	 * 
	 * @param nome
	 *            Novo nome.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Retorna o Usuário dono da Categoria.
	 * 
	 * @return Usuário dono da Categoria.
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Atribui um novo Usuário como o dono da Categoria.
	 * 
	 * @param usuario
	 *            Usuário dono da Categoria.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Retorna as Transações associadas com a Categoria.
	 * 
	 * @return Transações associadas com a Categoria.
	 */
	public Set<Transacao> getTransacoes() {
		return transacoes;
	}

	/**
	 * Atribui um novo conjunto de Transações como sendo o associado a esta Categoria.
	 * 
	 * @param transacoes
	 *            Transações associadas a esta Categoria.
	 */
	public void setTransacoes(Set<Transacao> transacoes) {
		this.transacoes = transacoes;
	}

	/**
	 * Adiciona uma nova Transação à Categoria.
	 * 
	 * @param transacao
	 *            Transação a ser adicionada.
	 */
	public void addTransacao(Transacao transacao) {
		this.transacoes.add(transacao);
	}

	/**
	 * Remove a associação da Transação com a Categoria.
	 * 
	 * @param transacao
	 *            Transação a ser removida.
	 */
	public void removerTransacao(Transacao transacao) {
		this.transacoes.remove(transacao);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Categoria other = (Categoria) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("id", id).append("nome", nome).append(
				"usuario", usuario).append("transacoes", transacoes).toString();
	}

}

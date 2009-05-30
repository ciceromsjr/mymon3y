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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

/**
 * Entidade que representa uma Transação. Uma Transação representa qualquer movimento de dinheiro realizado pelo
 * Usuário.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
@Entity
@Table(name = "tb_transacao")
@NamedQueries( {
		@NamedQuery(name = "transacao.transacoes", query = "from Transacao t where t.categoria.usuario.login like :loginDoUsuario and :dataInicio <= t.data and t.data <= :dataFim"),
		@NamedQuery(name = "transacao.loginDoUsuario", query = "select count(*) from Transacao t where t.categoria.usuario.login like :loginDoUsuario"),
		@NamedQuery(name = "transacao.porUsuarioEData", query = "select count(*) from Transacao t where t.categoria.usuario.id = :idDoUsuario and t.dataAvisoPrevio = :data") })
public class Transacao implements Identificavel {

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
	 * Pequena descrição da Transação.
	 */
	@NotNull(message = "Descrição Inválida.")
	@NotEmpty(message = "Descrição Inválida.")
	@Column(nullable = false)
	private String descricao;

	/**
	 * Data que a Transação foi/será realizada.
	 */
	@Column(nullable = false)
	private Date data;

	/**
	 * Valor monetário da Transação em centavos (menor unidade da moeda).
	 */
	@Column(nullable = false)
	private Integer valor;

	/**
	 * Algum comentário a respeito da Transação. Utilizada quando a {@link #descricao} não for descritiva o suficiente.
	 */
	@Column
	private String comentario;

	/**
	 * Data de quando o aviso a respeito da Transação deve ser enviado ao Usuário dono da Transação.
	 */
	@Column
	private Date dataAvisoPrevio;

	/**
	 * Identifica se a Transação é de crédito (true) ou débito (false).
	 */
	@Column(nullable = false)
	private Boolean credito;

	/**
	 * Categoria que esta Transação pertence.
	 */
	@ManyToOne
	@ForeignKey(name = "fk_categoria_constraint")
	@JoinColumn(name = "fk_categoria", nullable = false)
	private Categoria categoria;

	/**
	 * Construtor vazio.
	 */
	public Transacao() {
		super();
	}

	/**
	 * Construtor da Transação.
	 * 
	 * @param descricao
	 *            Pequena descrição da Transação.
	 * @param data
	 *            Data que a Transação foi/será realizada.
	 * @param valor
	 *            Valor monetário da Transação em centavos (menor unidade da moeda).
	 * @param comentario
	 *            Algum comentário a respeito da Transação.
	 * @param dataAvisoPrevio
	 *            Data de quando o aviso a respeito da Transação deve ser enviado ao Usuário dono da Transação.
	 * @param credito
	 *            Crédito (true) ou Débito (false).
	 */
	public Transacao(String descricao, Date data, Integer valor, String comentario, Date dataAvisoPrevio,
			Boolean credito) {
		super();
		setDescricao(descricao);
		this.data = data;
		this.valor = valor;
		this.comentario = comentario;
		this.dataAvisoPrevio = dataAvisoPrevio;
		this.credito = credito;
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
	 * Retorna a descrição associada com esta Transação.
	 * 
	 * @return A descrição associada com esta Transação.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atribui uma nova descrição à Transação.
	 * 
	 * @param descricao
	 *            Nova descrição.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	/**
	 * Retorna a data que a Transação foi/será realizada.
	 * 
	 * @return a data que a Transação foi/será realizada.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * Atribui uma nova data à Transação.
	 * 
	 * @param data
	 *            Nova data.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * Retorna o valor monetário da Transação em centavos (menor unidade da moeda).
	 * 
	 * @return O valor monetário da Transação em centavos (menor unidade da moeda).
	 */
	public Integer getValor() {
		return valor;
	}

	/**
	 * Atribui um novo valor monetário à Transação.
	 * 
	 * @param valor
	 *            Novo valor.
	 */
	public void setValor(Integer valor) {
		this.valor = valor;
	}

	/**
	 * Retorna o comentário a respeito da Transação.
	 * 
	 * @return comentário a respeito da Transação.
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * Atribui um novo comentário à Transação.
	 * 
	 * @param comentario
	 *            Novo comentário.
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	/**
	 * Retorna a data de quando o aviso a respeito da Transação deve ser enviado ao Usuário dono da Transação.
	 * 
	 * @return A data de quando o aviso a respeito da Transação deve ser enviado ao Usuário dono da Transação.
	 */
	public Date getDataAvisoPrevio() {
		return dataAvisoPrevio;
	}

	/**
	 * Atribui uma nova data de aviso prévio à Transação.
	 * 
	 * @param dataAvisoPrevio
	 *            Nova data.
	 */
	public void setDataAvisoPrevio(Date dataAvisoPrevio) {
		this.dataAvisoPrevio = dataAvisoPrevio;
	}

	/**
	 * Retorna True caso a Transação seja de crédito e False caso seja de débito.
	 * 
	 * @return True caso a Transação seja de crédito e False caso seja de débito.
	 */
	public Boolean getCredito() {
		return credito;
	}

	/**
	 * Atribui um novo tipo de movimento (crédito ou débito) à Transação.
	 * 
	 * @param credito
	 *            Novo tipo de movimento (crédito ou débito).
	 */
	public void setCredito(Boolean credito) {
		this.credito = credito;
	}

	/**
	 * Retorna a Categoria associada com a Transação.
	 * 
	 * @return Categoria associada com a Transação.
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * Atribui uma nova Categoria à Transação.
	 * 
	 * @param categoria
	 *            Nova Categoria.
	 */
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

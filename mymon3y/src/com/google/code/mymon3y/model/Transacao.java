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

@Entity
@Table(name = "tb_transacao")
@NamedQueries( { @NamedQuery(name = "transacao.transacoes", query = "from Transacao t where t.categoria.usuario.login like :loginDoUsuario and :dataInicio <= t.data and t.data <= :dataFim"),
			     @NamedQuery(name = "transacao.loginDoUsuario", query = "select count(*) from Transacao t join t.categoria c join c.usuario u where u.login like :loginDoUsuario"),
				 @NamedQuery(name = "transacao.porUsuarioEData", query = "select count(*) from Transacao t join t.categoria c join c.usuario u where u.id = :idDoUsuario and t.dataAvisoPrevio = :data")})
/*
 * *
 * 
 * @author Jaindson Valentim Santana
 * 
 * @author Matheus Gaudencio do Rêgo
 */
public class Transacao implements Identificavel {

	@Transient
	/*
	 * *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Descrição Inválida.")
	@NotEmpty(message = "Descrição Inválida.")
	@Column(nullable = false)
	private String descricao;

	@Column(nullable = false)
	private Date data;

	@Column(nullable = false)
	private Integer valor;

	@Column
	private String comentario;

	@Column
	private Date dataAvisoPrevio;

	@Column(nullable = false)
	private Boolean credito;

	@ManyToOne
	@ForeignKey(name = "fk_categoria_constraint")
	@JoinColumn(name = "fk_categoria", nullable = false)
	private Categoria categoria;

	public Transacao() {
		super();
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao.trim();
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getValor() {
		return valor;
	}

	public void setValor(Integer valor) {
		this.valor = valor;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Date getDataAvisoPrevio() {
		return dataAvisoPrevio;
	}

	public void setDataAvisoPrevio(Date dataAvisoPrevio) {
		this.dataAvisoPrevio = dataAvisoPrevio;
	}

	public Boolean getCredito() {
		return credito;
	}

	public void setCredito(Boolean credito) {
		this.credito = credito;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Transacao other = (Transacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

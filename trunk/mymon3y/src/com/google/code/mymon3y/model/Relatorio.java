package com.google.code.mymon3y.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Relatorio {

	private Date inicio;
	private Date fim;
	private List<Transacao> transacoes;
	private Usuario usuario;

	public Relatorio(Usuario usuario, Date inicio, Date fim, List transacoes) {
		this.inicio = inicio;
		this.fim = fim;
		this.usuario = usuario;
		this.transacoes = new LinkedList<Transacao>(transacoes);
	}
	
	public Date getInicio() {
		return this.inicio;
	}
	
	public Date getFim() {
		return this.fim;
	}
	
	public Usuario getUsuario() {
		return this.usuario;
	}
	public List<Transacao> getTransacoes() {
		return new LinkedList<Transacao>(this.transacoes);
	}

	public int getNumeroDeTransacoesDebito() {
		int i = 0;
		for (Transacao t : transacoes) {
			if (!t.getCredito()) {
				i++;
			}
		}
		return i;
	}

	public int getNumeroDeTransacoesCredito() {
		int i = 0;
		for (Transacao t : transacoes) {
			if (t.getCredito()) {
				i++;
			}
		}
		return i;
	}
	
}
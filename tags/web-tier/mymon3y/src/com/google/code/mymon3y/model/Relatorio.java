package com.google.code.mymon3y.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Entidade que representa um Relatório. O relatório possui todas as informações necessárias para serem mostradas ao
 * usuário do sistema.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class Relatorio {

	/**
	 * Data inicial. Todas as Transações deste Relatório possuem sua data maior ou igual a data inicial.
	 */
	private Date inicio;

	/**
	 * Data final. Todas as Transações deste Relatório possuem sua data menor ou igual a data final.
	 */
	private Date fim;

	/**
	 * Transações que fazem parte deste Relatório.
	 */
	private List<Transacao> transacoes;

	/**
	 * Usuário dono do Relatório e das Transações associadas com ele.
	 */
	private Usuario usuario;

	/**
	 * Construtor do Relatório.
	 * 
	 * @param usuario
	 *            Usuário dono do Relatório.
	 * @param inicio
	 *            Data inicial a ser considerada.
	 * @param fim
	 *            Data final a ser considerada.
	 * @param transacoes
	 *            Transações associadas ao Usuário e que possuem a data entre a data inicial e a final.
	 */
	public Relatorio(Usuario usuario, Date inicio, Date fim, List<Transacao> transacoes) {
		this.inicio = inicio;
		this.fim = fim;
		this.usuario = usuario;
		this.transacoes = new LinkedList<Transacao>(transacoes);
	}

	/**
	 * Retorna a data inicial do Relatório.
	 * 
	 * @return data inicial do Relatório.
	 */
	public Date getInicio() {
		return this.inicio;
	}

	/**
	 * Retorna a data final do Relatório.
	 * 
	 * @return data final do Relatório.
	 */
	public Date getFim() {
		return this.fim;
	}

	/**
	 * Retorna o Usuário dono do Relatório.
	 * 
	 * @return Usuário dono do Relatório.
	 */
	public Usuario getUsuario() {
		return this.usuario;
	}

	/**
	 * Retorna as Transações associadas com este Relatório.
	 * 
	 * @return Transações associadas com este Relatório.
	 */
	public List<Transacao> getTransacoes() {
		return new LinkedList<Transacao>(this.transacoes);
	}

	/**
	 * Retorna as Transações em formato CSV.
	 * 
	 * @return Transações em formato CSV.
	 */
	public List<String> getTransacoesCSV() {
		List<String> result = new LinkedList<String>();
		// "05/18/2009","1619-5","Compra com Cart<E3>o - 16/05 20:48 YOKAN COMIDA JAPO","","174895","-48.95",
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (Transacao t : transacoes) {
			String tmp = "\"";
			tmp += sdf.format(t.getData());
			tmp += "\":\"";
			tmp += t.getCategoria().getNome();
			tmp += "\":\"";
			tmp += t.getDescricao();
			tmp += "\":\"";
			tmp += t.getComentario();
			tmp += "\":\"";
			int valor = t.getValor();
			if (!t.getCredito()) {
				tmp += '-';
			}
			tmp += (valor / 100);
			tmp += '.';
			valor = (valor % 100);
			if (valor < 10) {
				tmp += '0';
			}
			tmp += valor;
			tmp += "\"";
			result.add(tmp);
		}
		return result;
	}
	
	/**
	 * Retorna o número de Transações do tipo débito.
	 * 
	 * @return número de Transações do tipo débito.
	 */
	public int getNumeroDeTransacoesDebito() {
		int i = 0;
		for (Transacao t : transacoes) {
			if (!t.getCredito()) {
				i++;
			}
		}
		return i;
	}

	/**
	 * Retorna o número de Transações do tipo crédito.
	 * 
	 * @return número de Transações do tipo crédito.
	 */
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
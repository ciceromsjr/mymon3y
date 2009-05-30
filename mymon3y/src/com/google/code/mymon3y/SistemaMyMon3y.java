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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;
import org.xml.sax.SAXException;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Identificavel;
import com.google.code.mymon3y.model.Relatorio;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;
import com.google.code.mymon3y.persistencia.GerenciadorDePersistencia;
import com.google.code.mymon3y.persistencia.InvalidPropertiesException;
import com.google.code.mymon3y.util.OFXImport;
import com.google.code.mymon3y.util.Hasher;

/**
 * Fachada principal do sistema.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class SistemaMyMon3y {

	/**
	 * Lida com a persistência das entidades.
	 */
	private GerenciadorDePersistencia gdp;

	/**
	 * Calendário usado na manipulação de datas.
	 */
	private Calendar calendar;

	/**
	 * Construtor vazio.
	 */
	public SistemaMyMon3y() {
		this.gdp = new GerenciadorDePersistencia();
		this.calendar = Calendar.getInstance();
	}

	/**
	 * @see GerenciadorDePersistencia#zerarSistema()
	 */
	public void zerarSistema() throws MyMon3yException {
		this.gdp.zerarSistema();
	}

	/**
	 * Cria um novo Usuário no sistema.
	 * 
	 * @param login
	 *            Login do Usuário.
	 * @param senha
	 *            Senha do Usuário.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public void criarUsuario(String login, String senha) throws MyMon3yException {

		Usuario usuario = new Usuario(login, senha);
		criarUsuario(usuario);
	}
	
	/**
	 * Cria um novo Usuário no sistema.
	 * 
	 * @param usuario Novo usuário.
	 * @throws MyMon3yException Caso algum erro ocorra.
	 */
	public void criarUsuario(Usuario usuario) throws MyMon3yException {
		Usuario usuarioExistente = this.gdp.getUsuarioByLogin(usuario.getLogin());
		if (usuarioExistente != null) {
			throw new MyMon3yException("Login não disponível.");
		}

		validar(usuario);
		this.gdp.makePersistent(usuario);
	}

	/**
	 * @see GerenciadorDePersistencia#getUsuarioByLogin(String)
	 */
	public Usuario getUsuario(String login) throws MyMon3yException {
		Usuario usuario = this.gdp.getUsuarioByLogin(login);
		if (usuario == null) {
			throw new MyMon3yException("Login inexistente.");
		}
		return usuario;
	}

	/**
	 * @see GerenciadorDePersistencia#atualizar(Usuario)
	 */
	public void atualizar(Usuario usuario) throws MyMon3yException {
		validar(usuario);
		this.gdp.atualizar(usuario);
	}

	/**
	 * Faz a validação dos atributos do Usuário.
	 * 
	 * @param usuario
	 *            Usuário a ser validado.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	private void validar(Usuario usuario) throws MyMon3yException {
		validar((Identificavel) usuario);
		if (!usuario.isCriptografada()) {
			usuario.criptografarSenha();
		}
	}

	/**
	 * Realiza a validação de uma entidade.
	 * 
	 * @param <T>
	 *            Tipo que estende {@link Identificavel}.
	 * @param obj
	 *            Objeto a ser validado.
	 * @throws MyMon3yException
	 *             Caso algum erro de validação ocorra.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Identificavel> void validar(T obj) throws MyMon3yException {
		ClassValidator<T> cv = new ClassValidator<T>((Class<T>) obj.getClass());
		InvalidValue[] invalidValues = cv.getInvalidValues(obj);
		if (invalidValues.length > 0) {
			throw new InvalidPropertiesException(invalidValues);
		}
	}

	/**
	 * @see #getCategoriaByNomeELoginDoUsuario(String, String)
	 */
	public Categoria getCategoria(String login, String nome) throws MyMon3yException {
		validarLogin(login);
		Categoria categoria = getCategoriaByNomeELoginDoUsuario(login, nome);
		if (categoria == null) {
			throw new MyMon3yException("Categoria inexistente.");
		}
		return categoria;
	}

	/**
	 * Faz a validação do login (verifica se existe).
	 * 
	 * @param login
	 *            Login a ser verificado.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	private void validarLogin(String login) throws MyMon3yException {
		getUsuario(login);
	}

	/**
	 * @see GerenciadorDePersistencia#getCategoriaByNomeELoginDoUsuario(String, String)
	 */
	private Categoria getCategoriaByNomeELoginDoUsuario(String login, String nome) throws MyMon3yException {
		return this.gdp.getCategoriaByNomeELoginDoUsuario(login, nome);
	}

	/**
	 * Retorna a Categoria associada com o login e cujo id seja igual ao passado como parâmetro.
	 * 
	 * @param login
	 *            Login do Usuário dono da Categoria.
	 * @param idCategoria
	 *            Identificador único da Categoria.
	 * @return Categoria associada com o login e cujo id seja igual ao passado como parâmetro.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public Categoria getCategoria(String login, Long idCategoria) throws MyMon3yException {
		validarLogin(login);
		Categoria categoria = this.gdp.getCategoriaById(idCategoria);
		if (categoria == null) {
			throw new MyMon3yException("Categoria inexistente.");
		}
		return categoria;
	}

	/**
	 * Adiciona uma nova Categoria.
	 * 
	 * @param login
	 *            Login do Usuário dono da Categoria.
	 * @param categoria
	 *            Categoria a ser adicionada.
	 * @return Identificador único da Categoria adicionada.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public Long adicionarCategoria(String login, Categoria categoria) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		validar(categoria);

		Categoria categoriaExistente = getCategoriaByNomeELoginDoUsuario(login, categoria.getNome());

		if (categoriaExistente != null) {
			throw new MyMon3yException("Esta Categoria já existe.");
		}

		usuario.addCategorias(categoria);
		categoria.setUsuario(usuario);

		this.gdp.atualizar(usuario);

		return categoria.getId();
	}

	/**
	 * @see GerenciadorDePersistencia#atualizar(Categoria)
	 */
	public void atualizar(Categoria categoria) throws MyMon3yException {
		validar(categoria);
		this.gdp.atualizar(categoria);
	}

	/**
	 * @see GerenciadorDePersistencia#atualizar(Transacao)
	 */
	public void atualizar(Transacao transacao) throws MyMon3yException {
		validar(transacao);
		this.gdp.atualizar(transacao);
	}

	/**
	 * Adiciona uma nova Transação.
	 * 
	 * @param login
	 *            Login do Usuário dono da Transação.
	 * @param idCategoria
	 *            Identificador único da Categoria que a Transação pertence.
	 * @param transacao
	 *            Transação a ser adicionada.
	 * @return Identificador único da Transação adicionada.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public Long adicionarTransacao(String login, Long idCategoria, Transacao transacao) throws MyMon3yException {
		Categoria categoria = getCategoria(login, idCategoria);

		validar(transacao);

		transacao.setData(getDataNormalizada(transacao.getData()));
		transacao.setDataAvisoPrevio(getDataNormalizada(transacao.getDataAvisoPrevio()));

		categoria.addTransacao(transacao);
		transacao.setCategoria(categoria);

		this.gdp.atualizar(categoria);

		return transacao.getId();
	}

	/**
	 * Elimina as informações de hora do dia, minutos, segundos e milisegundos de uma data.
	 * 
	 * @param data
	 *            Data a ser normalizada.
	 * @return Uma nova data normalizada.
	 */
	private Date getDataNormalizada(Date data) {

		if (data == null) {
			return null;
		}

		this.calendar.setTime(data);
		this.calendar.set(Calendar.HOUR_OF_DAY, 0);
		this.calendar.set(Calendar.MINUTE, 0);
		this.calendar.set(Calendar.SECOND, 0);
		this.calendar.set(Calendar.MILLISECOND, 0);

		return this.calendar.getTime();
	}

	/**
	 * @see GerenciadorDePersistencia#getTransacaoById(Long)
	 */
	public Transacao getTransacao(String login, Long idTransacao) throws MyMon3yException {
		validarLogin(login);
		Transacao transacao = this.gdp.getTransacaoById(idTransacao);
		if (transacao == null) {
			throw new MyMon3yException("Transação Inexistente.");
		}
		return transacao;
	}

	/**
	 * @see GerenciadorDePersistencia#removerTransacao(Transacao)
	 */
	public void removerTransacao(String login, Long idTransacao) throws MyMon3yException {
		Transacao transacao = getTransacao(login, idTransacao);
		if (transacao == null) {
			throw new MyMon3yException("Transação Inexistente.");
		}
		this.gdp.removerTransacao(transacao);
	}

	/**
	 * @see GerenciadorDePersistencia#getNumeroDeTransacoes(String)
	 */
	public Long getNumeroDeTransacoes(String login) throws MyMon3yException {
		validarLogin(login);
		return this.gdp.getNumeroDeTransacoes(login);
	}

	/**
	 * Edita uma Transação modificando sua Categoria.
	 * 
	 * @param login
	 *            Login do Usuário dono da Transação.
	 * @param idTransacao
	 *            Id da Transação sendo editada.
	 * @param idCategoria
	 *            Id da nova Categoria.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public void editarCategoriaDaTransacao(String login, Long idTransacao, Long idCategoria) throws MyMon3yException {
		validarLogin(login);

		Transacao transacao = getTransacao(login, idTransacao);
		Categoria categoria = getCategoria(login, idCategoria);

		Categoria antigaCategoria = transacao.getCategoria();
		antigaCategoria.removerTransacao(transacao);

		transacao.setCategoria(categoria);
		categoria.addTransacao(transacao);

		atualizar(antigaCategoria);
		atualizar(categoria);
	}

	/**
	 * @see GerenciadorDePersistencia#removerCategoria(Categoria)
	 */
	public void removerCategoria(String login, Long idCategoria) throws MyMon3yException {
		Categoria categoria = getCategoria(login, idCategoria);
		if (categoria.getTransacoes().size() > 0) {
			throw new MyMon3yException("Esta Categoria possui transações associadas.");
		}
		this.gdp.removerCategoria(categoria);
	}

	/**
	 * @see GerenciadorDePersistencia#removerUsuario(Usuario)
	 */
	public void removerUsuario(String login, String senha) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		if(!validoLoginESenha(usuario, senha)){
			throw new MyMon3yException("Login/Senha errada.");
		}
		this.gdp.removerUsuario(usuario);
	}

	public boolean validoLoginESenha(Usuario usuario, String senha) {
		if(usuario == null || senha == null){
			return false;
		}
		String senhaVerdadeira = usuario.getSenha();
		String senhaInformada = Hasher.getSha256(senha);
		return senhaVerdadeira.equals(senhaInformada);
	}
	
	/**
	 * @see GerenciadorDePersistencia#getNotificacoes(Long, Date)
	 */
	public Long getNotificacoes(String login, Date data) throws MyMon3yException {
		Usuario usuario = getUsuario(login);
		return this.gdp.getNotificacoes(usuario.getId(), getDataNormalizada(data));
	}

	/**
	 * Retorna um Relatório com as Transações que atende a busca.
	 * 
	 * @param login
	 *            Login do Usuário dono das Transações.
	 * @param inicio
	 *            Data inicial da busca.
	 * @param fim
	 *            Data final da busca.
	 * @return Relatório com as Transações que atende a busca.
	 * @throws MyMon3yException
	 *             Caso algum erro ocorra.
	 */
	public Relatorio criarRelatorio(String login, Date inicio, Date fim) throws MyMon3yException {
		validarLogin(login);
		List<Transacao> transacoes = this.gdp.getTransacoesByLogin(login, inicio, fim);
		Relatorio r = new Relatorio(this.getUsuario(login), inicio, fim, transacoes);
		return r;
	}

	/**
	 * Adiciona as Transações importando-as de um arquivo OFX exportado pelo Banco do Brasil.
	 * 
	 * @param login
	 *            Login do Usuário.
	 * @param arquivo
	 *            Arquivo de entrada.
	 * @see OFXImport#readOFX(String)
	 */
	public void importarOFX(String login, String arquivo) throws MyMon3yException, SAXException, IOException,
			ParserConfigurationException {
		List<Transacao> list = OFXImport.readOFX(arquivo);
		for (Transacao t : list) {
			adicionarTransacao(login, getCategoriaByNomeELoginDoUsuario(login, "Outro").getId(), t);
		}
	}

}

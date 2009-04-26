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
package com.google.code.mymon3y.persistencia.dao.hibernate;

import org.hibernate.Query;

import com.google.code.mymon3y.model.Usuario;
import com.google.code.mymon3y.persistencia.PersistenciaMyMon3yException;
import com.google.code.mymon3y.persistencia.dao.Comando;
import com.google.code.mymon3y.persistencia.dao.UsuarioDAO;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class UsuarioDAOHibernate extends AbstractGenericHibernateDAO<Usuario, Long> implements UsuarioDAO {
	
	protected void fazerAntesDoLoadSessaoFechada(Usuario usuario) throws PersistenciaMyMon3yException {
		if(usuario != null){
			usuario.setCriptografada(true);
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.code.mymon3y.persistencia.dao.UsuarioDAO#findByLogin(java.lang.String)
	 */
	@Override
	public Usuario findByLogin(final String login) throws PersistenciaMyMon3yException {
		Usuario result = null;

		result = (Usuario) executarOperacao(new Comando() {

			public Object executar() {

				Query q = getSession().getNamedQuery("usuario.login");
				q.setString("login", login);

				return q.list().size() > 0 ? q.list().get(0) : null;
			}

		});
		fazerAntesDoLoadSessaoFechada(result);
		return result;
	}

}
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
package com.google.code.mymon3y.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * Entidade capaz de realizar hash de strings.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class Hasher {

	/**
	 * {@link MessageDigest} responsável por fazer os hashes das strings.
	 */
	private static MessageDigest md;

	static {
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Construtor vazio.
	 */
	private Hasher() {

	}

	/**
	 * Retorna o hash SHA 256 da string.
	 * 
	 * @param string
	 *            String a ser avaliada.
	 * @return O hash SHA 256 da string.
	 */
	public static String getSha256(String string) {

		if (string == null || string.equals("")) {
			return string;
		}

		String senhaEncriptada = null;

		byte[] hash = md.digest(string.getBytes());
		senhaEncriptada = new String(Base64.encodeBase64(hash));

		return senhaEncriptada;
	}

}

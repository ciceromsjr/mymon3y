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
package com.google.code.mymon3y.jsf.facelets;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class MethodValueExpression extends ValueExpression implements Externalizable {

	private ValueExpression orig;

	private MethodExpression methodExpression;

	public MethodValueExpression() {
	}

	MethodValueExpression(ValueExpression orig, MethodExpression methodExpression) {
		this.orig = orig;
		this.methodExpression = methodExpression;
	}

	@Override
	public Class getExpectedType() {
		return orig.getExpectedType();
	}

	@Override
	public Class getType(ELContext ctx) {
		return MethodExpression.class;
	}

	@Override
	public Object getValue(ELContext ctx) {
		return methodExpression;
	}

	@Override
	public boolean isReadOnly(ELContext ctx) {
		return orig.isReadOnly(ctx);
	}

	@Override
	public void setValue(ELContext ctx, Object val) {
	}

	@Override
	public boolean equals(Object val) {
		return orig.equals(val);
	}

	@Override
	public String getExpressionString() {
		return orig.getExpressionString();
	}

	@Override
	public int hashCode() {
		return orig.hashCode();
	}

	@Override
	public boolean isLiteralText() {
		return orig.isLiteralText();
	}

	/**
	 * *
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		orig = (ValueExpression) in.readObject();
		methodExpression = (MethodExpression) in.readObject();
	}

	/**
	 * *
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(orig);
		out.writeObject(methodExpression);
	}
}

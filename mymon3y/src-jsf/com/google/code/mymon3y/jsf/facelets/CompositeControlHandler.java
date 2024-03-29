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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

import com.sun.el.util.ReflectionUtil;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletException;
import com.sun.facelets.FaceletHandler;
import com.sun.facelets.el.VariableMapperWrapper;
import com.sun.facelets.tag.Tag;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.ui.ComponentRef;
import com.sun.facelets.tag.ui.ComponentRefHandler;


/**
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 *
 */
public class CompositeControlHandler extends TagHandler {
	private final static Pattern METHOD_PATTERN = Pattern.compile("(\\w+)\\s*=\\s*(.+?)\\s*;\\s*");

	private final TagAttribute rendererType;

	private final TagAttribute componentType;

	private final TagAttribute methodBindings;

	private ComponentHandler componentHandler;

	/**
	 * *
	 * 
	 * @param config
	 */
	public CompositeControlHandler(TagConfig config) {
		
		
		super(config);
		
		rendererType = getAttribute("rendererType");
		componentType = getAttribute("componentType");
		methodBindings = getAttribute("methodBindings");
		componentHandler = new ComponentRefHandler(new ComponentConfig() {
			/**
			 * *
			 * 
			 * @see com.sun.facelets.tag.TagConfig#getNextHandler()
			 */
			public FaceletHandler getNextHandler() {
				return CompositeControlHandler.this.nextHandler;
			}

			public Tag getTag() {
				return CompositeControlHandler.this.tag;
			}

			public String getTagId() {
				return CompositeControlHandler.this.tagId;
			}

			/**
			 * *
			 * 
			 * @see com.sun.facelets.tag.jsf.ComponentConfig#getComponentType()
			 */
			public String getComponentType() {
				return (componentType == null) ? ComponentRef.COMPONENT_TYPE : componentType.getValue();
			}

			/**
			 * *
			 * 
			 * @see com.sun.facelets.tag.jsf.ComponentConfig#getRendererType()
			 */
			public String getRendererType() {
				return (rendererType == null) ? null : rendererType.getValue();
			}
		});
	}

	/**
	 * *
	 * 
	 * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
	 *      javax.faces.component.UIComponent)
	 */
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException, FacesException, FaceletException,
			ELException {
		VariableMapper origVarMap = ctx.getVariableMapper();
		try {
			VariableMapperWrapper variableMap = new VariableMapperWrapper(origVarMap);
			ctx.setVariableMapper(variableMap);
			if (methodBindings != null) {
				String value = (String) methodBindings.getValue(ctx);
				Matcher match = METHOD_PATTERN.matcher(value);
				while (match.find()) {
					String var = match.group(1);
					ValueExpression currentExpression = origVarMap.resolveVariable(var);
					if (currentExpression != null) {
						try {
							FunctionMethodData methodData = new FunctionMethodData(var, match.group(2).split("\\s+"));
							MethodExpression mexpr = buildMethodExpression(ctx,
									currentExpression.getExpressionString(), methodData);
							variableMap.setVariable(var, new MethodValueExpression(currentExpression, mexpr));
						} catch (Exception ex) {
							throw new FacesException(ex);
						}
					}
				}
			}
			componentHandler.apply(ctx, parent);
		} finally {
			ctx.setVariableMapper(origVarMap);
		}
	}

	private MethodExpression buildMethodExpression(FaceletContext ctx, String expression, FunctionMethodData methodData)
			throws NoSuchMethodException, ClassNotFoundException {
		return ctx.getExpressionFactory().createMethodExpression(ctx, expression, methodData.getReturnType(),
				methodData.getArguments());
	}

	private class FunctionMethodData {
		private String variable;

		private Class returnType;

		private Class[] arguments;

		FunctionMethodData(String variable, String[] types) throws ClassNotFoundException {
			this.variable = variable;
			if ("null".equals(types[0]) || "void".equals(types[0]))
				returnType = null;
			else
				returnType = ReflectionUtil.forName(types[0]);
			arguments = new Class[types.length - 1];
			for (int i = 0; i < arguments.length; i++)
				arguments[i] = ReflectionUtil.forName(types[i + 1]);
		}

		public Class[] getArguments() {
			return this.arguments;
		}

		public void setArguments(Class[] arguments) {
			this.arguments = arguments;
		}

		public Class getReturnType() {
			return this.returnType;
		}

		public void setReturnType(Class returnType) {
			this.returnType = returnType;
		}

		public String getVariable() {
			return this.variable;
		}

		public void setVariable(String variable) {
			this.variable = variable;
		}
	}
}

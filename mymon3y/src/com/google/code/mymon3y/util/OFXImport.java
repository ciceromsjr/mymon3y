package com.google.code.mymon3y.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.code.mymon3y.model.Transacao;

/**
 * Responsável por fazer a importação de Transações a partir de um arquivo no formato OFX exportado pelo Banco do
 * Brasil.
 * 
 * @author Jaindson Valentim Santana
 * @author Matheus Gaudencio do Rêgo
 * 
 */
public class OFXImport {

	/**
	 * <code>
		    <STMTTRN>
		       <TRNTYPE>OTHER</TRNTYPE>
		       <DTPOSTED>20090302120000[-3:BRT]</DTPOSTED>
		       <TRNAMT>-150.00</TRNAMT>
		       <FITID>200903021150000</FITID>
		       <CHECKNUM>642056848245</CHECKNUM>
		       <REFNUM>281.642.056.848.245</REFNUM>
		       <MEMO>Saque no TAA - 28/02 16:42 PAO ACUCAR EPITACIO</MEMO>
		    </STMTTRN>
    	</code>
	 */

	/**
	 * Parser de arquivos no formato OFX exportado pelo do Banco do Brasil.
	 * 
	 * @author Jaindson Valentim Santana
	 * @author Matheus Gaudencio do Rêgo
	 * 
	 */
	static class OFXParser {

		/**
		 * OFX resultante do Parser.
		 */
		private OFX ofx;

		/**
		 * Construtor de {@link OFXParser}.
		 * 
		 * @param ofx
		 *            Objeto {@link OFX} a ser preenchido.
		 */
		public OFXParser(OFX ofx) {
			this.ofx = ofx;
		}

		/**
		 * Lê uma tag.
		 * 
		 * @param fis
		 *            {@link InputStream} com a entrada.
		 * @return Uma tag.
		 * @throws IOException
		 *             Caso algum erro de IO ocorra.
		 */
		private String readTag(InputStream fis) throws IOException {
			StringBuilder sb = new StringBuilder();
			int a;
			while ((a = fis.read()) != -1 && ((char) a) != '>') {
				sb.append((char) a);
			}
			return sb.toString();
		}

		/**
		 * Faz a leitura do conteúdo de uma tag.
		 * 
		 * @param fis
		 *            {@link InputStream} com a entrada.
		 * @return Conteúdo da tag.
		 * @throws IOException
		 *             Caso algum erro de IO ocorra.
		 */
		private String readContent(InputStream fis) throws IOException {
			StringBuilder sb = new StringBuilder();
			int a;
			while ((a = fis.read()) != -1 && ((char) a) != '<') {
				sb.append((char) a);
			}
			return sb.toString();
		}

		/**
		 * Realiza o <i>parsing</i> de um arquivo.
		 * 
		 * @param fis
		 *            InputStream de leitura.
		 * @throws IOException
		 *             Caso algum erro de IO ocorra.
		 */
		public void parse(InputStream fis) throws IOException {
			int a;
			while ((a = fis.read()) != -1) {
				if (((char) a) == '<') {
					String tag = readTag(fis);
					if (tag.equals("STMTTRN")) {
						ofx.readSTMTTRN();
					} else if (tag.equals("/STMTTRN")) {
						ofx.endReadSTMTTRN();
					} else if (tag.equals("DTPOSTED")) {
						ofx.readDTPOSTED(readContent(fis));
					} else if (tag.equals("TRNAMT")) {
						ofx.readTRNAMT(readContent(fis));
					} else if (tag.equals("MEMO")) {
						ofx.readMEMO(readContent(fis));
					}
				}
			}
		}
	}

	/**
	 * Representa o formato OFX exportado pelo Banco do Brasil.
	 * 
	 * @author Jaindson Valentim Santana
	 * @author Matheus Gaudencio do Rêgo
	 * 
	 */
	static class OFX {

		boolean STMTTRN = false;

		Date data = null;

		int amnt = Integer.MAX_VALUE;

		String memo = null;

		private List<Transacao> transacoes;

		public OFX() {
			transacoes = new LinkedList<Transacao>();
		}

		public void readSTMTTRN() {
			STMTTRN = true;
		}

		public void endReadSTMTTRN() {
			if (data == null || amnt == Integer.MAX_VALUE || memo == null) {
				// transacao incompleta, ignore
			} else {
				Transacao t = new Transacao(memo, data, amnt, "", null, amnt > 0);
				transacoes.add(t);
			}
			data = null;
			amnt = Integer.MAX_VALUE;
			memo = null;
			STMTTRN = false;
		}

		public void readDTPOSTED(String value) {
			if (STMTTRN) {
				data = new GregorianCalendar(Integer.parseInt(value.substring(0, 4)), Integer.parseInt(value.substring(
						4, 6)), Integer.parseInt(value.substring(6, 8)), Integer.parseInt(value.substring(8, 10)),
						Integer.parseInt(value.substring(10, 12)), Integer.parseInt(value.substring(12, 14))).getTime();
			}
		}

		public void readTRNAMT(String value) {
			if (STMTTRN) {
				amnt = Integer.parseInt(value.replaceAll("\\.", ""));
			}
		}

		public void readMEMO(String value) {
			if (STMTTRN) {
				memo = value;
			}
		}

	}

	/**
	 * Retorna as Transações extraídas do arquivo no formato OFX exportado pelo Banco do Brasil.
	 * 
	 * @param arquivo
	 *            Arquivo com a entrada.
	 * @return As Transações extraídas do arquivo no formato OFX exportado pelo Banco do Brasil.
	 * @throws SAXException
	 *             Caso algum erro ocorra durante a leitura do OFX.
	 * @throws IOException
	 *             Caso algum erro de IO ocorra.
	 * @throws ParserConfigurationException
	 *             Caso algum erro ocorra durante o <i>parsing</i>.
	 */
	public static List<Transacao> readOFX(String arquivo) throws SAXException, IOException,
			ParserConfigurationException {
		OFX ofx = new OFX();
		OFXParser ofxP = new OFXParser(ofx);
		FileInputStream fis = new FileInputStream(arquivo);
		ofxP.parse(fis);
		return ofx.transacoes;
	}

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		System.out.println(readOFX("resources/extrato.ofx"));
	}

	public static List<Transacao> readConteudoOFX(String conteudo) throws IOException {
		OFX ofx = new OFX();
		OFXParser ofxP = new OFXParser(ofx);
		StringBufferInputStream sbif = new StringBufferInputStream(conteudo);
		ofxP.parse(sbif);
		return ofx.transacoes;
	}

}
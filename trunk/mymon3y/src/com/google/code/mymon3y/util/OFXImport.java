package com.google.code.mymon3y.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.google.code.mymon3y.model.Transacao;

public class OFXImport {


	/*
    <STMTTRN>
       <TRNTYPE>OTHER</TRNTYPE>
       <DTPOSTED>20090302120000[-3:BRT]</DTPOSTED>
       <TRNAMT>-150.00</TRNAMT>
       <FITID>200903021150000</FITID>
       <CHECKNUM>642056848245</CHECKNUM>
       <REFNUM>281.642.056.848.245</REFNUM>
       <MEMO>Saque no TAA - 28/02 16:42 PAO ACUCAR EPITACIO</MEMO>
    </STMTTRN>
	*/
	
	static class OFXParser {
		private OFX ofx;

		public OFXParser(OFX ofx) {
			this.ofx = ofx;
		}
		
		private String readTag(FileInputStream fis) throws IOException {
			StringBuilder sb = new StringBuilder();
			int a;
			while ((a = fis.read()) != -1 && ((char) a) != '>') {
				sb.append((char) a);
			}
			return sb.toString();
		}
		
		private String readContent(FileInputStream fis) throws IOException {
			StringBuilder sb = new StringBuilder();
			int a;
			while ((a = fis.read()) != -1 && ((char) a) != '<') {
				sb.append((char) a);
			}
			return sb.toString();
		}
		
		public void parse(File f) throws IOException {
			FileInputStream fis = new FileInputStream(f);
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
				Transacao t = new Transacao(memo, data, amnt, "", null, amnt > 0 ? true : false);
				transacoes.add(t);				
			}
			data = null;
			amnt = Integer.MAX_VALUE;
			memo = null;
			STMTTRN = false;
		}
		
		public void readDTPOSTED(String value) {
			if (STMTTRN) {
				data = new GregorianCalendar(Integer.parseInt(value.substring(0, 4)),
						Integer.parseInt(value.substring(4, 6)),
						Integer.parseInt(value.substring(6, 8)),
						Integer.parseInt(value.substring(8, 10)),
						Integer.parseInt(value.substring(10, 12)),
						Integer.parseInt(value.substring(12, 14))).getTime();
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
	
	public static List<Transacao> readOFX(String arquivo) throws SAXException, IOException, ParserConfigurationException {
		OFX ofx = new OFX();
		OFXParser ofxP = new OFXParser(ofx);
		ofxP.parse(new File(arquivo));
		return ofx.transacoes;
	}
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		System.out.println(readOFX("resources/extrato.ofx"));
	}
	
}
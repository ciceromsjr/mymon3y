package com.google.code.mymon3y;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.masukomi.aspirin.core.MailQue;
import org.masukomi.aspirin.core.SimpleMimeMessageGenerator;

import com.google.code.mymon3y.model.Categoria;
import com.google.code.mymon3y.model.Transacao;
import com.google.code.mymon3y.model.Usuario;

public class MailDaemon {

	private static MailDaemon instance;
	
	public synchronized static MailDaemon getInstance(SistemaMyMon3y sistema) {
		if (instance == null) {
			instance = new MailDaemon(sistema);
			instance.start();
		}
		return instance;
	}
	
	public class MailThread implements Runnable {

		private SistemaMyMon3y sistema;
		private MailDaemon mailDaemon;

		public MailThread(SistemaMyMon3y sistema, MailDaemon mailDaemon) {
			this.sistema = sistema;
			this.mailDaemon = mailDaemon;
		}
		
		@Override
		public void run() {
			Date hoje = Calendar.getInstance().getTime();
			try {
				List<Transacao> notificacoes = this.sistema.getNotificacoes(hoje);
				for (Transacao t : notificacoes) {
					this.mailDaemon.enviarNotificacao(t);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	private SistemaMyMon3y sistema;
	private ScheduledExecutorService schedulerExecutor;

	private MailDaemon(SistemaMyMon3y sistema) {
		this.sistema = sistema;
		this.schedulerExecutor = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void enviarNotificacao(Transacao t) throws AddressException, MessagingException {
		Usuario u = t.getCategoria().getUsuario();

		MailQue mailQue = new MailQue();
		
		MimeMessage message = SimpleMimeMessageGenerator.getNewMimeMessage();
		
		message.setFrom(new InternetAddress("mymoney@example.net"));
		message.addRecipient(Message.RecipientType.TO, 
		  new InternetAddress(u.getLogin()));
		message.setSubject("MyMon3y - " + t.getDescricao());
		message.setText("Aviso de transação!\n" + t.toString());

		mailQue.queMail(message);		
	}

	public void start() {
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE) + (hour * 60);
		this.schedulerExecutor.scheduleWithFixedDelay(new MailThread(sistema, this), ((24*60) - minute) + 5, (24*60), TimeUnit.MINUTES);
	}
	
	public void stop() {
		this.schedulerExecutor.shutdownNow();
	}

	public static void main(String[] args) throws Exception {
		SistemaMyMon3y sistema = new SistemaMyMon3y();
		MailDaemon md = new MailDaemon(sistema);
		Usuario u = new Usuario("jaindson@lsd.ufcg.edu.br", "senha");
		Transacao t = new Transacao("Teste", Calendar.getInstance().getTime(), 10, "teste", Calendar.getInstance().getTime(), true);
		Categoria c = new Categoria(u, "teste");
		t.setCategoria(c);
		md.enviarNotificacao(t);
	}
	
}

package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

@Entity
@Table(name="mensagem")
public class Mensagem extends GenericModel{

	@Id
	@GeneratedValue
	public Integer men_codigo;
	//1 mensagem para usuario - 2 solicitação de amizade - 3 solicitação de co-autoria
	public int men_tipo;
	public String men_assunto;
	public String men_texto;
	public String men_emissor_nome;
	public String men_receptor_nome;
	public int usu_receptor_codigo;
	public int usu_emissor_codigo;
	public int edi_emissor_codigo;
	public int edi_receptor_codigo;
	public String men_data;
	
	public Mensagem() {
		super();
		
	}
	
	public Mensagem(int men_tipo, String men_assunto, String men_texto,
			String men_receptor_nome, int usu_receptor_codigo) {
		super();
		this.men_tipo = men_tipo;
		this.men_assunto = men_assunto;
		this.men_texto = men_texto;
		this.men_receptor_nome = men_receptor_nome;
		this.usu_receptor_codigo = usu_receptor_codigo;
	}
	
	
}

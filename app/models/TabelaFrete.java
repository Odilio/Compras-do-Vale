package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="tabelafrete")
public class TabelaFrete extends GenericModel{

	@Id
	@GeneratedValue
	public int tab_codigo;
	public String tab_nome;
	public String tab_valor;
	public String tab_CEP;
	public int tab_dias;
	public Date tab_data_inicio;
	public Date tab_data_fim;	
	
}

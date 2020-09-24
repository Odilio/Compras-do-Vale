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
@Table(name="tabelapreco")
public class TabelaPreco extends GenericModel{

	@Id
	@GeneratedValue
	public int tab_codigo;
	public String tab_nome;
	public Date tab_data_inicio;
	public Date tab_data_fim;
	
	
}

package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="grupoproduto")
public class GrupoProduto extends GenericModel{

	@Id
	@GeneratedValue
	public int gru_codigo;
	public String gru_nome;
	public String gru_ncm;
	
	
}

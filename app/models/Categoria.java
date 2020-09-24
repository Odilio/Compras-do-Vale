package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="categoria")
public class Categoria extends GenericModel{

	@Id
	@GeneratedValue
	public int cat_codigo;
	public String cat_nome;
	
	
	
}

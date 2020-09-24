package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="marca")
public class Marca extends GenericModel{

	@Id
	@GeneratedValue
	public int mar_codigo;
	public String mar_nome;
	
	
	
}

package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.i18n.Lang;
import play.libs.Crypto;
import play.libs.Images;
import play.libs.Mail;
import play.libs.OAuth2;
import play.modules.paginate.ValuePaginator;

import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import models.*;
public class Buscar extends Application {
	
		
		 public static void index(String categoria, int grupo) {
			 List<Produto> produtos = new ArrayList<Produto>();
		
			 Produto produto = new Produto();
			 	
				 List<GrupoProduto> listaGrupos = GrupoProduto.all().fetch();
				 List<Marca> listaMarcas = Marca.all().fetch();
			 
			 	String busca =  params.get("SearchText", String.class);
			   
			    List<Produto> todos = Cache.get("produtos", List.class);
			    if(todos == null) {
			    	todos = Produto.find("order by pro_prioridade asc").fetch();
			        Cache.set("produtos", todos);
			    }
			    
			    if (busca != null && busca !=""){
			    for (Produto produto2 : todos) {
					if ( produto2.pro_titulo.toLowerCase().contains(busca.toLowerCase())){
						produtos.add(produto2);
					}
				}}
			      
			    if (categoria != null && categoria !="" && grupo == 0){
			    	   for (Produto produto2 : todos) {
							if ( produto2.pro_categoria.toLowerCase().contains(categoria.toLowerCase())){
								produtos.add(produto2);
							}
						}}
			    if (categoria != null && categoria !="" && grupo != 0){
			    	 for (Produto produto2 : todos) {
							if ( produto2.pro_categoria.toLowerCase().contains(categoria.toLowerCase()) && produto2.pro_cod_grupo_produto == grupo){
								produtos.add(produto2);
							}
						}
				 }
			    
			    if ((params.get("preco", String.class)) != "" && (params.get("preco", String.class)) != null ){
					 String[] preco =  (params.get("preco", String.class)).split(",");
						
					 produtos = Produto.find("pro_preco > ?1 and pro_preco < ?2" , Double.parseDouble(preco[0]) , Double.parseDouble(preco[1])).fetch();
					 for (Produto produto2 : todos) {
							if ( produto2.pro_preco > Double.parseDouble(preco[0] ) && produto2.pro_preco < Double.parseDouble(preco[1] )){
								produtos.add(produto2);
							}
						}
				 }
			   
			    ValuePaginator listaProdutos = new ValuePaginator(produtos);
				listaProdutos.setPageSize(8);
			 render(listaProdutos,produto,listaGrupos,listaMarcas);
		    }

}
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import Romanizador.Junidecode;
import models.Cart;
import models.Categoria;
import models.Endereco;
import models.Frete;
import models.Parceiro;
import models.Pedido;
import models.Produto;
import models.RotaTransporte;
import models.Usuario;
import notifiers.Notifier;
import play.Play;
import play.cache.Cache;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.i18n.Lang;
import play.i18n.Messages;
import play.libs.Crypto;
import play.libs.Images;
import play.modules.paginate.ValuePaginator;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import util.calculoFreteCorreio;

public class Entregas extends Application {
	static SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");	
	
	public static void index() {
		List<RotaTransporte> rotas = RotaTransporte.find("byRot_executacaoAndRot_concluido", false,false).fetch();
		

		render(rotas);
	}
	
		public static void transportes() {
		

		render();
	}
		
		public static void minhasRotas() {
			List<RotaTransporte> rotas = RotaTransporte.all().fetch();
			
			render(rotas);
		}
		
		 public static void mostrarPedido(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();	
			 pedido.ped_status = Messages.get(pedido.ped_status);
			 List<Cart> cart = Cart.find("byPed_codigo", pedido.ped_codigo).fetch();
			 Endereco endereco = Endereco.find("byUsu_codigo", pedido.usu_codigo).first();
				render(pedido,cart,endereco);
			}
		
		public static void admin() {
			List<RotaTransporte> rotas = RotaTransporte.all().fetch();
			
			render(rotas);
		}

		public static void salvarRota(RotaTransporte rota) {
			rota.save();

			transportes();
		}
		
		public static void candidatar() {

			render();
		}
		
	  public static void login(String username, String password) {
	    	Crypto a = new Crypto();
	        String senha = a.encryptAES(password);
	    	Usuario user = Usuario.find("usu_nome = ? and usu_senha = ? ", username, senha).first();
	        if(user != null) {
	        	Cache.set("contacontador", Pedido.count("byPed_statusAndUsu_codigo", "status1",user.usu_codigo), "300mn");
	        	 session.put("user", user.usu_nome);
	        	 if(user.usu_role == 3)
	        		 Admin.admin();
	        	 if(user.usu_role == 2)
	        		 Parceiros.admin();
	        	 if(user.usu_role == 5)
	        		 Entregas.admin();
	           Perfil.index();         
	        
	        }
	        // Oops
	        flash.put("username", username);
	        flash.error("Login e/ou senha incorretos");
	        logar("");
	    }
	   
	   
	 
	  @Before
	    static void addUser() {
		  List<Categoria> categorias = Categoria.all().fetch();
		  renderArgs.put("categorias", categorias);
		  Usuario user = connected();
	        if(user != null) {
	            renderArgs.put("user", user);
	        }
	    }
	    
	    static Usuario connected() {
	        if(renderArgs.get("user") != null) {
	            return renderArgs.get("user", Usuario.class);
	        }
	        String username = session.get("user");
	        if(username != null) {
	            return Usuario.find("byUsu_nome", username).first();
	        } 
	        return null;
	    }
	

	
	public static void logar(String erro) {

		render();
	}
	public static void logout() {
		session.clear();
		index();
	}

	public static void registrar() {

		logar("");
	}

	public static void registrar(String erro) {

		logar(erro);
	}

	public static void saveUser(Usuario user, String verifyPassword) {
		/*
		 * validation.required(verifyPassword).message("Campo Obrigatorio!");
		 * validation.equals(verifyPassword, user.usu_senha).message(
		 * "Seu password não confere");
		 * 
		 * if (validation.hasErrors()) { render("@registrar", user,
		 * verifyPassword); }
		 */
		validation.required(user.usu_senha).message("Campo Obrigatorio!");
		if (Usuario.find("byUsu_nome", user.usu_nome).first() == null) {
			Crypto a = new Crypto();
			String senha = a.encryptAES(user.usu_senha);
			user.usu_senha = senha;
			Date data = new Date();
			user.usu_data_cadastro = formatador.format(data);
			user.usu_role = 5;
			user.save();
		
			session.put("user", user.usu_nome);
			 try {
					Notifier.welcomeMandrill(user.usu_email, user.usu_nome_completo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Perfil.index();
		} else {
			logar("login já esxiste!");
		}
	}

}
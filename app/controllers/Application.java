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
import models.Frete;
import models.Parceiro;
import models.Pedido;
import models.Produto;
import models.ProdutoLoja;
import models.Usuario;
import notifiers.Notifier;
import play.Play;
import play.cache.Cache;
import play.cache.CacheFor;
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
import play.vfs.VirtualFile;
import util.calculoFreteCorreio;
@With(Compress.class)
public class Application extends Controller {
	static SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	
	public static void converterCoreano() {
	
	  	
        render();
	}
	
	public static void inserirLetra() {
		File outputFile = Play.getFile("/public/templateemail/the filme - she.html");
    	
    	String texto = "texto minimo";
    	try {
			texto = FileUtils.readFileToString( outputFile, "Utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Frete core = new Frete();
		core.fre_valor = texto;
    	renderJSON(core);
	}
	
	public static void inserirLetraJung() {
		File outputFile = Play.getFile("/public/templateemail/Jung Joon Young.html");
    	
    	String texto = "texto minimo";
    	try {
			texto = FileUtils.readFileToString( outputFile, "Utf8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Frete core = new Frete();
		core.fre_valor = texto;
    	renderJSON(core);
	}

	public static void conversao(String coreano) {
    	
		Junidecode conver = new Junidecode();
		Frete core = new Frete();
		core.fre_valor  = conver.unidecode(coreano);
        renderJSON(core);
	}
	
	// @CacheFor("1min")
	public static void index() {
		long conta = 0;
		long carrinho = 0;
		if(renderArgs.get("user") != null) {
			if (Cache.get("cartcontador_" +renderArgs.get("user", Usuario.class).usu_codigo , Long.class) != null)
				carrinho = (long) Cache.get("cartcontador_" + renderArgs.get("user", Usuario.class).usu_codigo, Integer.class); 
		}
	
		List<Produto> maisVendidos= Produto.find("pro_codigo in (89,170,71,132)" ).fetch();
	      	
		List<Produto> produtos = Cache.get("produtos", List.class);
	    if(produtos == null) {
	    	produtos = Produto.find("order by pro_prioridade asc").fetch();
	        Cache.set("produtos", produtos);
	    }
	    
	    for (Produto produto : produtos) {
	    	produto.pro_categoria = Messages.get(produto.pro_categoria);
		}
		
	String lang = Lang.get();
	Lang.change(lang);
	ValuePaginator listaProdutos = new ValuePaginator(produtos);
	listaProdutos.setPageSize(8);
				
		 if (params.get("categoria", String.class) != "" && params.get("categoria", String.class) != null){
			 produtos = Produto.find("byPro_categoria", params.get("categoria", String.class)).fetch();
			 listaProdutos = new ValuePaginator(produtos);
			 listaProdutos.setPageSize(12);
			 render(listaProdutos,maisVendidos,conta,carrinho);
		 }
		 
			

		render(listaProdutos,maisVendidos,conta,carrinho);
	}
	
	public static void index2() {
		long conta = 0;
		long carrinho = 0;
		if(renderArgs.get("user") != null) {
			if (Cache.get("cartcontador_" +renderArgs.get("user", Usuario.class).usu_codigo , Long.class) != null)
				carrinho = (long) Cache.get("cartcontador_" + renderArgs.get("user", Usuario.class).usu_codigo, Integer.class); 
			
		}
	
		List<Produto> maisVendidos= Produto.find("pro_codigo in (157,170,71,132)" ).fetch();
	      	
		List<Produto> produtos = Cache.get("produtos", List.class);
	    if(produtos == null) {
	    	produtos = Produto.find("order by pro_prioridade asc").fetch();
	        Cache.set("produtos", produtos);
	    }
		
	String lang = Lang.get();
	Lang.change(lang);
	ValuePaginator listaProdutos = new ValuePaginator(produtos);
	listaProdutos.setPageSize(8);
				
		 if (params.get("categoria", String.class) != "" && params.get("categoria", String.class) != null){
			 produtos = Produto.find("byPro_categoria", params.get("categoria", String.class)).fetch();
			 listaProdutos = new ValuePaginator(produtos);
			 listaProdutos.setPageSize(12);
			 render(listaProdutos,maisVendidos,conta,carrinho);
		 }
		 
			

		render(listaProdutos,maisVendidos,conta,carrinho);
	}
	
	
	public static void contact() {
		

		render();
	}

	public static void transportes() {
		

		render();
	}

	public static void manutencao() {
		

		render();
	}

	public static void playerCorean() {
		

		render();
	}

	  public static void login(String username, String password) {
	    	Crypto a = new Crypto();
	        String senha = a.encryptAES(password);
	    	Usuario user = Usuario.find("usu_nome = ?1 and usu_senha = ?2 ", username, senha).first();
	        if(user != null) {
	        	 session.put("user", user.usu_nome);
	        	 if(user.usu_role == 3)
	        		 Admin.admin();
	        	 if(user.usu_role == 2)
	        		 Parceiros.admin();
	        	 
	        	 if ((request.cookies.get("parceiro") != null) && !(request.cookies.get("parceiro").value == "0")  )
		    		productDetails(Integer.parseInt(request.cookies.get("produto").value),Integer.parseInt(request.cookies.get("parceiro").value)); 
	        	 if ((request.cookies.get("produto") != null) &&  !request.cookies.get("produto").value.equalsIgnoreCase("0")  )
	        		 productDetails(Integer.parseInt(request.cookies.get("produto").value),0);
	           Perfil.index();         
	        
	        }
	        // Oops
	        flash.put("username", username);
	        flash.error("Login e/ou senha incorretos");
	        logar("");
	    }
	   
	   
	 
	  @Before
	    static void addUser() {
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

	public static void tipoSpeakrs() {

		render();
	}

	public static void productDetails(Integer codigo, int parceiro ) {
		Produto produto = Cache.get("produto_" + codigo, Produto.class);
	    if(produto == null) {
	    	produto = Produto.find("byPro_codigo",
				codigo).first();
	    	Cache.set("produto_" + codigo, produto);
	    }
	    if (parceiro!=0)
	    	response.setCookie("parceiro", parceiro+"", "1d");
	    if (codigo!=0)
		    response.setCookie("produto", codigo+"", "1d");
	    List<Produto> maisVendidos= Produto.find("pro_codigo in (157,170,71,167)" ).fetch();
	    render(produto,maisVendidos,parceiro);
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
			user.usu_role = 1;
			user.save();

			session.put("user", user.usu_nome);
			 try {
					Notifier.welcomeMandrill(user.usu_email, user.usu_nome_completo);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if ((request.cookies.get("parceiro") != null) && !(request.cookies.get("parceiro").value == "0")  )
		    		productDetails(Integer.parseInt(request.cookies.get("produto").value),Integer.parseInt(request.cookies.get("parceiro").value)); 
	        	 if ((request.cookies.get("produto") != null) &&  !request.cookies.get("produto").value.equalsIgnoreCase("0")  )
        		 productDetails(Integer.parseInt(request.cookies.get("produto").value),0);
			Perfil.index();
		} else {
			logar("login já esxiste!");
		}
	}


	public static void mudarIdioma(String id) {
		
			Lang.change(id);
		
	}

	public static void mudar() {
		System.out.println("ajax");
	
	}
	
	
	public static void parceiro(Integer parceiro, Integer marca) {
		
		System.out.println("path " + (Play.applicationPath.toURI()+"").replace("file:/", "file:///"));
		long conta = 0;
		long carrinho = 0;
		if (renderArgs.get("user") != null) {
			if (Cache.get("contacontador", Long.class) != null)
				conta = (long) Cache.get("contacontador", Long.class);
			if (Cache.get("cartcontador_" + renderArgs.get("user", Usuario.class).usu_codigo, Long.class) != null)
				carrinho = (long) Cache.get("cartcontador_" + renderArgs.get("user", Usuario.class).usu_codigo,
						Integer.class);

		}
		String nomeParceiro = "";
		String frase = "";
		String corFundo = "";
		String caminhoLogo = "";
		List<ProdutoLoja> produtos = new ArrayList<ProdutoLoja>();
		
		if (parceiro != null){
		/*	produtos = Cache.get("produtos_parceiro_" + parceiro , List.class);
		if (produtos == null) {*/
			produtos = ProdutoLoja.find("pro_cod_produtor = ?1 order by pro_prioridade asc", parceiro).fetch();
		/*	Cache.set("produtos_parceiro_" + parceiro, produtos);*/
			//nomeParceiro = produtos.get(0).pro_nome_produtor;
			Parceiro par = Parceiro.find("byUsu_codigo", parceiro).first();
			if (par != null) {
				caminhoLogo = par.par_caminho_ima;
				frase = par.par_frase;
				corFundo = par.par_cor_background;
				nomeParceiro = par.par_nome_completo;
			}
		/*}*/}
		
		if (marca != null){
		/*	produtos = Cache.get("produtos_marca_" + marca, List.class);
		if (produtos == null) {
			produtos = Produto.find("pro_cod_marca = ? order by pro_prioridade asc", marca).fetch();
			/*Cache.set("produtos_marca_" + marca, produtos);
			nomeParceiro = produtos.get(0).pro_marca;}*/
		}

		ValuePaginator listaProdutos = new ValuePaginator(produtos);
		listaProdutos.setPageSize(16);
		
		render(listaProdutos, conta, carrinho, nomeParceiro,caminhoLogo,frase,corFundo);
	}

	public static void calcularFrete(String rev_texto) {
		System.out.println("imprimindo o frete" + rev_texto);
		calculoFreteCorreio frete =  new calculoFreteCorreio();
		System.out.println("printando "+frete.calcularFrete(rev_texto,1,"40010"));
	
	Frete fre_valor = new Frete();
	fre_valor.fre_valor = frete.calcularFrete(rev_texto,1,"40010");
	fre_valor.fre_valor_PAC = frete.calcularFrete(rev_texto,1,"41106");
	renderJSON(fre_valor);
		
	}
	
}
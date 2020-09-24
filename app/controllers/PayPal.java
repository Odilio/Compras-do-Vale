package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import models.Pagamento;
import models.PaypalTransaction;
import models.Pedido;
import play.Logger;

public class PayPal extends Application  {

	
	public static void validation() throws Exception {

		Logger.info("validation");

		// creation de l'url a envoyé à paypal pour vérification
		// on recupere les parametpag de la requetes POST
		String str = "cmd=_notify-validate&" + params.get("body");
		Logger.info(str);

		// création d'une connection à la sandbox paypal
		URL url = new URL("https://www.paypal.com/cgi-bin/webscr");
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		// envoi de la requête
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		out.println(str);
		out.close();

		// lecture de la réponse
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String result = in.readLine();
		in.close();

		// assign posted variables to local variables
		String itemName = params.get("item_name");
		String itemNumber = params.get("item_number");
		String paymentStatus = params.get("payment_status");
		String paymentAmount = params.get("mc_gross");
		String paymentCurrency = params.get("mc_currency");
		String txnId = params.get("txn_id");
		String receiverEmail = params.get("receiver_email");
		String payerEmail = params.get("payer_email");
		String nome = params.get("nome");
		String lingua = params.get("lingua");
		Pagamento pag = new Pagamento();
		pag.pag_telefone = params.get("telefone");
		pag.pag_nome =  params.get("item_name");
		pag.pag_valor =params.get("amount");
		pag.save();
		// check notification validation
		if ("VERIFIED".equals(result)) {
			if ("Completed".equals(paymentStatus)) {
				// on vérife que la txn_id n'a pas été traité précédemment
				PaypalTransaction paypalTransaction = PaypalTransaction
						.findByTrxId(txnId);
				// si aucune transaction en base ou si transaction mais en
				// status invalide
				// on traite la demande
				if (paypalTransaction == null
						|| (paypalTransaction != null && PaypalTransaction.TrxStatusEnum.INVALID
								.equals(paypalTransaction.status))) {
					// on vérifie que receiver_email est votre adresse email
					// a remplacer par l'adresse mail du vendeur
					if ("odilio@tobesunglasses.com.br".equals(receiverEmail)) {
						// vérifier que paymentAmount (EUR) et paymentCurrency
						// (prix du produit vendu) sont corrects
						Logger.info("Transaction OK");
						// sauvegarde la trace de la transaction paypal en base
						new PaypalTransaction(itemName, itemNumber,
								paymentStatus, paymentAmount, paymentCurrency,
								txnId, receiverEmail, payerEmail,
								PaypalTransaction.TrxStatusEnum.VALID).save();
					} else {
						// Mauvaise adresse email paypal
						Logger.info("Mauvaise adresse email paypal");
					}
				} else {
					// ID de transaction déjà utilisé
					Logger.info("La transaction a déjà été traité");
				}
			} else {
				// Statut de paiement: Echec
				Logger.info("Statut de paiement: Echec");
			}
		} else if ("INVALID".equals(result)) {
			Logger.info("Invalide transaction");
			new PaypalTransaction(itemName, itemNumber, paymentStatus,
					paymentAmount, paymentCurrency, txnId, receiverEmail,
					payerEmail, PaypalTransaction.TrxStatusEnum.INVALID).save();
		} else {
			Logger.info("Erreur lors du traitement");
		}
	}

	/** Success payment */
	public static void success(int codigo) {
		Pedido pedido = Pedido.find("byPed_codigo", codigo).first();
		pedido.ped_status = "status2";
		pedido.save();
		render(pedido);
	}

	/** home page */
	public static void buy() {
		Logger.info("buy");
		render();
	}

	/** fail payment */
	public static void fail() {
		Logger.info("fail");
		render();
	}
}

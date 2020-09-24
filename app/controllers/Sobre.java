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
public class Sobre extends Application {
		
		
		 public static void sobre() {
		    
			 render();
		    }

		 public static void mapa() {
			    
			 render();
		    }
	
}
package br.com.laersondev.goldenraspberryawardsapi.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(//
		title = "Golden Raspberry Awards Application", //
		version = "1.0.0", //
		contact = @Contact(//
				name = "Laerson Vieira", //
				email = "laercio.vsantos@gmail.com", //
				url = "https://github.com/laercioVieira"//
				)), //
servers = {@Server(url = "/", description = "Application Base URL")//
})
@ApplicationPath("/api")
public class RestApplication extends Application {

}

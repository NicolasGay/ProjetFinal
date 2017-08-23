package io.robusta.auth.api;


import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("users")
public class UserResource {


	@Path("test")
	@GET
	public String test(){
		return "tst";
	}
	

}

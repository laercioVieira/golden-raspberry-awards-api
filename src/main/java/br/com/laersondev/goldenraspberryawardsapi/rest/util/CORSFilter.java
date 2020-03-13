package br.com.laersondev.goldenraspberryawardsapi.rest.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

	private static final List<String> LOCAL_HOST = Arrays.asList("localhost", "127.0.0.1");

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if( LOCAL_HOST.contains(requestContext.getUriInfo().getRequestUri().getHost())) {
			responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
			responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
			responseContext.getHeaders().add("Access-Control-Max-Age", "-1");
			responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		}
	}

}

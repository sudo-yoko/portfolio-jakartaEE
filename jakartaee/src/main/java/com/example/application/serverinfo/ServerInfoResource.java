package com.example.application.serverinfo;

import java.util.logging.Logger;

import com.example.application.MediaTypes;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("server-info")
public class ServerInfoResource {
    private static final Logger logger = Logger.getLogger(ServerInfoResource.class.getName());
    private static final String LOG_PREFIX = ">>> [" + ServerInfoResource.class.getSimpleName() + "]: ";

    @Context
    private UriInfo uriInfo;
    @Context
    private HttpServletRequest request;
    @Inject
    private ServerInfoInteractor interactor;

    @Path("/time")
    @GET
    @Produces(MediaTypes.APPLICATION_JSON_UTF_8)
    public Response getTime() {
        logger.info(LOG_PREFIX + String.format("Request(inbound) -> %s %s", request.getMethod(), uriInfo.getPath()));

        ServerInfoResponse response = interactor.getNow();
        return Response.ok(response).build();
    }
}

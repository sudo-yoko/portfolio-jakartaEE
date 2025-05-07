package com.example.application.users;

import java.net.URI;
import java.util.logging.Logger;

import com.example.application.ExtractingJsonSerializer;
import com.example.application.MediaTypes;
import com.example.domain.valueobjects.UserId;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/users")
public class UsersResource {
    private static final Logger logger = Logger.getLogger(UsersResource.class.getName());
    private static final String LOG_PREFIX = ">>> " + UsersResource.class.getSimpleName() + ": ";

    @Inject
    private UsersInteractor interactor;

    @Path("/{userId}")
    @GET
    @Produces(MediaTypes.APPLICATION_JSON_UTF_8)
    public Response getUser(@PathParam("userId") String userId, @QueryParam("properties") String properties) {
        logger.info(LOG_PREFIX + String.format("Request(inbound) GET -> userId=%s, properties=%s", userId, properties));

        String error = UserId.Validator.validate(userId);
        if (error != null) {
            throw new BadRequestException(error);
        }
        UsersResponse response = interactor.getUser(userId);

        JsonObject filteredResponse = ExtractingJsonSerializer.properties(properties).apply(response);
        return Response.ok(filteredResponse).build();
    }

    @Path("/{userId}")
    @POST
    @Consumes(MediaTypes.APPLICATION_JSON_UTF_8)
    public Response postUser(@PathParam("userId") String userId, UsersRequest body, @Context UriInfo uriInfo) {
        logger.info(LOG_PREFIX + String.format("Request(inbound) POST -> userId=%s, body=%s", userId, body.toString()));

        String error = UserId.Validator.validate(userId);
        if (error != null) {
            throw new BadRequestException(error);
        }
        if (!userId.equals(body.getUserId())) {
            throw new BadRequestException("userIdが不一致です。");
        }
        interactor.postUser(body);

        URI newItemUri = uriInfo.getAbsolutePath();
        return Response.created(newItemUri).build();
    }

    @Path("/{userId}")
    @PUT
    @Consumes(MediaTypes.APPLICATION_JSON_UTF_8)
    public Response putUser(@PathParam("userId") String userId, UsersRequest body) {
        logger.info(LOG_PREFIX + String.format("Request(inbound) PUT -> userId=%s, body=%s", userId, body.toString()));

        String error = UserId.Validator.validate(userId);
        if (error != null) {
            throw new BadRequestException(error);
        }
        if (!userId.equals(body.getUserId())) {
            throw new BadRequestException("userIdが不一致です。");
        }
        interactor.putUser(body);

        return Response.ok().build();
    }

    @Path("/{userId}")
    @DELETE
    public Response deleteUser(@PathParam("userId") String userId) {
        logger.info(LOG_PREFIX + String.format("Request(inbound) DELETE -> userId=%s", userId));

        String error = UserId.Validator.validate(userId);
        if (error != null) {
            throw new BadRequestException(error);
        }
        interactor.deleteUser(userId);
        return Response.ok().build();
    }
}

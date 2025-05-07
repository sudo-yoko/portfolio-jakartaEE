package com.example.application.users;

import com.example.application.ExtractingJsonSerializer;
import com.example.domain.valueobjects.UserId;

import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

    @Inject
    private UsersInteractor interactor;

    @Path("/{userId}")
    @GET
    public Response getUser(@PathParam("userId") String userId, @QueryParam("properties") String properties) {

        String error = UserId.Validator.validate(userId);
        if (error != null) {
            throw new BadRequestException(error);
        }
        UsersResponse response = interactor.getUser(userId);
        if (response == null) {
            throw new NotFoundException("ユーザー情報がありません。");
        }
        JsonObject filteredResponse = ExtractingJsonSerializer.properties(properties).apply(response);
        return Response.ok(filteredResponse).build();
    }

}

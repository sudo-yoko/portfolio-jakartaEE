package com.example.application.users;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

    @Path("/{userId}")
    @GET
    public Response getUser(@PathParam("userId") String userId) {
        return Response.ok().build();
    }

}

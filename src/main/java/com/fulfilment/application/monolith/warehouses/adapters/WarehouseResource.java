package com.fulfilment.application.monolith.warehouses.adapters;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

import static io.quarkus.hibernate.orm.panache.PanacheEntity_.id;

@Path("/warehouse")
public interface WarehouseResource {
    @GET
    List<Warehouse> listAllWarehousesUnits();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON) // Accept JSON input
    @Produces(MediaType.APPLICATION_JSON)
    void createANewWarehouseUnit(@NotNull Warehouse data);

    @Path("/{id}")
    @GET
    Warehouse getAWarehouseUnitByID(@PathParam("id") String id);

    @Path("/archive/{id}")
    @PUT
    void archiveAWarehouseUnitByID(@PathParam("id") String id);

    @Path("/replace")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON) // Accept JSON input
    @Produces(MediaType.APPLICATION_JSON)
    void replaceAWareHouseUnit(@NotNull Warehouse data);

    @Path("/archive")
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON) // Accept JSON input
    @Produces(MediaType.APPLICATION_JSON)
    void archiveAWareHouseUnit(@NotNull Warehouse data);

    @Path("/remove/{id}")
    @DELETE
    void removeAWareHouseUnit(@PathParam("id") Long id);
}

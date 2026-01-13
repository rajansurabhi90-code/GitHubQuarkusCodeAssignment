package com.fulfilment.application.monolith.location;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class LocationGatewayTest {

  @Test
  public void testWhenResolveExistingLocationShouldReturn() {
    LocationGateway locationGateway = new LocationGateway();
    Location location = locationGateway.resolveByIdentifier("ZWOLLE-002");

    assertNotNull(location);
    assertEquals("ZWOLLE-002", location.getIdentification());
    assertEquals(2, location.getMaxNumberOfWarehouses());
    assertEquals(50, location.getMaxCapacity());
  }

  @Test
  public void testWhenIdentifierNotFoundForLocation() {
    LocationGateway locationGateway = new LocationGateway();

    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> locationGateway.resolveByIdentifier("ZWOLLE-182"));
    assertTrue(ex.getMessage().contains("Identifier Not found"));
  }

  @Test
  public void shouldFailWhenIdentifierEmpty() {
    LocationGateway locationGateway = new LocationGateway();

    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> locationGateway.resolveByIdentifier(""));
    assertTrue(ex.getMessage().contains("Identifier cannot be empty"));
  }
}

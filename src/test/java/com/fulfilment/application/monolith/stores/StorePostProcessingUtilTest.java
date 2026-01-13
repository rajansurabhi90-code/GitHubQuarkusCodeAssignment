package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.stores.events.StorePostProcessingEvent;
import com.fulfilment.application.monolith.stores.util.StorePostProcessingUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@QuarkusTest
public class StorePostProcessingUtilTest {
    @Inject
    StorePostProcessingUtil util;

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @Test
    void testPostProcess_CreateEvent() {
        Store store = new Store();
        StorePostProcessingEvent event =
                new StorePostProcessingEvent(
                        store,
                        StorePostProcessingEvent.EventName.CREATE
                );

        util.postProcess(event);

        verify(legacyStoreManagerGateway)
                .createStoreOnLegacySystem(store);
    }

    @Test
    void testPostProcess_UpdateEvent() {
        Store store = new Store();
        StorePostProcessingEvent event =
                new StorePostProcessingEvent(
                        store,
                        StorePostProcessingEvent.EventName.UPDATE
                );

        util.postProcess(event);

        verify(legacyStoreManagerGateway)
                .updateStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never())
                .createStoreOnLegacySystem(any());
    }

    @Test
    void testPostProcess_PatchEvent() {
        Store store = new Store();
        StorePostProcessingEvent event =
                new StorePostProcessingEvent(
                        store,
                        StorePostProcessingEvent.EventName.PATCH
                );

        util.postProcess(event);

        verify(legacyStoreManagerGateway)
                .updateStoreOnLegacySystem(store);
        verify(legacyStoreManagerGateway, never())
                .createStoreOnLegacySystem(any());
    }
}

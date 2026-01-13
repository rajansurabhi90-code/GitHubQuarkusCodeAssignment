package com.fulfilment.application.monolith.stores;

import com.fulfilment.application.monolith.stores.events.StorePostProcessingEvent;
import com.fulfilment.application.monolith.stores.util.StorePostProcessingListener;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.verify;

@QuarkusTest
public class StorePostProcessingListenerTest {
    @Inject
    StorePostProcessingListener listener;

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @Test
    void testAfterCommit_FullChain() {
        Store store = new Store();
        StorePostProcessingEvent event =
                new StorePostProcessingEvent(
                        store,
                        StorePostProcessingEvent.EventName.CREATE
                );

        listener.afterCommit(event);

        verify(legacyStoreManagerGateway)
                .createStoreOnLegacySystem(store);
    }
}



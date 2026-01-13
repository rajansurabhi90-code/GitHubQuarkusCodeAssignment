package com.fulfilment.application.monolith.stores.util;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.events.StorePostProcessingEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
@ApplicationScoped
public class StorePostProcessingUtil {
    @Inject
    public LegacyStoreManagerGateway legacyStoreManagerGateway;

    public void postProcess(StorePostProcessingEvent storePostProcessingEvent) {
        switch (storePostProcessingEvent.getEventName()) {
            case CREATE -> legacyStoreManagerGateway.createStoreOnLegacySystem(storePostProcessingEvent.getStore());
            case UPDATE, PATCH -> legacyStoreManagerGateway.updateStoreOnLegacySystem(storePostProcessingEvent.getStore());
        }
    }
}

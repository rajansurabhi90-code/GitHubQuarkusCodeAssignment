package com.fulfilment.application.monolith.stores.util;

import com.fulfilment.application.monolith.stores.events.StorePostProcessingEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

@ApplicationScoped
public class StorePostProcessingListener {

    @Inject
    StorePostProcessingUtil storePostProcessingUtil;

    public void afterCommit(@Observes (during =TransactionPhase.AFTER_SUCCESS)
                            StorePostProcessingEvent event) {
        storePostProcessingUtil.postProcess(event);
    }
}

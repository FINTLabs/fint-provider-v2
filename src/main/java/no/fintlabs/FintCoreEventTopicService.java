package no.fintlabs;

import no.fintlabs.adapter.models.AdapterContract;
import no.fintlabs.adapter.models.AdapterPing;
import no.fintlabs.kafka.event.topic.EventTopicNameParameters;
import no.fintlabs.kafka.event.topic.EventTopicService;
import org.springframework.stereotype.Service;

@Service
public class FintCoreEventTopicService {
    private final EventTopicService eventTopicService;
    private final ProviderProperties providerProperties;

    public FintCoreEventTopicService(EventTopicService eventTopicService, ProviderProperties providerProperties) {
        this.eventTopicService = eventTopicService;
        this.providerProperties = providerProperties;
    }

    public void ensureAdapterPingTopic(AdapterPing adapterPing) {
        eventTopicService.ensureTopic(EventTopicNameParameters
                        .builder()
                        .orgId(adapterPing.getOrgId())
                        .domainContext("fint-core")
                        .eventName("adapter-health")
                        .build(),
                providerProperties.getAdapterPingRetentionTimeMs()
        );
    }

    public void ensureAdapterRegisterTopic(AdapterContract adapterContract) {
        eventTopicService.ensureTopic(EventTopicNameParameters
                        .builder()
                        .orgId(adapterContract.getOrgId())
                        .domainContext("fint-core")
                        .eventName("adapter-register")
                        .build(),
                providerProperties.getAdapterRegisterRetentionTimeMs()
        );
    }

    public void ensureAdapterFullSyncTopic(AdapterContract adapterContract) {
        eventTopicService.ensureTopic(EventTopicNameParameters
                        .builder()
                        .orgId(adapterContract.getOrgId())
                        .domainContext("fint-core")
                        .eventName("adapter-full-sync")
                        .build(),
                providerProperties.getAdapterFullSyncRetentionTimeMs()
        );
    }

    public void ensureAdapterDeltaSyncTopic(AdapterContract adapterContract) {
        eventTopicService.ensureTopic(EventTopicNameParameters
                        .builder()
                        .orgId(adapterContract.getOrgId())
                        .domainContext("fint-core")
                        .eventName("adapter-delta-sync")
                        .build(),
                providerProperties.getAdapterDeltaSyncRetentionTimeMs()
        );
    }
}

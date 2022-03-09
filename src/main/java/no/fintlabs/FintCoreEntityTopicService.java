package no.fintlabs;

import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.AdapterContract;
import no.fintlabs.kafka.entity.EntityTopicNameParameters;
import no.fintlabs.kafka.entity.EntityTopicService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FintCoreEntityTopicService {
    private final EntityTopicService entityTopicService;

    public FintCoreEntityTopicService(EntityTopicService entityTopicService) {
        this.entityTopicService = entityTopicService;
    }

    public void ensureEntityTopic(AdapterCapability adapterCapability, String orgId, long retentionTimeMs) {
        entityTopicService.ensureTopic(EntityTopicNameParameters
                        .builder()
                        .orgId(orgId)
                        .domainContext("fint-core")
                        .resource(String.format(
                                        "%s-%s-%s",
                                        adapterCapability.getDomain(),
                                        adapterCapability.getPackageName(),
                                        adapterCapability.getClazz()
                                )
                        )
                        .build(),
                retentionTimeMs);
    }

    public void ensureAdapterEntityTopics(AdapterContract adapterContract) {
        adapterContract.getCapabilities()
                .forEach(capability -> ensureEntityTopic(capability,
                                adapterContract.getOrgId(),
                                TimeUnit.DAYS.toMillis(capability.getFullSyncIntervalInDays())
                        )
                );
    }
}

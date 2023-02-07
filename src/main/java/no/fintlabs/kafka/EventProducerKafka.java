package no.fintlabs.kafka;

import no.fintlabs.kafka.event.EventProducer;
import no.fintlabs.kafka.event.EventProducerFactory;
import no.fintlabs.kafka.event.EventProducerRecord;
import no.fintlabs.kafka.event.topic.EventTopicNameParameters;
import no.fintlabs.kafka.event.topic.EventTopicService;
import org.springframework.util.concurrent.ListenableFuture;

public abstract class EventProducerKafka<T> {

    private final EventProducer<T> eventProducer;
    private final EventTopicService eventTopicService;
    private String eventName;

    public EventProducerKafka(EventProducerFactory eventProducerFactory, EventTopicService eventTopicService, Class<T> valueClass, String eventName) {
        this.eventTopicService = eventTopicService;
        this.eventName = eventName;
        this.eventProducer = eventProducerFactory.createProducer(valueClass);
    }

    public ListenableFuture send(T value, String orgId) {
        EventTopicNameParameters eventTopicNameParameters = generateTopicName(orgId);
        return eventProducer.send(createEventProducerRecord(value, eventTopicNameParameters));
    }

    public void ensureTopic(String ordId, long retentionTimeMs) {
        // Todo See CT-457 for reference
        eventTopicService.ensureTopic(generateTopicName(ordId), retentionTimeMs);
    }

    public EventProducerRecord createEventProducerRecord(T value, EventTopicNameParameters topicName) {
        return EventProducerRecord.builder()
                .topicNameParameters(topicName)
                .value(value)
                .build();
    }

    public EventTopicNameParameters generateTopicName(String orgId) {
        return EventTopicNameParameters
                .builder()
                .orgId(orgId)
                .eventName(eventName)
                .build();
    }


//    public ListenableFuture<SendResult<String, Object>> sendEntity(String orgId, String domain, String packageName, String entityName, SyncPageEntry<Object> entity) {
//        return entityProducer.send(
//                EntityProducerRecord.builder()
//                        .topicNameParameters(EntityTopicNameParameters
//                                .builder()
//                                .orgId(orgId)
//                                .resource(String.format("%s-%s-%s", domain, packageName, entityName))
//                                .build())
//                        .key(entity.getIdentifier())
//                        .value(entity.getResource())
//                        .build()
//        );
//    }
}
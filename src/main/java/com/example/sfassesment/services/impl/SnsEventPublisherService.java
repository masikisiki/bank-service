package com.example.sfassesment.services.impl;

import com.example.sfassesment.dto.BankTransactionEvent;
import com.example.sfassesment.services.EventPublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsEventPublisherService implements EventPublisherService {
    private final static Logger LOG = LoggerFactory.getLogger(SnsEventPublisherService.class);

    private final SnsClient snsClient;
    private final String topicArn;

    public SnsEventPublisherService(@Value("${aws.sns.topic.arn}") String topicArn) {
        this.snsClient = SnsClient.builder()
                .region(Region.US_EAST_1)
                .build();
        this.topicArn = topicArn;
    }

    @Override
    public void publish(BankTransactionEvent event) {
        //Making this method safe so that the application does not crash if there is an error while publishing the message
        try {
            PublishRequest request = PublishRequest.builder()
                    .topicArn(topicArn)
                    .message(event.toJson())
                    .build();
            PublishResponse result = snsClient.publish(request);
            LOG.info("Message sent. MessageId={}", result.messageId());
        } catch (Exception e) {
            LOG.error("Error occurred while publishing message to SNS", e);
        }
    }
}

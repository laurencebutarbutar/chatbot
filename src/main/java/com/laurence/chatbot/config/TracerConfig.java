package com.laurence.chatbot.config;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig.SingleBaggageField;
import brave.propagation.Propagation;
import com.laurence.chatbot.enums.TracerField;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

import static brave.propagation.B3Propagation.FACTORY;

@Configuration
public class TracerConfig {

    @Bean
    public Propagation.Factory propagationFactory() {
        BaggagePropagation.FactoryBuilder factory = BaggagePropagation.newFactoryBuilder(FACTORY);
        Stream.of(TracerField.values())
                .map(TracerField::getVariable)
                .forEach(variable -> factory.add(SingleBaggageField.remote(BaggageField.create(variable))));
        return factory.build();
    }
}

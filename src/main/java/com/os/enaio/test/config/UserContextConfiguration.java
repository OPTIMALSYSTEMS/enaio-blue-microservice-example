package com.os.enaio.test.config;

import com.os.ecm.jobs.CallerSource;
import com.os.ecm.jobs.template.dms.GetResultListTemplate;
import com.os.services.blue.connector.BlueConnector;
import com.os.services.blue.connector.auth.BlueConnectorContextSwitch;
import com.os.services.blue.connector.auth.ContextSwitchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * <p>
 * This configuration introduces a user context, by enabling web security. That is due to the
 * {@link BlueConnectorContextSwitch} annotation. The {@link com.os.services.blue.connector.BlueConnector} annotation
 * from {@link com.os.enaio.test.Application} will be disabled.
 * </p>
 * <p>
 * You can activate the {@link UserContextConfiguration} by using the 'user-context' profile.
 * </p>
 */
@Configuration
@Profile("user-context")
@BlueConnectorContextSwitch
@Import({ContextSwitchConfiguration.class})
public class UserContextConfiguration {

    @Autowired
    @Qualifier("ecm.session.callerSource.user")
    private CallerSource userCallerSource;

    @Bean
    public GetResultListTemplate getGetResultListTemplate() {
        return new GetResultListTemplate(userCallerSource);
    }
}

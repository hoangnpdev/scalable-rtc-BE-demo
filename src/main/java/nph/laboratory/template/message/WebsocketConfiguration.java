package nph.laboratory.template.message;

import lombok.extern.slf4j.Slf4j;
import nph.laboratory.template.account.SessionManager;
import nph.laboratory.template.app.UserNameOnlyPrincipal;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final SessionManager sessionManager;

    public WebsocketConfiguration(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/web-socket")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                log.info("entering websocket interceptor");
                StompHeaderAccessor accessor = MessageHeaderAccessor
                        .getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String session = Optional.ofNullable(
                                    accessor.getNativeHeader("Authorization")
                            ).orElse(Arrays.asList("")).get(0);
                    log.info("session {}", session);
                    if (!sessionManager.checkSessionExist(session)) {
                        throw new CredentialsExpiredException("Bad credentials!");
                    }
                    accessor.setUser(new UserNameOnlyPrincipal(sessionManager.getAccountName(session)));
                }
                return message;
            }
        });
    }
}

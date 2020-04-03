package drama.painter.web.mall.tool;

import drama.painter.core.web.misc.User;
import drama.painter.web.mall.model.dto.oa.StaffDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import sun.security.acl.PrincipalImpl;
import drama.painter.web.mall.mapper.StaffMapper;
import drama.painter.web.mall.service.impl.ChatImpl;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Slf4j(topic = "chat")
@Configuration
@EnableWebSocketMessageBroker
public class WebSocket implements WebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/kefu");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) accessor.getHeader("simpUser");
					if (token == null) {
						throw new RuntimeException("请先登录。");
					} else if (!token.isAuthenticated()) {
						throw new RuntimeException("登录超时，请重新登录。");
					} else {
						Optional<User> any = USERS.stream().filter(o -> o.getUsername().equals(token.getName())).findAny();
						Asserts.check(!any.isPresent(), "该帐号已在其它地方登录了。");
						accessor.setUser(new PrincipalImpl(token.getName()));
						return message;
					}
				} else {
					return message;
				}
			}
		});
	}

	@Autowired
	StaffMapper staffMapper;

	static Queue<User> USERS = new ConcurrentLinkedQueue();

	@EventListener
	public void established(SessionConnectedEvent sce) {
		String username = sce.getUser().getName();
		StaffDTO staffDTO = staffMapper.getStaff(username);
		if (staffDTO == null) {
			log.info("[{}]加入聊天发生异常：无法获取本人ID", username);
		} else {
			USERS.offer(new User(staffDTO.getUserid(), staffDTO.getUsername()));
			String list = USERS.stream()
				.map(User::getUsername)
				.collect(Collectors.joining(", ", "[", "]"));
			log.info("[{}]加入聊天，当前：{}", username, list);
		}
	}

	@EventListener
	public void disconnected(SessionDisconnectEvent sde) {
		String username = sde.getUser().getName();
		ChatImpl.removeUser(USERS.stream()
			.filter(o -> o.getUsername().equals(username))
			.findAny()
			.orElse(new User(0, ""))
			.getUserid()
		);
		USERS.removeIf(o -> o.getUsername().equals(username));
		String list = USERS.stream()
			.map(User::getUsername)
			.collect(Collectors.joining(", ", "[", "]"));
		log.info("[{}]退出聊天，当前：{}", username, list);
	}

	public static User getUser() {
		User username = USERS.poll();
		if (username != null) {
			USERS.offer(username);
		}
		return username == null ? new User(0, "unknown") : username;
	}
}
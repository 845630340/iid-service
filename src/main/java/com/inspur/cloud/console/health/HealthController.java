package com.inspur.cloud.console.health;

import com.inspur.cloud.common.response.entity.ResponseBean;
import com.inspur.iam.adapter.annotation.PermissionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @ClassName HealthController
 * @Description 服务健康状态
 * @Author ruanchanglong
 * @Date 2019/11/120:14
 * @Version 1.0
 **/
@RestController
@RequestMapping("/iid/v1")
@Slf4j
public class HealthController {
	@Autowired
    RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	@PermissionContext(whitelist = true)
	@GetMapping("/health-status")
	public ResponseBean healthStatus(){
		Collection<MessageListenerContainer> containers = rabbitListenerEndpointRegistry.getListenerContainers();
		for (MessageListenerContainer container: containers) {
			if(!container.isRunning()) {
				String queueNames = StringUtils.arrayToCommaDelimitedString(((AbstractMessageListenerContainer)container).getQueueNames());
				log.warn("Listener for [" + queueNames + "] is down, try to restart it.");
				container.start();
			}
		}
		return new ResponseBean<>("successful");
	}
}

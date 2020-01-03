package com.zengcheng.sandhouse.web.websocket;

import com.zengcheng.sandhouse.common.entity.ResponseEntity;
import com.zengcheng.sandhouse.common.entity.ResponseEntityFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * WebSocket样例控制器
 * @author zengcheng
 * @date 2019/12/19
 */
@Slf4j
@RestController
public class WebSocketExampleController {

    @Resource
    private SimpMessagingTemplate template;

    @MessageMapping("/sayHi")
    @SendTo("/topic/getResponse")
    public ResponseEntity sayHi(String msg) {
        log.info("websocket msg: {}", msg);
        return ResponseEntityFactory.success(msg);
    }

    @GetMapping(value = "/websocket/hello",produces = "application/json; charset=utf-8")
    public ResponseEntity sayHello(@RequestParam String msg){
        template.convertAndSend("/topic/getResponse",msg);
        return ResponseEntityFactory.success(msg);
    }

    @GetMapping(value = "/websocket/helloToTargetPerson",produces = "application/json; charset=utf-8")
    public String sayHelloToTarget(@RequestParam String msg,@RequestParam String toPerson){
        template.convertAndSendToUser(toPerson,"/topic/hello",msg);
        return msg;
    }

}

package com.wenxin.demo.controller;

import com.gearwenxin.client.ernie.ErnieBot4Client;
import com.gearwenxin.entity.chatmodel.ChatErnieRequest;
import com.gearwenxin.entity.response.ChatResponse;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Ge Mingjia
 * @date 2023/7/29
 */
@RestController
@RequestMapping("/ernie")
public class ErnieBotController {

    @Resource
    private ErnieBot4Client ernieBot4Client;

    //------------------默认参数------------------//

    // 单轮对话
    @PostMapping("/chat")
    public Mono<ChatResponse> chatSingle(String msg) {
        return ernieBot4Client.chatSingle(msg);
    }

    // 连续对话
    @PostMapping("/chats")
    public Mono<ChatResponse> chatCont(String msg, String msgUid) {
        return ernieBot4Client.chatCont(msg, msgUid);
    }

    // 流式返回，单次对话
    @GetMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatSingleStream(@RequestParam String msg) {
        Flux<ChatResponse> chatResponse = ernieBot4Client.chatSingleOfStream(msg);

        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    @GetMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chatSingleSSE(@RequestParam String msg) {
        SseEmitter emitter = new SseEmitter();

        ernieBot4Client.chatSingleOfStream(msg)
                .subscribe(
                        response -> {
                            try {
                                emitter.send(SseEmitter.event().data("data: " + response.getResult() + "\n\n"));
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        },
                        emitter::completeWithError,
                        emitter::complete
                );
        return emitter;
    }

    // 流式返回，连续对话
    @GetMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatContStream(@RequestParam String msg, @RequestParam String msgUid) {
        Flux<ChatResponse> chatResponse = ernieBot4Client.chatContOfStream(msg, msgUid);

        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    //------------------自定义参数------------------//

    @PostMapping("/param/chat")
    public Mono<ChatResponse> pChatSingle(@RequestBody ChatErnieRequest chatErnieRequest) {
        return ernieBot4Client.chatSingle(chatErnieRequest);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public Mono<ChatResponse> pChatCont(@RequestBody ChatErnieRequest chatErnieRequest) {
        return ernieBot4Client.chatCont(chatErnieRequest, chatErnieRequest.getUserId());
    }

}

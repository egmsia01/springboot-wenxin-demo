package com.wenxin.demo.controller;

import com.gearwenxin.client.ernie.ErnieBotClient;
import com.gearwenxin.entity.chatmodel.ChatErnieRequest;
import com.gearwenxin.entity.response.ChatResponse;
import com.wenxin.demo.common.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author Ge Mingjia
 * @date 2023/7/29
 */
@RestController
@RequestMapping("/ernie")
public class ErnieBotController {

    @Resource
    private ErnieBotClient ernieBotClient;

    //------------------默认参数------------------//

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(@RequestBody String content) {
        ChatResponse chatResponse = ernieBotClient.chatSingle(content);
        return BaseResponse.success(chatResponse);
    }
    @GetMapping
    public BaseResponse<ChatResponse> get() {
        return BaseResponse.success(null);
    }
    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(@RequestBody String msg,@RequestBody  String chatUid) {
        ChatResponse response = ernieBotClient.chatCont(msg, chatUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatSingleStream(@RequestBody String msg) {
        Flux<ChatResponse> chatResponseFlux = ernieBotClient.chatSingleOfStream(msg);
        return chatResponseFlux;
    }

    // 流式返回，连续对话
    @PostMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatContStream(@RequestBody String msg, @RequestBody String msgUid) {
        Flux<ChatResponse> chatResponseFlux = ernieBotClient.chatContOfStream(msg, msgUid);
        return chatResponseFlux;
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatErnieRequest chatErnieRequest) {
        ChatResponse chatResponse = ernieBotClient.chatSingle(chatErnieRequest);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatErnieRequest chatErnieRequest) {
        String chatUid = "1001";
        ChatResponse response = ernieBotClient.chatCont(chatErnieRequest, chatUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/param/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatSingleStream(@RequestBody ChatErnieRequest chatErnieRequest) {
        Flux<ChatResponse> chatResponseFlux = ernieBotClient.chatSingleOfStream(chatErnieRequest);
        return chatResponseFlux;
    }

    // 流式返回，连续对话
    @PostMapping(value = "/param/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatContStream(@RequestBody ChatErnieRequest chatErnieRequest, String msgUid) {
        Flux<ChatResponse> chatResponseFlux = ernieBotClient.chatContOfStream(chatErnieRequest, msgUid);
        return chatResponseFlux;
    }

}

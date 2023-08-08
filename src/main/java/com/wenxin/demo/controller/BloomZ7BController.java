package com.wenxin.demo.controller;


import com.gearwenxin.client.BloomZ7BClient;
import com.gearwenxin.entity.chatmodel.ChatBloomZ7BRequest;
import com.gearwenxin.entity.response.ChatResponse;
import com.wenxin.demo.common.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@RestController
@RequestMapping("/bloomz7b")
public class BloomZ7BController {

    @Resource
    private BloomZ7BClient bloomz7BClient;

    //------------------默认参数------------------//

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(String msg) {
        ChatResponse chatResponse = bloomz7BClient.chatSingle(msg);
        return BaseResponse.success(chatResponse);
    }

    @GetMapping
    public BaseResponse<ChatResponse> get() {
        return BaseResponse.success(null);
    }

    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(String msg, String msgUid) {
        ChatResponse response = bloomz7BClient.chatCont(msg, msgUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatSingleStream(String msg) {
        return bloomz7BClient.chatSingleOfStream(msg);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatContStream(String msg, String msgUid) {
        return bloomz7BClient.chatContOfStream(msg, msgUid);
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatBloomZ7BRequest chatTurbo7BRequest) {
        ChatResponse chatResponse = bloomz7BClient.chatSingle(chatTurbo7BRequest);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatBloomZ7BRequest chatTurbo7BRequest) {
        ChatResponse response = bloomz7BClient.chatCont(chatTurbo7BRequest, chatTurbo7BRequest.getUserId());
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/param/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatSingleStream(@RequestBody ChatBloomZ7BRequest chatTurbo7BRequest) {
        return bloomz7BClient.chatSingleOfStream(chatTurbo7BRequest);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/param/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatContStream(@RequestBody ChatBloomZ7BRequest chatTurbo7BRequest, String msgUid) {
        return bloomz7BClient.chatContOfStream(chatTurbo7BRequest, msgUid);
    }

}

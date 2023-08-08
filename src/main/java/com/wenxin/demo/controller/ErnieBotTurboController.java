package com.wenxin.demo.controller;

import com.gearwenxin.client.ernie.ErnieBotTurboClient;
import com.gearwenxin.entity.chatmodel.ChatBaseRequest;
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
@RequestMapping("/turbo")
public class ErnieBotTurboController {

    @Resource
    private ErnieBotTurboClient ernieBotTurboClient;

    //------------------默认参数------------------//
    @GetMapping
    public BaseResponse<ChatResponse> get() {
        return BaseResponse.success(null);
    }

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(String msg) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(msg);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(String msg, String msgUid) {
        ChatResponse response = ernieBotTurboClient.chatCont(msg, msgUid);
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatSingleStream(String msg) {
        return ernieBotTurboClient.chatSingleOfStream(msg);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chatContStream(String msg, String msgUid) {
        return ernieBotTurboClient.chatContOfStream(msg, msgUid);
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatBaseRequest chatTurbo7BRequest) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(chatTurbo7BRequest);
        return BaseResponse.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatBaseRequest chatTurbo7BRequest) {
        ChatResponse response = ernieBotTurboClient.chatCont(chatTurbo7BRequest, chatTurbo7BRequest.getUserId());
        return BaseResponse.success(response);
    }

    // 流式返回，单次对话
    @PostMapping(value = "/param/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatSingleStream(@RequestBody ChatBaseRequest chatTurbo7BRequest) {
        return ernieBotTurboClient.chatSingleOfStream(chatTurbo7BRequest);
    }

    // 流式返回，连续对话
    @PostMapping(value = "/param/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> pChatContStream(@RequestBody ChatBaseRequest chatTurbo7BRequest, String msgUid) {
        return ernieBotTurboClient.chatContOfStream(chatTurbo7BRequest, msgUid);
    }

}

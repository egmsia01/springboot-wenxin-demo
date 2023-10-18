package com.wenxin.demo.controller;

import com.gearwenxin.client.ernie.ErnieBotTurboClient;
import com.gearwenxin.entity.chatmodel.ChatBaseRequest;
import com.gearwenxin.entity.response.ChatResponse;
import com.wenxin.demo.common.BaseResponse;
import com.wenxin.demo.exception.ResultUtils;
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

    // 单轮对话
    @PostMapping("/chat")
    public BaseResponse<ChatResponse> chatSingle(String msg) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(msg).block();
        return ResultUtils.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/chats")
    public BaseResponse<ChatResponse> chatCont(String msg, String msgUid) {
        ChatResponse response = ernieBotTurboClient.chatCont(msg, msgUid).block();
        return ResultUtils.success(response);
    }

    // 流式返回，单次对话
    @GetMapping(value = "/stream/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatSingleStream(@RequestParam String msg) {
        Flux<ChatResponse> chatResponse = ernieBotTurboClient.chatSingleOfStream(msg);
        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    // 流式返回，连续对话
    @PostMapping(value = "/stream/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatContStream(@RequestParam String msg, @RequestParam String msgUid) {
        Flux<ChatResponse> chatResponse = ernieBotTurboClient.chatContOfStream(msg, msgUid);
        return chatResponse.map(response -> "data: " + response.getResult() + "\n\n");
    }

    //------------------自定义参数------------------//

    // 模板对话
    @PostMapping("/param/chat")
    public BaseResponse<ChatResponse> pChatSingle(@RequestBody ChatBaseRequest chatBaseRequest) {
        ChatResponse chatResponse = ernieBotTurboClient.chatSingle(chatBaseRequest).block();
        return ResultUtils.success(chatResponse);
    }

    // 连续对话
    @PostMapping("/param/chats")
    public BaseResponse<ChatResponse> pChatCont(@RequestBody ChatBaseRequest chatBaseRequest) {
        ChatResponse response = ernieBotTurboClient.chatCont(chatBaseRequest, chatBaseRequest.getUserId()).block();
        return ResultUtils.success(response);
    }

}

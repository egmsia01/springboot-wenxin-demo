package com.wenxin.demo.controller;

import com.gearwenxin.client.PromptClient;
import com.gearwenxin.model.chatmodel.ChatPromptRequest;
import com.gearwenxin.model.response.PromptResponse;
import com.wenxin.demo.common.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ge Mingjia
 * @date 2023/7/29
 */
@RestController
@RequestMapping("/api/propt")
public class PromptController {

    @Resource
    private PromptClient promptClient;

    // 模板对话
    @PostMapping
    public BaseResponse<PromptResponse> chatSingle(int id) {

        Map<String, String> map = new HashMap<>();
        map.put("article", "人们都说：“桂林山水甲天下。”我们乘着木船，荡漾在漓江上，来观赏桂林的山水。\n" +
                "我看见过波澜壮阔的大海，玩赏过水平如镜的西湖，却从没看见过漓江这样的水。漓江的水真静啊，静得让你感觉不到它在流动；漓江的水真清啊，清得可以看见江底的沙石；漓江的水真绿啊，绿得仿佛那是一块无瑕的翡翠。船桨激起的微波扩散出一道道水纹，才让你感觉到船在前进，岸在后移。\n" +
                "我攀登过峰峦雄伟的泰山，游览过红叶似火的香山，却从没看见过桂林这一带的.山，桂林的山真奇啊，一座座拔地而起，各不相连，像老人，像巨象，像骆驼，奇峰罗列，形态万千；桂林的山真秀啊，像翠绿的屏障，像新生的竹笋，色彩明丽，倒映水中；桂林的山真险啊，危峰兀立，怪石嶙峋，好像一不小心就会栽倒下来。\n" +
                "这样的山围绕着这样的水，这样的水倒映着这样的山，再加上空中云雾迷蒙，山间绿树红花，江上竹筏小舟，让你感到像是走进了连绵不断的画卷，真是“舟行碧波上，人在画中游”。");
        map.put("number", "20");
        ChatPromptRequest promptRequest = new ChatPromptRequest();
        promptRequest.setId(id);
        promptRequest.setParamMap(map);
        PromptResponse promptResponse = promptClient.chatPrompt(promptRequest);

        return BaseResponse.success(promptResponse);
    }

}

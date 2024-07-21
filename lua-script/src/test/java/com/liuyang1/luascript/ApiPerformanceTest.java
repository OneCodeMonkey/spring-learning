//package com.liuyang1.luascript;
//
//import com.google.gson.Gson;
//import com.liuyang1.luascript.grpc.GrpcClient;
//import com.liuyang1.luascript.grpc.SentenceDetectOuterClass;
//import com.liuyang1.luascript.hub.AssistantV2Request;
//import com.liuyang1.luascript.hub.DeviceAppStateContextItem;
//import com.liuyang1.luascript.hub.DeviceAppStatePayload;
//import com.liuyang1.luascript.hub.Header;
//import com.liuyang1.luascript.hub.tts.AsrParams;
//import com.xiaojukeji.davinci.nlp.didilog.utils.HttpUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.LinkedList;
//import java.util.List;
//
//@SpringBootTest
//@Slf4j
//public class ApiPerformanceTest {
//    String baseUrl = "https://mona.xiaojukeji.com/prediff2/nlp/hub/queryV2Stream";
//    List<String> queryList = Arrays.asList(
//            "播放歌曲爱江山更爱美人",
//            "导航去白鹿洞书院",
//            "播放西游记",
//            "打开灯光",
//            "关闭车灯",
//            "播放哪吒传奇",
//            "去南海碧桂园",
//            "播放地老天荒",
//            "播放周杰伦的龙拳",
//            "导航蓝森牛肉面",
//            "今天限行尾号",
//            "打电话给江长荣",
//            "播放男人花",
//            "播放棉子的勇气",
//            "导航到南港路",
//            "导航妇产医院东院区",
//            "播放猪猪侠",
//            "播放海底小纵队的故事",
//            "导航去南通永旺",
//            "帮我放一首陈奕迅的孤勇者",
//            "收藏歌曲",
//            "播放孙楠的善变",
//            "播放流行音乐",
//            "播放敌意",
//            "导航到友谊酒店",
//            "导航万科森林公园",
//            "音乐关掉",
//            "后天天气",
//            "我不想说话",
//            "拨打张老爸电话",
//            "给我播放肖战的歌",
//            "播放像我这样的人",
//            "播放张杰的歌曲",
//            "播放一首衡山路",
//            "请播放一首小海的衡山路",
//            "播放歌曲相逢是首歌",
//            "导航去运河宸园",
//            "导航去念坛公园",
//            "第一个第一个",
//            "导航田心裕田大楼",
//            "导航到公司",
//            "导航到充电桩",
//            "全车二十四度",
//            "切换成燃油模式",
//            "胎压多少",
//            "导航去玉米玉米楼",
//            "导航去武汉蓝图科技有限公司五号门",
//            "带我去天河区益智街",
//            "导航到越州工贸园区",
//            "取消",
//            "导航去南医大二附院",
//            "导航到中国石油加油站",
//            "导航去齐齐哈尔烤肉店",
//            "放宝宝巴士儿歌",
//            "放一首水果的英文歌",
//            "放小蝌蚪找妈妈重复播放",
//            "我要听一首小蝌蚪找妈妈",
//            "播放首世上只有妈妈好",
//            "播一首阿爸阿妈",
//            "放一首心上人",
//            "打电话给威马官方客服",
//            "今天的天气预报",
//            "关闭",
//            "打开双闪灯",
//            "怎么关闭儿童锁",
//            "播放一首分分钟需要你",
//            "播放杨宗纬的长镜头",
//            "我想听周杰伦的歌",
//            "播放留什么给你",
//            "我想听蔡依林的歌",
//            "我想听张学友的歌",
//            "看左侧轮胎",
//            "打电话给妈妈",
//            "送把关了打开",
//            "附近的加油站",
//            "我要去梅林加油站",
//            "导航到广州市第一人民医院",
//            "导航到西区医院",
//            "导航到长龙岛",
//            "导航到洪塘派出所",
//            "锁车",
//            "来一首郁可唯的他不懂",
//            "来一首爱的代价",
//            "关下",
//            "播放玫瑰少年",
//            "播放无奈的思绪",
//            "播放红楼梦主题曲",
//            "播放哪里都是你",
//            "播放米小圈三国演义",
//            "播放收音机",
//            "郭德纲相声",
//            "我想听广播",
//            "下去",
//            "拨打周翔雨电话",
//            "那个开空调还是不好",
//            "唐林电话",
//            "关闭副屏",
//            "打开屏幕打开屏幕",
//            "座椅通风",
//            "把座椅通风关了吧",
//            "播一个可可托海的牧羊人",
//            "播放卢巧音的好心分手",
//            "播放陈加玲的呼吸决定",
//            "全车空调调到二十三度",
//            "温度调到二十四度",
//            "给景江打电话",
//            "混动模式",
//            "该更新了",
//            "打开全部车灯",
//            "关闭全车车灯",
//            "打开外循环",
//            "灰姑娘的故事",
//            "打电话给慕殷",
//            "窗户关闭",
//            "没有关闭啊",
//            "断开蓝牙",
//            "开始导航",
//            "去皇庭广场",
//            "导航去取消导航",
//            "导航和平饭店",
//            "导航到腾讯大厦",
//            "取消导航",
//            "继续导航",
//            "拨打杜飞",
//            "播放火力全开",
//            "切换到网易音乐",
//            "关闭爱奇艺",
//            "请唱疯了巴啦啦小魔仙的歌",
//            "播放世上只有妈妈好",
//            "播放你是我的唯一",
//            "开最高",
//            "唱小阿枫的歌",
//            "把声音关掉",
//            "退出音",
//            "冲水雨刮器冲水",
//            "我要回家",
//            "导航到朱宏路",
//            "第二个",
//            "听宝宝巴士",
//            "讲个笑话",
//            "播放广播",
//            "关一下",
//            "请开启小憩模式",
//            "全车空调二十五度",
//            "关车灯",
//            "关后备箱门",
//            "停止充电",
//            "快退下退下下载",
//            "播放民谣",
//            "播放我收藏的歌曲",
//            "音乐音量调到六十",
//            "三十音量",
//            "音量二十",
//            "窗户关上",
//            "关窗户",
//            "我是你爸爸",
//            "给朱大哥打电话",
//            "打开遮阳板",
//            "奥特曼",
//            "导航去金源御景华府北门",
//            "第三个",
//            "导航文旅发展中心",
//            "导航到蓝山国际a座",
//            "第六个吧",
//            "关闭遮阳板",
//            "放李荣浩的歌",
//            "请播放超人宝宝",
//            "播放小猪佩奇",
//            "单曲循环",
//            "最喜欢的歌",
//            "我已经调整了",
//            "打开天窗",
//            "关闭车门",
//            "续航",
//            "还有勿扰模式了",
//            "拨打六六幺",
//            "打开导航",
//            "导航至",
//            "铁东路",
//            "开着开热风",
//            "你把空调给我调到最凉",
//            "关闭AC",
//            "关闭车内灯光",
//            "关闭远光灯",
//            "退出",
//            "灯光控",
//            "到嘉兴千秋工程咨询有限公司",
//            "东站",
//            "我要去卓悦汇",
//            "傻子",
//            "走了下车了",
//            "关后备箱",
//            "锁车门",
//            "悬架舒适",
//            "去庄胜广场",
//            "红楼梦",
//            "拨打给老婆",
//            "海口最大的超市",
//            "关闭阅读灯",
//            "关闭雨刷维修模式"
//    );
//
//    private static String queryToSententCompleteStr = "";
//
//    private static int counter = 0;
//    private static HashMap<String, Boolean> queryToSentenceMapping;
//
//    private final Gson gson = new Gson();
//
//    private HashMap<String, Boolean> getQueryToSentence() {
//        URL currentFilePath = new Object() {
//        }.getClass().getEnclosingClass().getResource("");
//        String directoryPath = currentFilePath.getPath();
//        System.out.println("path: " + directoryPath);
//        directoryPath = directoryPath.replace("target/test-classes", "src/test/java");
//
//        queryToSentenceMapping = new HashMap<>();
//        try {
//            FileInputStream fis = new FileInputStream(directoryPath + "text_result.txt");
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//                count++;
//                String[] arr = line.split(" -> ");
//                String query = arr[0];
//                Boolean isComplete = arr[1].equals("true");
//                queryToSentenceMapping.put(query, isComplete);
//                if (count % 1000 == 0) {
//                    log.info("handled {} query: {}", count, query);
//                }
//            }
//        } catch (IOException e) {
//            log.error("exception: {}", e.getMessage());
//        }
//
////        String[] queryToSentenceResultArr = queryToSententCompleteStr.split("\n");
////        HashMap<String, Boolean> queryToSentenceMapping = new HashMap<>();
////        for (String row : queryToSentenceResultArr) {
////            String[] arr = row.split(" -> ");
////            String rawQuery = arr[0];
////            Boolean sentenceCompleteResult = arr[1].equals("true");
////            queryToSentenceMapping.put(rawQuery, sentenceCompleteResult);
////        }
//        log.info("queryToSentenceMapping size: {}", queryToSentenceMapping.size());
//        return queryToSentenceMapping;
//    }
//
//    public void initQueryList() {
//        URL currentFilePath = new Object() {
//        }.getClass().getEnclosingClass().getResource("");
//        String directoryPath = currentFilePath.getPath();
//        System.out.println("path: " + directoryPath);
//        directoryPath = directoryPath.replace("target/test-classes", "src/test/java");
//
//        queryList = new LinkedList<>();
//        try {
//            FileInputStream fis = new FileInputStream(directoryPath + "test_5w.txt");
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//                count++;
//                line = line.replace("\t", " ").replace("   ", " ").replace("  ", " ");
//                String[] arr = line.split(" ");
//                String query = arr[0];
//
//                if (count % 1000 == 0) {
//                    log.info("handled {} query: {}", count, query);
//                }
//                queryList.add(query);
//            }
//        } catch (IOException e) {
//            log.error("exception: {}", e.getMessage());
//        }
//
//        log.info("queryList size: {}", queryList.size());
//    }
//
//    @Test
//    public void getQueryToSentenceTest() {
//        queryToSentenceMapping = getQueryToSentence();
//        for (String query : queryList) {
//            System.out.println(query + ": " + (queryToSentenceMapping.get(query) ? "✅" : "❌"));
//        }
//    }
//
//    @Test
//    public void testStreamApi() {
//        initQueryList();
//        queryToSentenceMapping = getQueryToSentence();
//
//        long totalCost = 0, time = 0, totalPredictTimes = 0, totalPredictRequest = 0;
//        HashMap<String, Long> queryToCost = new HashMap<>();
//        HashMap<String, String> queryPredictResult = new HashMap<>();
//        HashMap<String, Integer> queryToPredictTimes = new HashMap<>(), predictTypeCount = new HashMap<>();
//        for (String query : queryList) {
//            time++;
//            if (time <= 38900) {
//                continue;
//            }
////            if (time > 40000) {
////                break;s
////            }
//            String rawTraceid = genTraceId();
//            for (int i = 0; i < query.length() + 1; i++) {
//                String subQuery = query.substring(0, Math.min(i + 1, query.length()));
//                int sequenceId = i != query.length() ? i + 1 : 0;
//                long startTime = System.currentTimeMillis();
//                String response = singleApiRequestQueryV2Stream(baseUrl, "preview1", rawTraceid, subQuery, sequenceId);
//                long cost = System.currentTimeMillis() - startTime;
//                log.info("query: {}, response: {}, cost: {}ms", subQuery, response, cost);
////                if (sequenceId == 0) {
////                    totalCost += cost;
////                }
////                if (subQuery.equals(query)) {
////                    queryToCost.put(query, cost);
////                }
//
//                String predictType = "";
//                int predictTimes = 0;
//                if (response.contains("predictType")) {
//                    String split1 = response.split("predictType:")[1];
//                    if (split1.contains("wakeUpPosition")) {
//                        predictType = split1.split(" ,")[0].trim();
//                    } else {
//                        predictType = split1.split("\"")[0].trim();
//                    }
//                    queryPredictResult.put(query, predictType);
//                    predictTypeCount.merge(predictType, 1, Integer::sum);
//                    if (!predictType.equals("no predict")) {
//                        String split11 = response.split("predictTimes:")[1];
//                        if (split1.contains("wakeUpPosition")) {
//                            predictTimes = Integer.parseInt(split11.split(",")[0].trim());
//                        } else {
//                            predictTimes = Integer.parseInt(split11.split("\"")[0].trim());
//                        }
//                        totalPredictRequest++;
//                        totalPredictTimes += predictTimes;
//                        queryToPredictTimes.put(query, predictTimes);
//                    }
//                    System.out.println(query + " \tpredictTimes: " + predictTimes + " \t avg predictTimes: " + (totalPredictTimes * 1.0 / totalPredictRequest));
//                }
//
//                if (sequenceId != 0) {
//                    try {
//                        Thread.sleep(8);
//                    } catch (InterruptedException ignored) {
//                    }
//                }
//            }
//            log.info("handled {} query: {}", time, query);
//            if (time % 100 == 0) {
//                log.info("times: {}, predictType count: {}", time, gson.toJson(predictTypeCount));
//            }
//            try {
//                Thread.sleep(2);
//            } catch (InterruptedException ignored) {
//            }
//
////            System.out.println(queryToCost.toString());
////            break;
//        }
//        log.info("average cost: {}ms", totalCost / time);
//
//        for (String key : queryPredictResult.keySet()) {
//            System.out.println(key + ": \t " + queryPredictResult.get(key) + " \t" + queryToPredictTimes.getOrDefault(key, 0));
//        }
//
//        log.info("total predict request: {}, total predict times: {}, avg predict time: {}", totalPredictRequest, totalPredictTimes, (totalPredictTimes * 1.0 / totalPredictRequest));
//
//        log.info("predictType count: {}", gson.toJson(predictTypeCount));
//    }
//
//    @Test
//    public void generateSubQuerys() throws IOException {
//        HashSet<String> querySet = new HashSet<>();
//        for (String query : queryList) {
//            for (int i = 0; i < query.length(); i++) {
//                String subQuery = query.substring(0, i + 1);
//                if (querySet.contains(subQuery)) {
//                    continue;
//                }
//                System.out.println(subQuery);
//                querySet.add(subQuery);
//            }
//        }
//    }
//
//    @Test
//    public void testGrpcRequest() {
//        GrpcClient client = new GrpcClient();
//        SentenceDetectOuterClass.Response result = client.handle("我想去天安门");
//        log.info("result: {}", result);
//    }
//
//    private String singleApiRequestQueryV2(String url, String vid, String traceId, String query) {
//        AssistantV2Request request = new AssistantV2Request();
//        request.setVehicleId(vid);
//        request.setTraceId(traceId);
//        request.setAudio(query);
//        request.setToken("1dc600ca627dfbbeb5e10c5401f35501");
//        request.setSourceType(2);
//        request.setAppVersion("0.4.8");
//        request.setExtend(new HashMap<>() {{
//            put("wakeUpPosition", "1");
//            put("lng", "110.11");
//            put("lat", "40.11");
//        }});
//        List<DeviceAppStateContextItem> deviceAppStateContextItemList = new LinkedList<>();
//        DeviceAppStateContextItem deviceAppStateContextItem = new DeviceAppStateContextItem();
//        Header header = new Header();
//        header.setName("appState");
//        header.setNamespace("appState");
//        deviceAppStateContextItem.setHeader(header);
//        DeviceAppStatePayload payload = new DeviceAppStatePayload();
//        payload.setRunning(Arrays.asList("com.didichuxing.diia.map", "com.didi.davinci.media.music", "com.didi.davinci.media.musicqq"));
//        payload.setInstalled(Arrays.asList("com.didi.davinci.media.music", "com.didi.davinci.media.musicqq"));
//        payload.setFocused(Arrays.asList("com.didi.davinci.media.musicqq"));
//        payload.setPlaying(Arrays.asList("com.didi.davinci.media.musicqq"));
//        payload.setDock(Arrays.asList("com.didi.davinci.media.musicqq"));
//        deviceAppStateContextItem.setPayload(payload);
//
//        deviceAppStateContextItemList.add(deviceAppStateContextItem);
//
//        request.setDeviceContext(deviceAppStateContextItemList);
//
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "application/json");
//        headers.put("nuwa-wings", "env=osim109-v");
//        String response = HttpUtils.post(url, new HashMap<>(), headers, request);
//
//        return response;
//    }
//
//    private String singleApiRequestQueryV2Stream(String url, String vid, String traceId, String query, int sequenceId) {
//        AssistantV2Request request = new AssistantV2Request();
//        request.setVehicleId(vid);
//        request.setTraceId(traceId);
//
//        AsrParams asrParams = new AsrParams();
//        if (sequenceId > 0) {
//            asrParams.setSequenceId(sequenceId);
//            request.setAsr(asrParams);
//        }
//        request.setAudio(query);
//        request.setToken("1dc600ca627dfbbeb5e10c5401f35501");
//        request.setSourceType(2);
//        request.setAppVersion("0.4.8");
//        request.setExtend(new HashMap<>() {{
//            put("wakeUpPosition", "1");
//            put("lng", "110.11");
//            put("lat", "40.11");
//        }});
//        List<DeviceAppStateContextItem> deviceAppStateContextItemList = new LinkedList<>();
//        DeviceAppStateContextItem deviceAppStateContextItem = new DeviceAppStateContextItem();
//        Header header = new Header();
//        header.setName("appState");
//        header.setNamespace("appState");
//        deviceAppStateContextItem.setHeader(header);
//        DeviceAppStatePayload payload = new DeviceAppStatePayload();
//        payload.setRunning(Arrays.asList("com.didichuxing.diia.map", "com.didi.davinci.media.music", "com.didi.davinci.media.musicqq"));
//        payload.setInstalled(Arrays.asList("com.didi.davinci.media.music", "com.didi.davinci.media.musicqq"));
//        payload.setFocused(Arrays.asList("com.didi.davinci.media.musicqq"));
//        payload.setPlaying(Arrays.asList("com.didi.davinci.media.musicqq"));
//        payload.setDock(Arrays.asList("com.didi.davinci.media.musicqq"));
//        deviceAppStateContextItem.setPayload(payload);
//
//        deviceAppStateContextItemList.add(deviceAppStateContextItem);
//
//        request.setDeviceContext(deviceAppStateContextItemList);
//
//        // 加入 断句模型结果。
//        HashMap<String, String> asrExtend = new HashMap<>();
//        Boolean isSentenceCompleteResult = queryToSentenceMapping.getOrDefault(query, false);
//        String isSentenceComplete = isSentenceCompleteResult ? "1" : "0";
//        if (isSentenceCompleteResult) {
//            System.out.println(query + " is algo complete.");
//        }
//        asrExtend.put("sentence", isSentenceComplete);
//        asrParams.setExtend(asrExtend);
//        request.setAsr(asrParams);
//
//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Content-type", "application/json");
////        headers.put("nuwa-wings", "env=osim109-v");
//        String response = HttpUtils.post(url, new HashMap<>(), headers, request);
//
//        return response;
//    }
//
//    private String genTraceId() {
//        StringBuilder traceId = new StringBuilder("f08041116a52e40429a52e6269907");
//        if (counter < 10) {
//            traceId.append("000");
//        } else if (counter < 100) {
//            traceId.append("00");
//        } else if (counter < 1000) {
//            traceId.append("0");
//        }
//        traceId.append(counter++);
//
//        log.info("traceId: {}", traceId);
//        return traceId.toString();
//    }
//}

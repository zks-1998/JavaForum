package com.qau.cache;

import com.qau.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO front = new TagDTO();
        front.setCategoryName("前端");
        front.setTags(Arrays.asList("HTML","CSS","JavaScript","Node.js"));
        tagDTOS.add(front);

        TagDTO behind = new TagDTO();
        behind.setCategoryName("服务端");
        behind.setTags(Arrays.asList("spring","Mybatis","SpringBoot","SpringMVC","SpringCloud"));
        tagDTOS.add(behind);

        TagDTO server = new TagDTO();
        server.setCategoryName("数据库");
        server.setTags(Arrays.asList("MySQL","NoSQL","Redis","Oracle","H2"));
        tagDTOS.add(server);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("Git","IDEA","SVN","Eclipse","Postman"));
        tagDTOS.add(tool);
        return tagDTOS;
    }

    public static String filterInvalid(String tags){
        String [] split = StringUtils.split(tags,",");
        List<TagDTO> tagDTOS = get();
        //flatMap 流的扁平化
        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}

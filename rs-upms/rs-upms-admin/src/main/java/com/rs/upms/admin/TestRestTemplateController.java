package com.rs.upms.admin;

import com.rs.common.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestRestTemplateController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping(value = "/infos/{id}")
    public Object info(@PathVariable("id") String id, ModelMap modelMap) throws IOException {

        String str="中华人民共和国";
        String uri = "http://127.0.0.1:8000/infos";
        Map<String, Object> result = new HashMap<>(16);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<Object> responseEntity=restTemplate.exchange(uri, HttpMethod.GET, entity,Object.class);
        String url="http://wthrcdn.etouch.cn/weather_mini?city=杭州市";
        HttpHeaders headersw = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entityw = new HttpEntity<String>(headers);
        ResponseEntity<String> responseEntityw=restTemplate.exchange(url, HttpMethod.GET, entityw,String.class);
        // ResponseEntity<Object> responseEntity= restTemplate.getForEntity("http://127.0.0.1:8080/infos",Object.class);
        Object object = restTemplate.getForObject(uri,Object.class);
        if(id != null){
            JSONObject json = JSONObject.fromObject(StringUtil.conventFromGzip(responseEntityw.getBody()));
            result.put("body", responseEntity.getBody());
            result.put("w", json.getJSONObject("data"));
            result.put("object", object);
            result.put("str",str);
            return result;
        }

        return null;
    }
    @GetMapping (value = "/infos")
    public Object get(){
        String str="中华人民共和国";
        return  str;
    }
}

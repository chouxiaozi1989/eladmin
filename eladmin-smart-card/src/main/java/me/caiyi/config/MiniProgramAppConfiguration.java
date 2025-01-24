package me.caiyi.config;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import me.caiyi.service.dto.MiniProgramLoginDto;
import me.caiyi.service.dto.TCardDto;
import me.caiyi.service.dto.TCardQueryCriteria;
import me.zhengjie.annotation.rest.AnonymousAccess;
import me.zhengjie.utils.PageResult;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Data
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "mini-program")
public class MiniProgramAppConfiguration {
    public String appid;
    public String appSecret;
}

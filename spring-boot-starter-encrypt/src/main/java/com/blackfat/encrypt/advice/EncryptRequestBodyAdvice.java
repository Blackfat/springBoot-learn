package com.blackfat.encrypt.advice;

import com.blackfat.encrypt.annotion.Decrypt;
import com.blackfat.encrypt.auto.EncryptProperties;
import com.blackfat.encrypt.util.AesEncryptUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author wangfeiyang
 * @desc
 *  请求数据接收处理类<br>
 *
 * 对加了@Decrypt的方法的数据进行解密操作<br>
 *
 * 只对@RequestBody参数有效
 * @create 2018/11/26-14:55
 */
@ControllerAdvice
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {

    private Logger logger = LoggerFactory.getLogger(EncryptRequestBodyAdvice.class);

    @Autowired
    private EncryptProperties encryptProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                  Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if(parameter.getMethod().isAnnotationPresent(Decrypt.class) && !encryptProperties.isDebug()){
            try {
                return new DecryptHttpInputMessage(inputMessage, encryptProperties.getKey(), encryptProperties.getCharset());
            } catch (Exception e) {
                logger.error("数据解密失败", e);
            }
        }
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}

class DecryptHttpInputMessage implements HttpInputMessage {
    private Logger logger = LoggerFactory.getLogger(EncryptRequestBodyAdvice.class);
    private HttpHeaders headers;
    private InputStream body;

    public DecryptHttpInputMessage(HttpInputMessage inputMessage, String key, String charset) throws Exception {
        this.headers = inputMessage.getHeaders();
        String content = IOUtils.toString(inputMessage.getBody(), charset);
        long startTime = System.currentTimeMillis();
        // JSON 数据格式的不进行解密操作
        String decryptBody = "";
        if (content.startsWith("{")) {
            decryptBody = content;
        } else {
            decryptBody = AesEncryptUtils.aesDecrypt(content, key);
        }
        long endTime = System.currentTimeMillis();
        logger.info("Decrypt Time:" + (endTime - startTime));
        this.body = IOUtils.toInputStream(decryptBody, charset);
    }

    @Override
    public InputStream getBody() throws IOException {
        return body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }
}

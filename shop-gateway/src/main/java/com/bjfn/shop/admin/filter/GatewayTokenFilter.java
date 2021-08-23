package com.bjfn.shop.admin.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;

@Component
@Order(0)
@Slf4j
public class GatewayTokenFilter implements GlobalFilter {

    @Value("${gateway.uri_whitelist}")
    private  String[] uri_whitelist;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        /*
        这个request对象可以获取更多的内容
       比如，如果是使用token验证的话，就可以判断它的Header中的Token值了
       为了演示方便，我就判断了它的参数
         */

        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        log.info(path);
        long count = Arrays.stream(uri_whitelist).filter(e -> path.startsWith(e)).count();
        if(count != 0){
            //放行
            return chain.filter(exchange);
        }else{
            log.info("非白明单请求"+path);
        }
        /*
        HttpHeaders headers = request.getHeaders();
        Collection<String> authorization = headers.get("Authorization");

        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String username = queryParams.getFirst("username");
        if (!username.equals("admin")) {

            //不允许访问，禁止访问
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE); //这个状态码是406

            return exchange.getResponse().setComplete();
        }*/
        //放行
        return chain.filter(exchange);
    }
}

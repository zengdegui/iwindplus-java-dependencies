/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWTUtil加解密工具类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class JwtUtil {
    private static final String SECRET = "9a96349e2345385785e804e0f4254dee";
    private static String ISSUER = "admin";

    /**
     * 生成token.
     *
     * @param claims     map
     * @param expireTime 过期时间点
     * @return String
     */
    public static String genToken(Map<String, String> claims, Integer expireTime) {
        // 使用HMAC256进行加密
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        Date expireDatePoint = new Date(System.currentTimeMillis() + expireTime * 1000);
        // 创建jwt
        JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER). // 发行人
                withExpiresAt(expireDatePoint); // 过期时间点

        // 传入参数
        claims.forEach((key, value) -> {
            builder.withClaim(key, value);
        });

        // 签名加密
        return builder.sign(algorithm);
    }

    /**
     * 解密jwt.
     *
     * @param token token
     * @return Map<String, String>
     */
    public static Map<String, String> verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        // 解密
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        Map<String, String> resultMap = new HashMap<>();
        map.forEach((key, val) -> resultMap.put(key, val.asString()));
        return resultMap;
    }
}

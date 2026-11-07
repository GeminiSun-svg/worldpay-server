package com.worldpay.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 类描述：BigDecimal序列化器
 *
 * @author GeminiSun
 * @date 2026/1/20 14:54
 */
public class BigDecimalThreeDecimalSerializer  extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal == null) {
            gen.writeNull();
        } else {
            // 四舍五入，保留3位小数
            BigDecimal scaled = bigDecimal.setScale(3, RoundingMode.HALF_UP);
            gen.writeNumber(scaled);
        }
    }
}

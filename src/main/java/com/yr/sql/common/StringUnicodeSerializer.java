package com.yr.sql.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.CharTypes;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @moduleName: StringUnicodeSerializer
 * @description: 字符串转化
 * @author: 杨睿
 * @date: 2021-03-20 10:30
 **/

public class StringUnicodeSerializer extends JsonSerializer<String> {

    private final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private final int[] ESCAPE_CODES = CharTypes.get7BitOutputEscapes();

    private void writeUnicodeEscape(JsonGenerator gen, char c) throws IOException {
        gen.writeRaw('\\');
        gen.writeRaw('u');
        gen.writeRaw(HEX_CHARS[(c >> 12) & 0xF]);
        gen.writeRaw(HEX_CHARS[(c >> 8) & 0xF]);
        gen.writeRaw(HEX_CHARS[(c >> 4) & 0xF]);
        gen.writeRaw(HEX_CHARS[c & 0xF]);
    }

    private void writeShortEscape(JsonGenerator gen, char c) throws IOException {
        gen.writeRaw('\\');
        gen.writeRaw(c);
    }

    @Override
    public void serialize(String str, JsonGenerator gen, SerializerProvider provider) throws IOException {
        int status = ((JsonWriteContext) gen.getOutputContext()).writeValue();
        switch (status) {
        case JsonWriteContext.STATUS_OK_AFTER_COLON:
            gen.writeRaw(':');
            break;

        case JsonWriteContext.STATUS_OK_AFTER_COMMA:
            gen.writeRaw(',');
            break;

        case JsonWriteContext.STATUS_EXPECT_NAME:
            throw new IOException("Can not write string value here");

        default:
            throw new IllegalStateException("Unexpected value: " + status);
        }
        // 写入JSON中字符串的开头引号
        gen.writeRaw('"');
        for (char c : str.toCharArray()) {
            if (c >= 0x80) {
                // 为所有非ASCII字符生成转义的unicode字符
                writeUnicodeEscape(gen, c);
            } else {
                // 为ASCII字符中前128个字符使用转义的unicode字符
                int code = (c < ESCAPE_CODES.length ? ESCAPE_CODES[c] : 0);
                if (code == 0) {
                    // 此处不用转义
                    gen.writeRaw(c);
                } else if (code < 0) {
                    writeUnicodeEscape(gen, (char) (-code - 1)); // 通用转义字符
                } else {
                    writeShortEscape(gen, (char) code); // 短转义字符 (\n \t ...)
                }
            }
        }
        gen.writeRaw('"');// 写入JSON中字符串的结束引号
    }

}
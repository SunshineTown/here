package com.extclp.here.fabric.utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Texts {

    public static LiteralText of(String str){
        return new LiteralText(str);
    }

    public static LiteralText of(String str, Object... args){
        LiteralText parent = new LiteralText("");
        int i = 0;String[] split = str.split("%s");
        while (i < split.length) {
            parent.append(split[i]);
            if(i < args.length){
                Object arg = args[i];
                if(arg instanceof Text){
                    parent.append((Text) arg);
                }else {
                    parent.append(arg.toString());
                }
            } else {
                StringBuilder builder = new StringBuilder();
                while (++i < split.length) {
                    builder.append("%s");
                    builder.append(split[i]);
                }
                parent.append(builder.toString());
                return parent;
            }
            i++;
        }
        for (; i < args.length; i++) {
            Object arg = args[i - 1];
            if(arg instanceof Text){
                parent.append((Text) arg);
            } else {
                parent.append(arg.toString());
            }
        }
        return parent;
    }
}

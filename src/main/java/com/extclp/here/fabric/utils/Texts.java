package com.extclp.here.fabric.utils;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Texts {

    static class TextBuilder{

        private final LiteralText parent;

        private String lastText;

        public TextBuilder() {
            parent = new LiteralText("");
        }

        public TextBuilder(LiteralText parent) {
            this.parent = parent;
        }

        public void append(Object o){
            if(o instanceof Text){
                if(lastText != null){
                    parent.append(lastText);
                }
                parent.append(((Text) o));
            } else {
                String str = o.toString();
                if(!str.isEmpty()){
                    if(lastText != null){
                        lastText += o.toString();
                    } else {
                        lastText = o.toString();
                    }
                }
            }
        }

        public LiteralText toText(){
            if(lastText != null){
                parent.append(lastText);
                lastText = null;
            }
            return parent;
        }
    }

    public static LiteralText of(String str){
        return new LiteralText(str);
    }

    public static LiteralText of(String str, Object... args){
        TextBuilder textBuilder = new TextBuilder();
        int i = 0;
        String[] split = str.split("%s");
        while (i < split.length) {
            textBuilder.append(split[i]);
            if(i < args.length){
                textBuilder.append(args[i]);
            }
            i++;
        }
        for (; i < args.length; i++) {
            textBuilder.append(args[i - 1]);
        }
        return textBuilder.toText();
    }
}

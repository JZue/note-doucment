package com.jzue.note.effectivejava.chapter2.item2;

/**
 * 遇到多个构造器时要考虑用构建起
 *
 *
 * @author jzue
 * @date 2019/9/9 下午1:56
 **/
public class Item2 {

    private int field1;

    private int field2;
    // 非必填参数
    private int optionalField3;
    // 非必填参数
    private int optionalField4;
    // 非必填参数
    private int optionalField5;

    public static class Builder{

        private int field1;

        private int field2;
        // 非必填参数
        private int optionalField3;
        // 非必填参数
        private int optionalField4;
        // 非必填参数
        private int optionalField5;

        public Builder(int filed1,int field2){
            this.field1=filed1;
            this.field2=field2;
        }

        public Builder setOptionalField3(int optionalField3) {
            this.optionalField3 = optionalField3;
            return this;
        }
        public Builder setOptionalField4(int optionalField4) {
            this.optionalField4 = optionalField4;
            return this;
        }
        public Builder setOptionalField5(int optionalField5) {
            this.optionalField5 = optionalField5;
            return this;
        }
        public Item2 build(){
            return new Item2(this);
        }

    }

    public Item2(Builder builder) {
        this.field1 = field1;
        this.field2 = field2;
        this.optionalField3 = optionalField3;
        this.optionalField4 = optionalField4;
        this.optionalField5 = optionalField5;
    }

    public static void main(String[] args) {
        Item2 item2=
                new Builder(1,2)
                .setOptionalField3(3)
                .setOptionalField4(4)
                .setOptionalField5(5).build();
    }
}

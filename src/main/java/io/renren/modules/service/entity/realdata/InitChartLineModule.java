package io.renren.modules.service.entity.realdata;

import lombok.Data;


@Data
public class InitChartLineModule {

    private String name;
    private String type;
    private String stack;
    private int[] data;


    public InitChartLineModule() {
    }
    public InitChartLineModule(String name, String type, String stack, int[] data) {
        this.name = name;
        this.type = type;
        this.stack = stack;
        this.data = data;
    }

}

package com.jzue.study.rcs;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

/**
 * @Author: junzexue
 * @Date: 2018/12/24 下午3:52
 * @Description:
 **/
public class InstructionsConfigrationSelector extends AdviceModeImportSelector<Instructions> {
    @Override
    protected String[] selectImports(AdviceMode adviceMode) {

        return new String[0];
    }
}

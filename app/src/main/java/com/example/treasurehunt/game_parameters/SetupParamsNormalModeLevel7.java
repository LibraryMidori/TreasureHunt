package com.example.treasurehunt.game_parameters;

import com.example.treasurehunt.constant.GameParameterConstant;

/**
 * Created by library on 2016/03/14.
 */
public class SetupParamsNormalModeLevel7 implements SetupGameParameters {
    @Override
    public int getTotalTrap() {
        return GameParameterConstant.getMinTraps() + 7;
    }

    @Override
    public int getPlayTime() {
        return GameParameterConstant.getMaxTime() - 120;
    }
}
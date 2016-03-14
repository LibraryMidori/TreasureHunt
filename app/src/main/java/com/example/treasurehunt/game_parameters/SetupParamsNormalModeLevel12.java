package com.example.treasurehunt.game_parameters;

import com.example.treasurehunt.constant.GameParameterConstant;

/**
 * Created by library on 2016/03/14.
 */
public class SetupParamsNormalModeLevel12 implements SetupGameParameters {
    @Override
    public int getTotalTrap() {
        return GameParameterConstant.getMinTraps() + 12;
    }

    @Override
    public int getPlayTime() {
        return GameParameterConstant.getMaxTime() - 180;
    }
}
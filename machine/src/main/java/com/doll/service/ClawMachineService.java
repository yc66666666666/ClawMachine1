package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.common.R;
import com.doll.entity.ClawMachine;

import java.util.Map;

public interface ClawMachineService  extends IService<ClawMachine> {
    public void saveMachine(ClawMachine clawMachine);
    public void deleteByIds1(String ids);
    public void changeStatus(int status,String id);
    public R<Map> getBeingChangedClawMachine(Long id);
    public void changeMachine(ClawMachine clawMachine);

}

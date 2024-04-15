package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.ClawMachine;

public interface ClawMachineService  extends IService<ClawMachine> {
    public void saveMachine(ClawMachine clawMachine);
    public void deleteByIds1(String ids);
    public void changeStatus(int status,String id);

}

package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.entity.ClawMachine;
import com.doll.mapper.CategoryMapper;
import com.doll.mapper.ClawMachineMapper;
import com.doll.service.ClawMachineService;
import com.doll.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClawMachineServiceImpl extends ServiceImpl<ClawMachineMapper, ClawMachine> implements ClawMachineService {

    @Autowired
    private ComponentService componentService;



    @Override
    @Transactional
    public void saveMachine(ClawMachine clawMachine) {
        List<Long> list=new ArrayList<>();
        Long sensorId=clawMachine.getSensorId();
        Long controllerId=clawMachine.getControllerId();
        Long cameraId=clawMachine.getCameraId();
        list.add(sensorId);
        list.add(controllerId);
        list.add(cameraId);
        String ids=list.stream().map(String::valueOf).collect(Collectors.joining(","));
        componentService.changeStatus(1,ids);
        super.save(clawMachine);
    }

    @Override
    @Transactional
    public void deleteByIds1(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        for(Long id:idList){
            ClawMachine clawMachine=super.getById(id);
            if(clawMachine!=null){
                List<Long> list=new ArrayList<>();
                Long sensorId=clawMachine.getSensorId();
                Long controllerId=clawMachine.getControllerId();
                Long cameraId=clawMachine.getCameraId();
                list.add(sensorId);
                list.add(controllerId);
                list.add(cameraId);
                String componentIds=list.stream().map(String::valueOf).collect(Collectors.joining(","));
                componentService.changeStatus(0,componentIds);
            }
        }
        super.removeByIds(idList);
    }

    @Override
    public void changeStatus(int status, String ids) {
        List<Long> idList=Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<ClawMachine> clawMachineList=new ArrayList<>();
        for(Long id:idList){
            ClawMachine clawMachine=new ClawMachine();
            clawMachine.setStatus(status);
            clawMachine.setId(id);
            clawMachineList.add(clawMachine);
        }
        super.updateBatchById(clawMachineList);
    }
}

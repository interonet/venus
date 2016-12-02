package org.interonet.venus.controller;

import org.interonet.venus.domain.SwitchToSwitchTunnel;
import org.interonet.venus.domain.SwitchToVMTunnel;
import org.interonet.venus.service.ConfigService;
import org.interonet.venus.service.LibvirtService;
import org.interonet.venus.service.SnmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/core")
@EnableAutoConfiguration
public class CoreController {

    @Autowired
    LibvirtService libvirtService;

    @Autowired
    ConfigService configService;

    @Autowired
    SnmpService snmpService;

    @RequestMapping(value = "/vm/{vmId}", method = RequestMethod.GET)
    public void powerOnVm(@PathVariable("vmId") Integer vmId){
        return libvirtService.getVmInfo(vmId);
    }

    @RequestMapping(value = "/vm/{vmId}", method = RequestMethod.PUT)
    public void powerOnVm(@PathVariable("vmId") Integer vmId){
        libvirtService.powerOnVm(vmId);
    }

    @RequestMapping(value = "/vm/{vmId}", method = RequestMethod.DELETE)
    public void powerOffVm(@PathVariable("vmId") Integer vmId){
        libvirtService.powerOffVm(vmId);
    }

    @RequestMapping(value = "/tunnel/switch_to_switch", method = RequestMethod.PUT)
    public void createSwitchToSwitchTunnel(@RequestBody SwitchToSwitchTunnel tunnel){
        int switchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getSwitchId(), tunnel.getSwitchIdPortNum());
        int athrSwitchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getPeerSwitchId(), tunnel.getPeerSwitchIdPortNum());
        snmpService.createTunnelSW2SW(switchPeerPortOnTT, athrSwitchPeerPortOnTT);
    }

    @RequestMapping(value = "/tunnel/switch_to_switch", method = RequestMethod.DELETE)
    public void createSwitchToSwitchTunnel(@RequestBody SwitchToSwitchTunnel tunnel){
        int switchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getSwitchId(), tunnel.getSwitchIdPortNum());
        int athrSwitchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getPeerSwitchId(), tunnel.getPeerSwitchIdPortNum());
        snmpService.deleteTunnelSW2SW(switchPeerPortOnTT, athrSwitchPeerPortOnTT);
    }

    @RequestMapping(value = "/tunnel/switch_to_vm", method = RequestMethod.PUT)
    public void createSwitchToSwitchTunnel(@RequestBody SwitchToVMTunnel tunnel){
        int switchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getSwitchId(), tunnel.getSwitchPort());
        int vmVlanId = configService.getVlanIdByVM(tunnel.getVmId(), tunnel.getVmPort());
        snmpService.createTunnelSW2VM(switchPeerPortOnTT, vmVlanId);
    }

    @RequestMapping(value = "/tunnel/switch_to_vm", method = RequestMethod.DELETE)
    public void createSwitchToSwitchTunnel(@RequestBody SwitchToVMTunnel tunnel){
        int switchPeerPortOnTT = configService.getTopologyTransformerPortFromPeerPort(tunnel.getSwitchId(), tunnel.getSwitchPort());
        int vmVlanId = configService.getVlanIdByVM(tunnel.getVmId(), tunnel.getVmPort());
        snmpService.deleteTunnelSW2VM(switchPeerPortOnTT, vmVlanId);
    }
}

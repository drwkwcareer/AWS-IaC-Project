package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;

import java.util.Collections;
import java.util.Map;

public class Ec2Stack extends Stack {
    public Ec2Stack(final Construct scope, final String id, final StackProps props, Vpc vpc, SecurityGroup sg) {
        super(scope, id, props);

        // User Data
        UserData userData = UserDataUtil.createGitLabUserData();

        // EC2 Instance
        ISubnet publicSubnet = vpc.getPublicSubnets().get(0);
        Instance instance = Instance.Builder.create(this, "gitlab-server")
            .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.LARGE))
            .machineImage(MachineImage.genericLinux(Map.of("ap-northeast-3", "ami-0aafffc426e129572")))
            .vpc(vpc)
            .vpcSubnets(SubnetSelection.builder().subnets(Collections.singletonList(publicSubnet)).build())
            .keyName("Wang-OSK")
            .securityGroup(sg)
            .blockDevices(Collections.singletonList(BlockDevice.builder().deviceName("/dev/sda1").volume(BlockDeviceVolume.ebs(500)).build()))
            .associatePublicIpAddress(true)
            .userData(userData)
            .build();
    }
}

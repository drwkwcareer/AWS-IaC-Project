package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;

public class SecurityGroupStack extends Stack {
    public final SecurityGroup sg;

    public SecurityGroupStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);

        sg = SecurityGroup.Builder.create(this, "GitlabSG")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();
        sg.addIngressRule(Peer.anyIpv4(), Port.allTraffic(), "Allow all inbound traffic");
    }
}

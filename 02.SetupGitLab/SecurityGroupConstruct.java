package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.services.ec2.*;

public class SecurityGroupConstruct extends Construct {
    public final SecurityGroup sg;

    public SecurityGroupConstruct(final Construct scope, final String id, Vpc vpc) {
        super(scope, id);

        sg = SecurityGroup.Builder.create(this, "GitlabSG")
                .vpc(vpc)
                .allowAllOutbound(true)
                .build();
        sg.addIngressRule(Peer.anyIpv4(), Port.allTraffic(), "Allow all inbound traffic (INSECURE)");
    }
}

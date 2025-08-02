package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;

public class VpcConstruct extends Construct {
    public final Vpc vpc;

    public VpcConstruct(final Construct scope, final String id) {
        super(scope, id);

        vpc = Vpc.Builder.create(this, "MyVpc")
            .maxAzs(2)
            .subnetConfiguration(Arrays.asList(
                SubnetConfiguration.builder()
                    .subnetType(SubnetType.PUBLIC)
                    .name("PublicSubnet")
                    .cidrMask(24)
                    .build(),
                SubnetConfiguration.builder()
                    .subnetType(SubnetType.PRIVATE_WITH_EGRESS)
                    .name("PrivateSubnet")
                    .cidrMask(24)
                    .build()
            ))
            .build();
    }
}

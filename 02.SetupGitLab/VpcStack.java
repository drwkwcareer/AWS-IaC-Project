package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.ec2.*;

import java.util.Arrays;

public class VpcStack extends Stack {
    public final Vpc vpc;

    public VpcStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public VpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

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

         TaggingUtil.tagVpcAndSubnets(vpc);
    }
}

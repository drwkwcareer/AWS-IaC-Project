package com.myorg;

import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.ec2.*;
import java.util.List;

public class TaggingUtil {
    public static void tagVpcAndSubnets(Vpc vpc) {
        Tags.of(vpc).add("Name", "VPC-For-EC2");
        for (ISubnet subnet : vpc.getPublicSubnets()) {
            Tags.of(subnet).add("Name", "PublicSubnet-" + subnet.getAvailabilityZone());
        }
        for (ISubnet subnet : vpc.getPrivateSubnets()) {
            Tags.of(subnet).add("Name", "PrivateSubnet-" + subnet.getAvailabilityZone());
        }
    }
}

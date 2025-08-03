package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.eks.*;

import java.util.List;

public class EksStack extends Stack {
    public final Cluster eksCluster;

    public EksStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);

        eksCluster = Cluster.Builder.create(this, "EKSArgoCD")
            .version(KubernetesVersion.V1_29)
            .vpc(vpc)
            .vpcSubnets(List.of(
                SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build()
            ))
            .defaultCapacity(0)
            .build();

        eksCluster.addFargateProfile("kube-system", FargateProfileOptions.builder()
            .selectors(List.of(
                FargateProfileSelector.builder().namespace("kube-system").build()
            ))
            .subnets(List.of(
                SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build()
            ))
            .build());

        eksCluster.addFargateProfile("argocd", FargateProfileOptions.builder()
            .selectors(List.of(
                FargateProfileSelector.builder().namespace("argocd").build()
            ))
            .subnets(List.of(
                SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build()
            ))
            .build());

    }
}

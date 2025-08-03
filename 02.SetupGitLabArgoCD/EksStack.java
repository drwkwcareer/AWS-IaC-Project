package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.eks.*;
import software.amazon.awscdk.services.lambda.ILayerVersion;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.Tags;
import java.util.*;

public class EksStack extends Stack {
    public final Cluster eksCluster;

    public EksStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);
        
        ILayerVersion kubectlLayer = LayerVersion.fromLayerVersionArn(
            this,
            "KubectlLayerV33",
            "arn:aws:lambda:ap-northeast-3:386710736237:layer:kubectl:1"
        );

        eksCluster = Cluster.Builder.create(this, "EKSArgoCD")
            .clusterName("eks-cluster-argocd")
            .version(KubernetesVersion.V1_33)
            .vpc(vpc)
            .kubectlLayer(kubectlLayer)
            .vpcSubnets(List.of(
                SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build()
            ))
            .defaultCapacity(0)
            .build();
        
        eksCluster.getAwsAuth().addMastersRole(Role.fromRoleArn(this, "AdminRole", "arn:aws:iam::<account>:role/<role>"));
        
        FargateProfile.Builder.create(this, "kube-system-fargate-profile")
            .cluster(eksCluster)
            .fargateProfileName("kube-system")
            .selectors(List.of(Selector.builder().namespace("kube-system").build()))
            .subnetSelection(SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build())
            .build();

        FargateProfile.Builder.create(this, "argocd-fargate-profile")
            .cluster(eksCluster)
            .fargateProfileName("argocd")
            .selectors(List.of(Selector.builder().namespace("argocd").build()))
            .subnetSelection(SubnetSelection.builder().subnetType(SubnetType.PRIVATE_WITH_EGRESS).build())
            .build();

        for (ISubnet subnet : vpc.getPublicSubnets()) {
            Tags.of(subnet).add("kubernetes.io/role/elb", "1");
            Tags.of(subnet).add("kubernetes.io/cluster/eks-cluster-argocd", "shared");
        }
        for (ISubnet subnet : vpc.getPrivateSubnets()) {
            Tags.of(subnet).add("kubernetes.io/role/internal-elb", "1");
            Tags.of(subnet).add("kubernetes.io/cluster/eks-cluster-argocd", "shared");
        }
        
    }
}

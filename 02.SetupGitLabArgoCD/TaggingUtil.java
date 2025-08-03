package com.myorg;

import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.ec2.*;
import software.constructs.IConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaggingUtil {
    public static void tagVpcAndSubnets(Vpc vpc) {
        Tags.of(vpc).add("Name", "VPC-ArgoCD-Gitlab");
        for (ISubnet subnet : vpc.getPublicSubnets()) {
            Tags.of(subnet).add("Name", "PublicSubnet-" + subnet.getAvailabilityZone());
        }
        for (ISubnet subnet : vpc.getPrivateSubnets()) {
            Tags.of(subnet).add("Name", "PrivateSubnet-" + subnet.getAvailabilityZone());
        }

        // サブネットID→AZ のマッピング
        Map<String, String> subnetIdToAz = new HashMap<>();
        for (ISubnet subnet : vpc.getPublicSubnets()) {
            if (subnet instanceof Subnet) {
                subnetIdToAz.put(((Subnet) subnet).getSubnetId(), subnet.getAvailabilityZone());
            }
        }
        for (ISubnet subnet : vpc.getPrivateSubnets()) {
            if (subnet instanceof Subnet) {
                subnetIdToAz.put(((Subnet) subnet).getSubnetId(), subnet.getAvailabilityZone());
            }
        }

        // NAT Gatewayタグ付け
        for (IConstruct construct : vpc.getNode().findAll()) {
            if (construct instanceof CfnNatGateway natGateway) {
                String subnetId = natGateway.getSubnetId();
                String az = subnetIdToAz.getOrDefault(subnetId, "unknown");
                Tags.of(natGateway).add("Name", "NATGateway-" + az);
            }
        }

        // Internet Gatewayタグ付け（AZは無いので "Internet Gateway" のみ）
        for (IConstruct construct : vpc.getNode().findAll()) {
            if (construct instanceof CfnInternetGateway igw) {
                Tags.of(igw).add("Name", "InternetGateway");
            }
        }

        // ENIタグ付け（AZ付き）
        for (IConstruct construct : vpc.getNode().findAll()) {
            if (construct instanceof CfnNetworkInterface eni) {
                String subnetId = eni.getSubnetId();
                String az = subnetIdToAz.getOrDefault(subnetId, "unknown");
                Tags.of(eni).add("Name", "ENI-" + az);
            }
        }
    }
}

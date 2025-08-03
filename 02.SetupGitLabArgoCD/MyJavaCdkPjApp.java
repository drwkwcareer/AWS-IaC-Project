package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class MyJavaCdkPjApp {
    public static void main(final String[] args) {
        App app = new App();

        VpcStack vpcStack =new VpcStack(app, "VpcStack", StackProps.builder().build());

        SecurityGroupStack sgStack = new SecurityGroupStack(
                app,
                "SecurityGroupStack",
                StackProps.builder().build(),
                vpcStack.vpc
        );

        Ec2Stack ec2Stack = new Ec2Stack(
                app,
                "Ec2Stack",
                StackProps.builder().build(),
                vpcStack.vpc,
                sgStack.sg
        );
        
        EksStack eksStack = new EksStack(app, "EksStack", StackProps.builder().build(), vpcStack.vpc);
        
        app.synth();
    }
}

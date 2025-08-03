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

        app.synth();
    }
}

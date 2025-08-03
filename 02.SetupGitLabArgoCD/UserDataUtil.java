package com.myorg;

import software.amazon.awscdk.services.ec2.UserData;

public class UserDataUtil {

    /**
     * Creates UserData for installing and configuring GitLab CE on a Linux instance.
     * @return UserData with the necessary commands.
     */
    public static UserData createGitLabUserData() {
        UserData userData = UserData.forLinux();
        userData.addCommands(
            "apt-get update -y",
            "apt-get upgrade -y",
            "DEBIAN_FRONTEND=noninteractive apt-get install -y curl openssh-server ca-certificates tzdata perl",
            "curl https://packages.gitlab.com/install/repositories/gitlab/gitlab-ce/script.deb.sh | bash",
            "TOKEN=$(curl -X PUT \"http://169.254.169.254/latest/api/token\" -H \"X-aws-ec2-metadata-token-ttl-seconds: 21600\")",
            "PUBLIC_IP=$(curl -H \"X-aws-ec2-metadata-token: $TOKEN\" -s http://169.254.169.254/latest/meta-data/public-ipv4)",
            "export EXTERNAL_URL=\"http://$PUBLIC_IP\"",
            "DEBIAN_FRONTEND=noninteractive apt-get install -y gitlab-ce",
            "gitlab-ctl reconfigure"
        );
        return userData;
    }
}

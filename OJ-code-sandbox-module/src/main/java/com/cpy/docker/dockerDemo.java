package com.cpy.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * @Author:成希德
 */
public class dockerDemo {
    public static void main(String[] args) {
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://192.168.99.134:2375").build();
        PingCmd pingCmd = dockerClient.pingCmd();
        pingCmd.exec();
    }
}

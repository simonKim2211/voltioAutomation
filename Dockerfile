FROM jenkins/inbound-agent:latest-jdk21

USER root

RUN apt-get update && apt-get install -y \
    git \
    curl \
    wget \
    unzip \
    maven \
    chromium \
    chromium-driver \
    && rm -rf /var/lib/apt/lists/*

ENV CHROME_BIN=/usr/bin/chromium
ENV CHROMEDRIVER_BIN=/usr/bin/chromedriver

USER jenkins

WORKDIR /home/jenkins/agent

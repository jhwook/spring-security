FROM ubuntu:latest
LABEL authors="jhwook"

ENTRYPOINT ["top", "-b"]
FROM ubuntu:latest

RUN apt-get update -y 

RUN apt-get install -y python3 \
    python3-pip \
    python3-dev

RUN pip3 install scikit-learn pandas matplotlib numpy

COPY ./* /questions/
COPY ./intent/* /questions/intent/

ENTRYPOINT [ "python3", "/questions/intent/intent.py" ]
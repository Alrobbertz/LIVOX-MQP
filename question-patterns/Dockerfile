FROM ubuntu:latest

RUN apt-get update -y 

RUN apt-get install -y python3 \
    python3-pip \
    python3-dev

RUN pip3 install scikit-learn pandas matplotlib numpy

COPY ./*.csv /questions/
COPY ./*.py /questions/

ENTRYPOINT [ "python3", "/questions/dt.py" ]
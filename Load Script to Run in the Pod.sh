#!/bin/bash
while true; do
  curl http://qr-gen-service:8080/qr_gen_api/api/v1/message/loadTest
  #sleep 5  # Adjust the interval as needed
done

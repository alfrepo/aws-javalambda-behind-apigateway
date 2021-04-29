#!/bin/bash
BUCKET_NAME="arc-iam-dev-newiamtonavvisadapter-s3"
echo $BUCKET_NAME > bucket-name.txt
aws s3 mb s3://$BUCKET_NAME

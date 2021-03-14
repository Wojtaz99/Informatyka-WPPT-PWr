#!/bin/bash

IFS=$'\n'
for file in $(find $1 -type f); 
do
     echo $file
done

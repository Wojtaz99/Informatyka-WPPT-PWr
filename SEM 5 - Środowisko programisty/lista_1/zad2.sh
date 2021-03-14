#!/bin/bash

IFS=$'\n'

(for File in $(find $1 -type f);
    do
    Slowa=$(tr '[A-Z] ' '[a-z]\n' < $File)
    for Slowo in $Slowa; 
    do
        echo $Slowo
    done
done) | sort | uniq -c






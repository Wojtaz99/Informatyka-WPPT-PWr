#!/bin/bash

IFS=$'\n' 
(for Plik in $(find $1 -type f); 
    do
    Slowa=$(tr '[A-Z] ' '[a-z]\n' < $Plik) 
    (for Slowo in $Slowa; 
    do
        echo $Slowo
    done) | sort | uniq
done) | sort | uniq -c
